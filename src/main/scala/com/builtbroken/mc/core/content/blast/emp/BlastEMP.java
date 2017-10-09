package com.builtbroken.mc.core.content.blast.emp;

import com.builtbroken.jlib.debug.DebugPrinter;
import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.energy.IEMReceptiveDevice;
import com.builtbroken.mc.api.energy.IVoltageTransmitter;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.items.energy.IEMInterferenceItem;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.energy.UniversalEnergySystem;
import com.builtbroken.mc.framework.explosive.blast.Blast;
import com.builtbroken.mc.imp.transform.region.Sphere;
import com.builtbroken.mc.imp.transform.sorting.Vector3DistanceComparator;
import com.builtbroken.mc.imp.transform.vector.BlockPos;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.world.map.radar.RadarMap;
import com.builtbroken.mc.lib.world.map.radar.data.RadarObject;
import com.builtbroken.mc.lib.world.map.radar.data.RadarTile;
import com.builtbroken.mc.lib.world.map.tile.TileMapRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Energy pulse wave that disables and damages machines nearby. Can path threw most walls but loses power each object is passes threw.
 * Can damage entities near metal objects, destroy machines, remove power, and disable generators. In order to disable machines it
 * creates a wrapper object that sucks power out of the machine each tick. This way it is unable to continue to output power to other
 * machines around it.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/14/2015.
 */
public class BlastEMP extends Blast<BlastEMP> implements IVoltageTransmitter
{
    public static final String START_AUDIO = "icbm:icbm.taser";
    public static final String EDIT_AUDIO = "icbm:icbm.spark";
    public static final String END_AUDIO = "icbm:icbm.emp";

    protected DebugPrinter debugPrinter;


    public BlastEMP(IExplosiveHandler handler)
    {
        super(handler);
        debugPrinter = new DebugPrinter(Engine.logger());
    }

    @Override
    public void getEffectedBlocks(final List<IWorldEdit> edits)
    {
        //Debug
        debugPrinter.start("BlastEmp#getEffectedBlocks()", "Starting emp");
        long time = System.nanoTime();

        //Get tiles
        RadarMap map = TileMapRegistry.getRadarMapForWorld(oldWorld);
        if (map != null)
        {
            //Store tiles in linked list to easily allow reordering, expansion, and removal with minimal CPU usage
            LinkedList<BlockPos> tileLocations = new LinkedList();

            //Find all valid tiles
            List<RadarObject> objects = map.getRadarObjects(x(), z(), size);
            for (RadarObject object : objects)
            {
                if (object instanceof RadarTile)
                {
                    //Get tile and make sure its valid, no point in pathing invalid stuff
                    TileEntity tile = ((RadarTile) object).tile;
                    if (!tile.isInvalid())
                    {
                        //Handle if EMP or energy
                        if ((tile instanceof IEMReceptiveDevice || UniversalEnergySystem.isHandler(tile, null)))
                        {
                            tileLocations.add(new BlockPos(tile.xCoord, tile.yCoord, tile.zCoord));
                        }
                        //Handle if inventory
                        else if (tile instanceof IInventory)
                        {
                            IInventory inventory = (IInventory) tile;

                            //Well only handle if has items that can be effected
                            if (inventoryContainsElectricItems(inventory))
                            {
                                tileLocations.add(new BlockPos(tile.xCoord, tile.yCoord, tile.zCoord));
                            }
                            //TODO track emp value of inventory even if not added (e.g. chest full of iron will stop an emp)
                        }
                        else
                        {
                            //TODO add EMP handlers to allow EMP effects on tiles that are not normally EMP prone
                        }
                    }
                }
            }

            //Sort with largest distance moving to the front
            Collections.sort(tileLocations, new Vector3DistanceComparator(this, false));
            //High distance is used to allow the path to go through all tiles before hitting the tile.
            //  This way energy is consumed by the time it gets to the tile providing a weaker value.
            //  Similar to how a real emp would work with the energy being absorbed by objects

            //Loop valid tiles
            while (!tileLocations.isEmpty())
            {
                //Get next position
                BlockPos pos = tileLocations.poll();

                //Get tile at position
                TileEntity tile = pos.getTileEntity(oldWorld);

                //Ensure tile is valid
                if (tile != null && !tile.isInvalid())
                {
                    //Ensure position is inside of emp range
                    double distance = pos.distance(this);
                    if (distance < getEMPRange())
                    {
                        //Start emp path to target tile
                        startEmpPath(tile, distance, getPowerForRange(distance), tileLocations, edits);
                        //Power is starting power, not power at distance. As it will pass through blocks losing power
                    }
                }
            }
        }

        //Debug
        time = System.nanoTime() - time;
        debugPrinter.end("Done... " + StringHelpers.formatNanoTime(time));
    }

