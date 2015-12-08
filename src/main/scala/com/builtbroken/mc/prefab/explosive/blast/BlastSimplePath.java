package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Very simple version of the the BasicBlast the will
 * path find in all directions. Doesn't do any extra
 * checks beyond distance and can path.
 * Created by robert on 1/28/2015.
 */
public abstract class BlastSimplePath extends Blast
{
    /**
     * List of locations already check by the pathfinder, used to prevent infinite loops
     */
    protected List<Location> pathed_locations = new ArrayList();
    /**
     * Starting location or center of the blast as a location object
     */
    protected Location center;

    @Override
    public void getEffectedBlocks(List<IWorldEdit> list)
    {
        center = new Location(world(), x(), y(), z());
        //Temp fix to solve if center is an air block
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            path(center.add(dir), list);
        }
    }

    /**
     * Called to path the node location in all directions looking
     * for blocks to edit
     *
     * @param node - node, should not be null
     * @param list - list to add edits to, should not be null
     */
    public void path(final Location node, final List<IWorldEdit> list)
    {
        pathed_locations.add(node);
        if (shouldPath(node))
        {
            final BlockEdit edit = changeBlock(node);
            if (edit != null && !list.contains(edit) && edit.hasChanged())
                list.add(edit);

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            {
                final Location next = node.add(dir);
                if (shouldPathTo(node, next))
                {
                    path(next, list);
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
        return location.distance(center) <= size;
    }

    public boolean shouldPathTo(Location last, Location next)
    {
        return !pathed_locations.contains(next);
    }
}
