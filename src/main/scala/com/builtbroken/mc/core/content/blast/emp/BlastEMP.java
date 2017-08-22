package com.builtbroken.mc.core.content.blast.emp;

import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.energy.IEMReceptiveDevice;
import com.builtbroken.mc.api.energy.IVoltageTransmitter;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.framework.energy.UniversalEnergySystem;
import com.builtbroken.mc.framework.explosive.blast.Blast;
import com.builtbroken.mc.imp.transform.sorting.Vector3DistanceComparator;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.world.map.TileMapRegistry;
import com.builtbroken.mc.lib.world.map.radar.RadarMap;
import com.builtbroken.mc.lib.world.map.radar.data.RadarObject;
import com.builtbroken.mc.lib.world.map.radar.data.RadarTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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


    public BlastEMP(IExplosiveHandler handler)
    {
        super(handler);
    }

    @Override
    public void getEffectedBlocks(final List<IWorldEdit> edits)
    {
        RadarMap map = TileMapRegistry.getRadarMapForWorld(oldWorld);
        if (map != null)
        {
            LinkedList<Pos> tileLocations = new LinkedList();

            //Find all valid tiles
            List<RadarObject> objects = map.getRadarObjects(x(), z(), size);
            for (RadarObject object : objects)
            {
                if (object instanceof RadarTile)
                {
                    TileEntity tile = ((RadarTile) object).tile;
                    if (!tile.isInvalid() && (tile instanceof IEMReceptiveDevice || UniversalEnergySystem.isHandler(tile, null)))
                    {
                        tileLocations.add(new Pos(tile));
                    }
                }
            }

            //Sort based on distance, largest first
            Collections.sort(tileLocations, new Vector3DistanceComparator(this, false));

            //Loop valid tiles
            while (!tileLocations.isEmpty())
            {
                Pos pos = tileLocations.poll();
                TileEntity tile = pos.getTileEntity(oldWorld);

                if (tile != null && !tile.isInvalid())
                {
                    double distance = new Pos(tile).add(0.5).distance(this);
                    if (distance < getEMPRange())
                    {
                        handle(tile, distance, getPower(distance), tileLocations, edits);
                    }
                }
            }
        }
    }

    protected double handle(TileEntity tile, double distance, double power, LinkedList<Pos> tileLocations, List<IWorldEdit> edits)
    {
        power = rayTraceBlocks(toPos().toVec3d(), new Vec3d(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5), power, tileLocations, edits);
        if (power > 1)
        {
            IWorldEdit edit = null;
            double powerUsed = 0;
            if (tile instanceof IEMReceptiveDevice)
            {
                powerUsed = ((IEMReceptiveDevice) tile).onElectromagneticRadiationApplied(this, distance, power, false);
                edit = doEMP((IEMReceptiveDevice) tile, distance, power);
            }
            else if (UniversalEnergySystem.isHandler(tile, null))
            {
                edit = drainEnergy(tile, distance, power);
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

    protected IWorldEdit doEMP(IEMReceptiveDevice tile, double distance, double power)
    {
        System.out.println("EMP tile effect: " + tile + " " + distance + " " + power);
        return new EmpEdit(new Location((TileEntity) tile), this, distance, power);
    }

    protected IWorldEdit drainEnergy(TileEntity tile, double distance, double power)
    {
        System.out.println("EMP tile drain: " + tile + " " + distance + " " + power);
        return new EmpDrainEdit(new Location(tile), this, distance, power);
    }

    protected double getEMPRange()
    {
        return size;
    }

    protected double getPower(double distance)
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
            //oldWorld.playSoundEffect(x, y, z, START_AUDIO, 0.2F + oldWorld.rand.nextFloat() * 0.2F, 0.9F + oldWorld.rand.nextFloat() * 0.15F);
        }
    }

    @Override
    public void doEndAudio()
    {
        if (!oldWorld.isRemote)
        {
            //oldWorld.playSoundEffect(x, y, z, END_AUDIO, 0.2F + oldWorld.rand.nextFloat() * 0.2F, 0.9F + oldWorld.rand.nextFloat() * 0.15F);
        }
    }

    @Override
    public void playAudioForEdit(IWorldEdit blocks)
    {
        if (!oldWorld.isRemote)
        {
            //oldWorld.playSoundEffect(blocks.x(), blocks.y(), blocks.z(), EDIT_AUDIO, 0.2F + oldWorld.rand.nextFloat() * 0.2F, 0.9F + oldWorld.rand.nextFloat() * 0.15F);
        }
    }

    public double rayTraceBlocks(Vec3d s, Vec3d end, double power, LinkedList<Pos> tileLocations, List<IWorldEdit> edits)
    {
        double start_x = s.x;
        double start_y = s.y;
        double start_z = s.z;
        if (!Double.isNaN(start_x) && !Double.isNaN(start_y) && !Double.isNaN(start_z))
        {
            if (!Double.isNaN(end.x) && !Double.isNaN(end.y) && !Double.isNaN(end.z))
            {
                int end_x = MathHelper.floor(end.x);
                int end_y = MathHelper.floor(end.y);
                int end_z = MathHelper.floor(end.z);

                int xx = MathHelper.floor(start_x);
                int yy = MathHelper.floor(start_y);
                int zz = MathHelper.floor(start_z);

                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(start_x) || Double.isNaN(start_y) || Double.isNaN(start_z))
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
                    double d6 = end.x - start_x;
                    double d7 = end.y - start_y;
                    double d8 = end.z - start_z;

                    //Normalization
                    if (flag6)
                    {
                        d3 = (d0 - start_x) / d6;
                    }

                    if (flag3)
                    {
                        d4 = (d1 - start_y) / d7;
                    }

                    if (flag4)
                    {
                        d5 = (d2 - start_z) / d8;
                    }

                    boolean flag5 = false;
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

                        start_x = d0;
                        start_y += d7 * d3;
                        start_z += d8 * d3;
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

                        start_x += d6 * d4;
                        start_y = d1;
                        start_z += d8 * d4;
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

                        start_x += d6 * d5;
                        start_y += d7 * d5;
                        start_z = d2;
                    }

                    xx = MathHelper.floor(start_x);

                    if (b0 == 5)
                    {
                        --xx;
                    }

                    yy = MathHelper.floor(start_y);

                    if (b0 == 1)
                    {
                        --yy;
                    }

                    zz = MathHelper.floor(start_z);

                    if (b0 == 3)
                    {
                        --zz;
                    }

                    //Handle block location
                    final Pos pos = new Pos(xx, yy, zz);
                    if (tileLocations.contains(pos))
                    {
                        //O(2n)
                        tileLocations.remove(pos);
                    }

                    //Only do power hit if we have power, keep pathing to remove tiles behind current
                    if (power > 0)
                    {
                        power -= onPassThroughTile(oldWorld, pos, power, tileLocations, edits);
                    }
                }
            }
        }
        return power;
    }

    protected double onPassThroughTile(World world, Pos pos, double power, LinkedList<Pos> tileLocations, List<IWorldEdit> edits)
    {
        IBlockState block = pos.getBlockState(world);
        if (!canPassThrough(world, pos, block))
        {
            TileEntity tile = pos.getTileEntity(world);
            if (tile != null && !tile.isInvalid())
            {
                return power - handle(tile, pos.distance(this), power, tileLocations, edits);
            }

            //https://en.wikipedia.org/wiki/Absorption_(electromagnetic_radiation) TODO figure out values
            Material material = block.getMaterial();
            if (material == Material.IRON)
            {
                return 20; //TODO generate shocks if power is very high
            }
            else if (material == Material.ROCK)
            {
                return 5;
            }
            return 1;
        }
        return 0;
    }

    protected boolean canPassThrough(World world, Pos pos, IBlockState state)
    {
        //TODO list of blocks we don't care about (grass, glass, trees, water, etc)
        return !state.isOpaqueCube();
    }

    @Override
    public double getTotalVoltagePower()
    {
        return getPower(-1);
    }
}