    @Override
    public void doEffectOther(boolean beforeBlocksPlaced)
    {
        super.doEffectOther(beforeBlocksPlaced);
        //TODO recode to run with the block ray trace to save on CPU resources
        //TODO consider spreading over several ticks if entity count > is greater than a pre-determined amount that results in lag
        //TODO use delay action to spread over several ticks

        if (!beforeBlocksPlaced)
        {
            //get entities
            Pos center = toPos();
            Sphere sphere = new Sphere(center, size);
            List<Entity> entities = sphere.getEntities(world.unwrap(), Entity.class);
            if (entities != null && !entities.isEmpty())
            {
                for (Entity entity : entities)
                {
                    //TODO add entities based on equipment
                    //TODO damage entities based on equipment
                    if (entity instanceof IInventory)
                    {
                        applyEmpEffect((IInventory) entity, entity, this, getPowerForRange(size), center.distance(entity));
                    }
                    else if (entity instanceof EntityPlayer)
                    {
                        applyEmpEffect(((EntityPlayer) entity).inventory, entity, this, getPowerForRange(size), center.distance(entity));
                        ((EntityPlayer) entity).inventoryContainer.detectAndSendChanges();
                    }
                    else if (entity instanceof EntityItem)
                    {
                        applyEmpEffect(entity, ((EntityItem) entity).getEntityItem(), 0, false, this, getPowerForRange(size), center.distance(entity));
                    }
                }
            }
        }
    }

    /**
     * Called to start the emp path towards the target tile
     *
     * @param tile          - tile to hit, end point of ray trace
     * @param distance      - distance to target
     * @param power
     * @param tileLocations
     * @param edits
     * @return
     */
    protected double startEmpPath(TileEntity tile, double distance, double power, LinkedList<BlockPos> tileLocations, List<IWorldEdit> edits)
    {
        //TODO modify ray trace to not start from center each time
        power = rayTraceBlocks(toPos().toVec3(), Vec3.createVectorHelper(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5), power, tileLocations, edits, true);
        return handle(tile, distance, power, edits, true); //TODO check if the last tile is getting hit twice
    }

    /**
     * Called to handle setting up the EMP effect for the tile
     *
     * @param tile     - affected tile
     * @param distance - distance from center of EMP
     * @param power    - power passing through tile
     * @param edits    - list of edits, if tile is affected and edit will be added for said tile
     * @return power left over after passing through tile
     */
    protected double handle(TileEntity tile, double distance, double power, List<IWorldEdit> edits, boolean doEdits)
    {
        if (power > 1)
        {
            IWorldEdit edit = null;
            double powerUsed = 0;
            if (tile instanceof IEMReceptiveDevice)
            {
                powerUsed = ((IEMReceptiveDevice) tile).onElectromagneticRadiationApplied(this, distance, power, false);
                if (doEdits)
                {
                    edit = doEMP((IEMReceptiveDevice) tile, distance, power);
                }
            }
            else if (UniversalEnergySystem.isHandler(tile, null))
            {
                if (doEdits)
                {
                    edit = drainEnergy(tile, distance, power);
                }
                //TODO calculate power used by EMP action
            }
            if (edit != null)
            {
                edits.add(edit);
            }
            return powerUsed < power ? power - powerUsed : 0;
        }
        return power;
    }

    protected IWorldEdit doEMPInventory(TileEntity tile, double distance, double power)
    {
        return null; //TODO implement inventory EMP
    }

    protected boolean inventoryContainsElectricItems(IInventory inventory)
    {
        return false;
    }

    protected IWorldEdit doEMPInventory(Entity tile, double distance, double power)
    {
        return null; //TODO implement inventory EMP
    }

    protected IWorldEdit doEMP(IEMReceptiveDevice tile, double distance, double power)
    {
        //System.out.println("EMP tile effect: " + tile + " " + distance + " " + power);
        return new EmpEdit(new Location((TileEntity) tile), this, distance, power);
    }

    protected IWorldEdit drainEnergy(TileEntity tile, double distance, double power)
    {
        //System.out.println("EMP tile drain: " + tile + " " + distance + " " + power);
        return new EmpDrainEdit(new Location(tile), this, distance, power);
    }

    /**
     * Range of the EMP
     *
     * @return range in meters
     */
    protected double getEMPRange()
    {
        return size;
    }

