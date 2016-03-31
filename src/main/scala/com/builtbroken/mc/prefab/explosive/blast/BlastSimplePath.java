package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.api.edit.IWorldChangeLayeredAction;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Very simple version of the the BasicBlast the will
 * path find in all directions. Doesn't do any extra
 * checks beyond distance and can path.
 * Created by robert on 1/28/2015.
 */
public abstract class BlastSimplePath<B extends BlastSimplePath> extends Blast<B> implements IWorldChangeLayeredAction
{
    protected long lastUpdate = -1;
    /**
     * List of locations already check by the pathfinder, used to prevent infinite loops
     */
    protected List<Location> pathed_locations = new ArrayList();
    /**
     * Starting location or center of the blast as a location object
     */
    public Location center;

    /** Set to use recursive pathfinder. */
    public boolean recursive = false;

    protected Queue<Location> stack = new LinkedList();
    protected int layers = 1;
    protected int blocksPerLayer = 500;

    public BlastSimplePath() {}

    public BlastSimplePath(World world, int x, int y, int z, int size)
    {
        super(world, x, y, z, size);
    }

    @Override
    public void getEffectedBlocks(List<IWorldEdit> list)
    {
        Location c = new Location(world(), (int) x(), (int) y(), (int) z());
        center = c.add(0.5);
        if (shouldPath(c))
        {
            if (recursive)
            {
                pathNext(c, list);

            }
            else
            {
                pathEntire(c, list);
            }
        }
        else
        {
            //Temp fix to solve if center is an air block
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            {
                Location location = center.add(dir);
                if (shouldPath(location))
                {
                    if (recursive)
                    {
                        pathNext(location, list);

                    }
                    else
                    {
                        pathEntire(location, list);
                    }
                }
            }
        }
        //TODO find a better way to handle this, for example inside the actual pathfinder loop
        List<IWorldEdit> additions = new ArrayList();
        for(IWorldEdit vec : list)
        {
            //Ensure crops break with farmland to avoid floating crops
            if (vec != null && vec.getBlock() instanceof BlockFarmland)
            {
                Block block = vec.world().getBlock((int) vec.x(), (int) vec.y() + 1, (int) vec.z());
                if (block.canBlockStay(vec.world(), (int) vec.x(), (int) vec.y() + 1, (int) vec.z()) || block instanceof IPlantable || block instanceof IGrowable)
                {
                    BlockEdit edit = new BlockEdit(vec.world(), (int) vec.x(), (int) vec.y() + 1, (int) vec.z());
                    if(!list.contains(edit))
                    {
                        additions.add(edit);
                    }
                }
            }
        }
        list.addAll(additions);
    }

    @Override
    public B setYield(double size)
    {
        double prev = this.size;
        super.setYield(size);
        if (prev != size)
        {
            calculateLayers();
        }
        return (B) this;
    }

    public final void calculateLayers()
    {
        double volume = 4 / 3 * Math.PI * size * size * size;
        int i = (int) (volume / (double) blocksPerLayer);
        if (i > 0)
        {
            layers = i;
        }
        else
        {
            layers = 1;
        }
    }

    @Override
    public int getLayers()
    {
        return layers;
    }

    @Override
    public boolean shouldContinueAction(int layer)
    {
        return layer == 0 || !stack.isEmpty();
    }


    /**
     * Does the entire pathfinder in one go instead of recursing onto itself.
     *
     * @param startNode - starting point
     * @param list      - list of edits
     */
    public void pathEntire(final Location startNode, final List<IWorldEdit> list)
    {
        pathEntire(startNode, list, blocksPerLayer);
    }

    /**
     * Does the entire pathfinder in one go instead of recursing onto itself.
     *
     * @param startNode - starting point
     * @param list      - list of edits
     */
    public void pathEntire(final Location startNode, final List<IWorldEdit> list, final int count)
    {
        if (shouldPath(startNode))
        {
            if (stack.isEmpty())
            {
                //Get first edit
                list.add(changeBlock(startNode));

                //Create stack to store current path nodes
                stack.offer(startNode);
                pathed_locations.add(startNode);
            }

            int currentCount = 0;

            //Loop until we run out of nodes
            boolean shouldExit = false;
            while (!stack.isEmpty() && !shouldExit && currentCount < count)
            {
                shouldExit = shouldKillAction();
                currentCount++;
                if (lastUpdate != -1)
                {
                    long time = System.nanoTime();
                    if (time - lastUpdate > 1e+8)
                    {
                        lastUpdate = time;
                        Engine.instance.logger().info("PathEntireUpdate: " + list.size() + " entries added, " + stack.size() + " in stack, " + pathed_locations.size() + " nodes pathed.");
                    }
                }
                //Pop a node off the stack each iteration
                Location currentNode = stack.poll();

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                {
                    Location nextNode = currentNode.add(dir);

                    //Check if we can path to the node from the current node
                    if (shouldPathTo(currentNode, nextNode))
                    {
                        //Check if we can path the node
                        if (shouldPath(nextNode))
                        {
                            stack.offer(nextNode);

                            //Get Block edit for the location that we can path
                            final IWorldEdit edit = changeBlock(nextNode);

                            //Avoid adding empty edits or existing edits
                            if (edit != null && !list.contains(edit) && edit.hasChanged())
                            {
                                list.add(edit);
                            }
                        }
                        pathed_locations.add(nextNode);
                    }
                }
            }
        }
    }

    /**
     * Called to path the node location in all directions looking
     * for blocks to edit
     *
     * @param node - node, should not be null
     * @param list - list to add edits to, should not be null
     */
    public void pathNext(final Location node, final List<IWorldEdit> list)
    {
        if (!shouldKillAction())
        {
            //Prevent re-adding the same node again
            if (!pathed_locations.contains(node))
            {
                pathed_locations.add(node);
            }

            //Check if we can path the current node
            if (shouldPath(node))
            {
                //Get Block edit for the location that we can path
                final IWorldEdit edit = changeBlock(node);

                //Avoid adding empty edits or existing edits
                if (edit != null && !list.contains(edit) && edit.hasChanged())
                {
                    list.add(edit);
                }

                //Loop over all 6 sides
                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                {
                    //Generated next node
                    final Location next = node.add(dir);
                    //Check if we can path to next node from this node
                    if (shouldPathTo(node, next))
                    {
                        pathNext(next, list);
                    }
                }
            }
        }
    }

    /**
     * Called to see what block the location's block will change to
     *
     * @param location - location to get data from
     * @return null for ignore, or BlockEdit for anything else
     */
    public abstract IWorldEdit changeBlock(Location location);

    /**
     * Called to check if the location should be pathed
     * Can be used for any check including distance, and block infomration
     *
     * @param location - location to check
     * @return true if it can be pathed
     */
    public boolean shouldPath(Location location)
    {
        return center.distance(location.xi() + 0.5, location.yi() + 0.5, location.zi() + 0.5) <= size;
    }

    public boolean shouldPathTo(Location last, Location next)
    {
        return next.y() >= 0 && next.y() <= 255 && !pathed_locations.contains(next);
    }
}
