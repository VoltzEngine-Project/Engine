package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.event.blast.BlastEventBlockEdit;
import com.builtbroken.mc.api.event.blast.BlastEventBlockReplaced;
import com.builtbroken.mc.api.event.blast.BlastEventDestroyBlock;
import com.builtbroken.mc.api.explosive.IExplosive;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.helper.MathUtility;
import com.builtbroken.mc.lib.transform.sorting.Vector3DistanceComparator;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import com.builtbroken.mc.prefab.entity.selector.EntityDistanceSelector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Prefab for simple blasts
 * Created by robert on 11/19/2014.
 */
//TODO use pathfinder for emp to allow for EMP shielding
//TODO replace with recursive pathfinder that doesn't keep calling expand, this will stack overflow
//TODO update pathfinding methods to be more reusable
public class BlastBasic<B extends BlastBasic> extends Blast<B>
{
    /**
     * DamageSourse to attack entities with
     */
    static DamageSource source = new DamageSource("blast").setExplosion();
    static BlastProfiler profiler = new BlastProfiler();

    /**
     * Energy to start the explosion with
     */
    protected float energy = 0;
    /**
     * Median size of the explosion from center, max size is x2, min size is 0
     */
    protected double radius = 0;

    /**
     * Entity to pass into methods when destroying blocks or attacking entities
     */
    protected Entity explosionBlameEntity;
    /**
     * Explosion wrapper for block methods
     */
    protected Explosion wrapperExplosion;
    /**
     * Blocks to call after all blocks are removed in case they do updates when destroyed
     */
    protected List<IWorldEdit> postCallDestroyMethod = new ArrayList();
    /**
     * Profilier for the blast
     */
    protected BlastRunProfile profile;

    public BlastBasic(IExplosiveHandler handler)
    {
        super(handler);
        profile = profiler.run(this);
    }


    @Override
    public void getEffectedBlocks(List<IWorldEdit> list)
    {
        //TODO disable profiler if not in debug mode
        HashMap<BlockEdit, Float> map = new HashMap();
        profile.startSection("getEffectedBlocks");

        //Start path finder
        profile.startSection("Pathfinder");
        list.add(new BlockEdit(this, Blocks.air, 0));
        triggerPathFinder(map, new BlockEdit(this.world, this.x, this.y, this.z), energy);
        profile.endSection("Pathfinder");

        //Add map keys to block list
        list.addAll(map.keySet());

        //Sort results so blocks are placed in the center first
        profile.startSection("Sorter");
        Collections.sort(list, new Vector3DistanceComparator(new Pos(x(), y(), z())));
        profile.endSection("Sorter");

        profile.endSection("getEffectedBlocks");
        //Generate debug info
        if (Engine.runningAsDev)
        {
            Engine.instance.logger().info(profile.getOutputSimple());
        }
    }

    /**
     * Called to trigger the blast pathfinder
     *
     * @param map    - hash map to store data for block placement to energy used
     * @param vec    - starting block
     * @param energy - starting energy
     */
    protected void triggerPathFinder(HashMap<BlockEdit, Float> map, BlockEdit vec, float energy)
    {
        //Start pathfinder
        expand(map, vec, energy, null, 0);
    }

    /**
     * Called to map another iteration to expand outwards from the center of the explosion
     *
     * @param map       - hash map to store data for block placement to energy used
     * @param vec       - next block to expand from, and to log to map
     * @param energy    - current energy at block
     * @param side      - side not to expand in, and direction we came from
     * @param iteration - current iteration count from center, use this to stop the iteration if they get too far from center
     */
    protected void expand(HashMap<BlockEdit, Float> map, BlockEdit vec, float energy, EnumFacing side, int iteration)
    {
        long timeStart = System.nanoTime();
        if (iteration < size * 2)
        {
            float e = getEnergyCostOfTile(vec, energy);
            profile.tilesPathed++;
            if (e >= 0)
            {
                //Add block to effect list
                vec.energy = energy;
                onBlockMapped(vec, e, energy - e);
                map.put(vec, energy - e);

                //Only iterate threw sides if we have more energy
                if (e > 1)
                {
                    //Get valid sides to iterate threw
                    List<BlockEdit> sides = new ArrayList();
                    for (EnumFacing dir : EnumFacing.values())
                    {
                        if (dir != side)
                        {
                            BlockEdit v = new BlockEdit(world, vec.x(), vec.y(), vec.z());
                            v.doDrops();
                            v = v.add(dir);
                            v.face = dir;
                            v.logPrevBlock();
                            sides.add(v);
                        }
                    }

                    Collections.sort(sides, new Vector3DistanceComparator(new Pos(x(), y(), z())));

                    profile.blockIterationTimes.add(System.nanoTime() - timeStart);
                    //Iterate threw sides expending energy outwards
                    for (BlockEdit f : sides)
                    {
                        float eToSpend = (e / sides.size()) + (e % sides.size());
                        e -= eToSpend;
                        EnumFacing face = side == null ? getOpposite(f.face) : side;
                        if (!map.containsKey(f) || map.get(f) < eToSpend)
                        {
                            f.face = face;
                            expand(map, f, eToSpend, face, iteration + 1);
                        }
                    }
                }
            }
        }
    }

    //TODO move to helper class later, and PR into forge if its not already there
    private EnumFacing getOpposite(EnumFacing face)
    {
        switch (face)
        {
            case UP:
                return EnumFacing.DOWN;
            case DOWN:
                return EnumFacing.UP;
            case NORTH:
                return EnumFacing.SOUTH;
            case SOUTH:
                return EnumFacing.NORTH;
            case EAST:
                return EnumFacing.WEST;
            case WEST:
                return EnumFacing.EAST;
            default:
                return null;
        }
    }