    /**
     * How much power will be released by the EMP for the
     * given range. This is not the power at the location
     * but rather the power released by the emp from
     * the center point towards the distance value.
     *
     * @param distance - range of the emp in meters
     * @return power
     */
    protected double getPowerForRange(double distance)
    {
        if (distance == -1)
        {
            return (4 / 3) * Math.PI * (size * size * size);
        }
        return distance * 100;
    }

    @Override
    public void doStartAudio()
    {
        if (!oldWorld.isRemote)
        {
            oldWorld.playSoundEffect(x, y, z, START_AUDIO, 0.2F + oldWorld.rand.nextFloat() * 0.2F, 0.9F + oldWorld.rand.nextFloat() * 0.15F);
        }
    }

    @Override
    public void doEndAudio()
    {
        if (!oldWorld.isRemote)
        {
            oldWorld.playSoundEffect(x, y, z, END_AUDIO, 0.2F + oldWorld.rand.nextFloat() * 0.2F, 0.9F + oldWorld.rand.nextFloat() * 0.15F);
        }
    }

    @Override
    public void playAudioForEdit(IWorldEdit blocks)
    {
        if (!oldWorld.isRemote)
        {
            oldWorld.playSoundEffect(blocks.x(), blocks.y(), blocks.z(), EDIT_AUDIO, 0.2F + oldWorld.rand.nextFloat() * 0.2F, 0.9F + oldWorld.rand.nextFloat() * 0.15F);
        }
    }

