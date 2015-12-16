package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import net.minecraft.world.World;
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
public abstract class BlastSimplePath extends Blast
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

    public BlastSimplePath() {}

    public BlastSimplePath(World world, int x, int y, int z, int size)
    {
        super(world, x, y, z, size);
    }

    @Override
    public void getEffectedBlocks(List<IWorldEdit> list)
    {
        pathed_locations.clear();
        center = new Location(world(), (int) x() + 0.5, (int) y() + 0.5, (int) z() + 0.5);
        //Temp fix to solve if center is an air block
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            if (recursive)
            {
                pathNext(center.add(dir), list);
            }
            else
            {
                pathEntire(center.add(dir), list);
            }
        }
    }

    /**
     * Does the entire pathfinder in one go instead of recursing onto itself.
     *
     * @param startNode - starting point
     * @param list      - list of edits
     */
    public void pathEntire(final Location startNode, final List<IWorldEdit> list)
    {
        if (shouldPath(startNode))
        {
            //Get first edit
            list.add(changeBlock(startNode));

            //Create stack to store current path nodes
            Queue<Location> stack = new LinkedList();
            stack.offer(startNode);
            pathed_locations.add(startNode);

            //Loop until we run out of nodes
            while (!stack.isEmpty())
            {
                if(lastUpdate != -1)
                {
                    long time = System.nanoTime();
                    if(time - lastUpdate > 1e+8)
                    {
                        lastUpdate = time;
                        Engine.instance.logger().info("PathEntireUpdate: " + list.size() + " entries added, " + stack.size() + " in stack");
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
                            final BlockEdit edit = changeBlock(nextNode);

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
        //Prevent re-adding the same node again
        if (!pathed_locations.contains(node))
        {
            pathed_locations.add(node);
        }

        //Check if we can path the current node
        if (shouldPath(node))
        {
            //Get Block edit for the location that we can path
            final BlockEdit edit = changeBlock(node);

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

    /**
     * Called to see what block the location's block will change to
     *
     * @param location - location to get data from
     * @return null for ignore, or BlockEdit for anything else
     */
    public abstract BlockEdit changeBlock(Location location);

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