    /**
     * Called to see how much energy is lost effecting the block at the location
     *
     * @param vec    - location
     * @param energy - energy to expend on the location
     * @return energy left over after effecting the block
     */
    protected float getEnergyCostOfTile(BlockEdit vec, float energy)
    {
        //Update debug info
        if (vec.isAirBlock(world))
        {
            profile.airBlocksPathed++;
        }
        else
        {
            profile.blocksRemoved++;
        }
        //Get cost
        return (vec.getHardness() >= 0 ? energy - (float) Math.max(vec.getResistance(explosionBlameEntity, x, y, z), 0.5) : -1);

    }

    @Override
    public void handleBlockPlacement(IWorldEdit vec)
    {
        if (vec != null && vec.hasChanged() && prePlace(vec))
        {
            final Block block = vec.getBlock();
            //TODO add energy value of explosion to this explosion if it is small
            //TODO maybe trigger explosion inside this thread allowing for controlled over lap
            //TODO if we trigger the explosive move most of the energy in the same direction
            //the current explosion is running in with a little bit in the opposite direction

            //TODO check that the block was destroyed (If not modify events fired)
            //TODO add a damage event for blocks changed instead of destroyed
            //Trigger break event so blocks can do X action
            if (!(block instanceof BlockTNT) && !(vec.getTileEntity() instanceof IExplosive))
            {
                block.onBlockDestroyedByExplosion(world, (int) vec.x(), (int) vec.y(), (int) vec.z(), wrapperExplosion);
            }
            else
            {
                //Add explosives to post call to allow the thread to finish before generating more explosions
                postCallDestroyMethod.add(vec);
            }

            vec.place();
            postPlace(vec);
        }
    }

    /**
     * Called to give the blast a chance to override what
     * the block at the location will turn into
     *
     * @param change         - location and placement data
     *                       change.setBlock(Block)
     *                       change.setMeta(meta)
     *                       to update the placement info
     * @param energyExpended - energy expended on the block to change it
     * @return new placement info, never change the location or you will
     * create a duplication issue as the original block will not be removed
     */
    protected BlockEdit onBlockMapped(BlockEdit change, float energyExpended, float energyLeft)
    {
        if (energyExpended > energyLeft)
        {
            change.doItemDrop = true;
        }
        return change;
    }

    @Override
    public void doEffectOther(boolean beforeBlocksPlaced)
    {
        if (!beforeBlocksPlaced)
        {
            //TODO wright own version of getEntitiesWithinAABB that takes a filter and cuboid(or Vector3 to Vector3)
            //TODO ensure that the entity is in line of sight
            //TODO ensure that the entity can be pathed by the explosive
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x - size - 1, y - size - 1, z - size - 1, x + size + 1, y + size + 1, z + size + 1);
            List list = world.selectEntitiesWithinAABB(Entity.class, bounds, new EntityDistanceSelector(new Pos(x, y, z), size + 1, true));
            if (list != null && !list.isEmpty())
            {
                damageEntities(list, source);
            }
        }
    }

    @Override
    public B setCause(TriggerCause cause)
    {
        super.setCause(cause);
        //Create entity to check for blast resistance values on blocks
        if (cause instanceof TriggerCause.TriggerCauseEntity)
        {
            explosionBlameEntity = ((TriggerCause.TriggerCauseEntity) cause).source;
        }
        if (explosionBlameEntity == null)
        {
            explosionBlameEntity = new EntityTNTPrimed(world);
            explosionBlameEntity.setPosition(x, y, z);
        }
        wrapperExplosion = new WrapperExplosion(this);
        return (B) this;
    }


    @Override
    public B setYield(double size)
    {
        super.setYield(size);
        //Most of the time radius equals size of the explosion
        radius = size;
        calcStartingEnergy();
        return (B) this;
    }

    /**
     * Calculates the starting energy based on the size of the explosion
     */
    protected void calcStartingEnergy()
    {
        energy = (float) (MathUtility.getSphereVolume(radius) * eUnitPerBlock);
    }

    @Override
    protected void postPlace(final IWorldEdit vec)
    {
        MinecraftForge.EVENT_BUS.post(new BlastEventDestroyBlock.Post(this, BlastEventDestroyBlock.DestructionType.FORCE, world, vec.getBlock(), vec.getBlockMetadata(), (int) vec.x(), (int) vec.y(), (int) vec.z()));
    }

    @Override
    protected boolean prePlace(final IWorldEdit vec)
    {
        BlastEventBlockEdit event = new BlastEventDestroyBlock.Pre(this, BlastEventDestroyBlock.DestructionType.FORCE, world, vec.getBlock(), vec.getBlockMetadata(), (int) vec.x(), (int) vec.y(), (int) vec.z());

        boolean result = MinecraftForge.EVENT_BUS.post(event);
        if (vec instanceof BlockEdit && event instanceof BlastEventBlockReplaced.Pre)
        {
            ((BlockEdit) vec).set(((BlastEventBlockReplaced.Pre) event).newBlock, ((BlastEventBlockReplaced.Pre) event).newMeta);
        }
        return !result;
    }

    /**
     * Used to wrapper the blast into a minecraft explosion data object
     */
    public static class WrapperExplosion extends Explosion
    {
        public final BlastBasic blast;

        public WrapperExplosion(BlastBasic blast)
        {
            super(blast.world(), blast.explosionBlameEntity, blast.x(), blast.y(), blast.z(), (float) blast.size);
            this.blast = blast;
        }
    }
}