    /**
     * Custom ray trace implementation that doesn't stop when it hits a block. Instead it
     * passes through the block apply effects then continuing to the next block.
     *
     * @param start         - point to begin the ray trace
     * @param end           = point to stop the ray trace
     * @param power         - power to use while ray tracing
     * @param tileLocations - list of tile locations that will be traced it not hit. Will be modified
     *                      as the ray hits tiles moving towards the target.
     * @param edits         - list of edits created while pathing
     * @return power left at the end of the ray
     */
    public double rayTraceBlocks(Vec3 start, Vec3 end, double power, LinkedList<BlockPos> tileLocations, List<IWorldEdit> edits, boolean doEdits)
    {
        if (!Double.isNaN(start.xCoord) && !Double.isNaN(start.yCoord) && !Double.isNaN(start.zCoord))
        {
            if (!Double.isNaN(end.xCoord) && !Double.isNaN(end.yCoord) && !Double.isNaN(end.zCoord))
            {
                int end_x = MathHelper.floor_double(end.xCoord);
                int end_y = MathHelper.floor_double(end.yCoord);
                int end_z = MathHelper.floor_double(end.zCoord);

                int xx = MathHelper.floor_double(start.xCoord);
                int yy = MathHelper.floor_double(start.yCoord);
                int zz = MathHelper.floor_double(start.zCoord);

                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(start.xCoord) || Double.isNaN(start.yCoord) || Double.isNaN(start.zCoord))
                    {
                        return power;
                    }

                    //Current equals end, return null
                    if (xx == end_x && yy == end_y && zz == end_z)
                    {
                        return power;
                    }

                    boolean flag6 = true;
                    boolean flag3 = true;
                    boolean flag4 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (end_x > xx)
                    {
                        d0 = (double) xx + 1.0D;
                    }
                    else if (end_x < xx)
                    {
                        d0 = (double) xx + 0.0D;
                    }
                    else
                    {
                        flag6 = false;
                    }

                    if (end_y > yy)
                    {
                        d1 = (double) yy + 1.0D;
                    }
                    else if (end_y < yy)
                    {
                        d1 = (double) yy + 0.0D;
                    }
                    else
                    {
                        flag3 = false;
                    }

                    if (end_z > zz)
                    {
                        d2 = (double) zz + 1.0D;
                    }
                    else if (end_z < zz)
                    {
                        d2 = (double) zz + 0.0D;
                    }
                    else
                    {
                        flag4 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;

                    //Delta distance
                    double d6 = end.xCoord - start.xCoord;
                    double d7 = end.yCoord - start.yCoord;
                    double d8 = end.zCoord - start.zCoord;

                    //Normalization
                    if (flag6)
                    {
                        d3 = (d0 - start.xCoord) / d6;
                    }

                    if (flag3)
                    {
                        d4 = (d1 - start.yCoord) / d7;
                    }

                    if (flag4)
                    {
                        d5 = (d2 - start.zCoord) / d8;
                    }

                    byte b0;

                    if (d3 < d4 && d3 < d5)
                    {
                        if (end_x > xx)
                        {
                            b0 = 4;
                        }
                        else
                        {
                            b0 = 5;
                        }

                        start.xCoord = d0;
                        start.yCoord += d7 * d3;
                        start.zCoord += d8 * d3;
                    }
                    else if (d4 < d5)
                    {
                        if (end_y > yy)
                        {
                            b0 = 0;
                        }
                        else
                        {
                            b0 = 1;
                        }

                        start.xCoord += d6 * d4;
                        start.yCoord = d1;
                        start.zCoord += d8 * d4;
                    }
                    else
                    {
                        if (end_z > zz)
                        {
                            b0 = 2;
                        }
                        else
                        {
                            b0 = 3;
                        }

                        start.xCoord += d6 * d5;
                        start.yCoord += d7 * d5;
                        start.zCoord = d2;
                    }

                    Vec3 vec32 = Vec3.createVectorHelper(start.xCoord, start.yCoord, start.zCoord);
                    xx = (int) (vec32.xCoord = (double) MathHelper.floor_double(start.xCoord));

                    if (b0 == 5)
                    {
                        --xx;
                        ++vec32.xCoord;
                    }

                    yy = (int) (vec32.yCoord = (double) MathHelper.floor_double(start.yCoord));

                    if (b0 == 1)
                    {
                        --yy;
                        ++vec32.yCoord;
                    }

                    zz = (int) (vec32.zCoord = (double) MathHelper.floor_double(start.zCoord));

                    if (b0 == 3)
                    {
                        --zz;
                        ++vec32.zCoord;
                    }

                    //Handle block location
                    final BlockPos pos = new BlockPos(xx, yy, zz);
                    if (tileLocations.contains(pos))
                    {
                        //O(2n)
                        tileLocations.remove(pos);
                    }

                    //Only do power hit if we have power, keep pathing to remove tiles behind current
                    if (power > 0)
                    {
                        power -= onPassThroughTile(oldWorld, pos, power, edits, doEdits);
                    }
                }
            }
        }
        return power;
    }

    /**
     * Called when the EMP ray passes through the block
     *
     * @param world
     * @param pos   - position in the world
     * @param power - power passing through the block
     * @param edits - list of edits to add edits to when modifying blocks
     * @return power left after passing through the block
     */
    protected double onPassThroughTile(World world, BlockPos pos, double power, List<IWorldEdit> edits, boolean doEdits)
    {
        Block block = pos.getBlock(world);
        int meta = pos.getBlockMetadata(world);
        if (!canPassThrough(world, pos, block, meta))
        {
            TileEntity tile = pos.getTileEntity(world);
            if (tile != null && !tile.isInvalid())
            {
                return power - handle(tile, pos.distance(this), power, edits, doEdits);
            }

            //https://en.wikipedia.org/wiki/Absorption_(electromagnetic_radiation) TODO figure out values
            Material material = block.getMaterial();
            if (material == Material.iron)
            {
                return 20; //TODO generate shocks if power is very high
            }
            else if (material == Material.rock)
            {
                return 5;
            }
            return 1;
        }
        return 0;
    }

    public static double applyEmpEffect(IInventory inventory, Object host, IVoltageTransmitter source, double power, double distance)
    {
        if (power > 1)
        {
            //TODO split power over items
            //TODO hit held item first
            //TODO hit armor next
            //TODO then hit hot bar followed by inventory
            for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
            {
                boolean held = host instanceof EntityPlayer && slot == ((EntityPlayer) host).inventory.currentItem;
                applyEmpEffect(host, inventory.getStackInSlot(slot), slot, held, source, power, distance);
            }
        }
        return power;
    }

    public static double applyEmpEffect(Object host, ItemStack stack, int slot, boolean held, IVoltageTransmitter source, double power, double distance)
    {
        if (stack != null && stack.getItem() != null)
        {
            Item item = stack.getItem();
            if (item instanceof IEMInterferenceItem)
            {
                return ((IEMInterferenceItem) item).onElectromagneticRadiationApplied(stack, host, slot, held, distance, power, source, true);
            }
            else if (UniversalEnergySystem.isHandler(stack, null))
            {
                UniversalEnergySystem.drain(stack, Integer.MAX_VALUE, true);
                //TODO calculate power usage and drain partial based on input power
                return power * 0.98;
            }
            //TODO handle items that contain items
            //TODO handle remote access points (AE remote, Ender bags)
            //TODO burn out AE drives or randomize items :P
        }
        return power;
    }


    /**
     * Called to see if the ray can pass through the block unaffected
     *
     * @param world
     * @param pos
     * @param block
     * @param meta
     * @return true to ignore the block
     */
    protected boolean canPassThrough(World world, BlockPos pos, Block block, int meta)
    {
        //TODO list of blocks we don't care about (grass, glass, trees, water, etc)
        return !block.isOpaqueCube();
    }

    @Override
    public double getTotalVoltagePower()
    {
        return getPowerForRange(-1);
    }
}
