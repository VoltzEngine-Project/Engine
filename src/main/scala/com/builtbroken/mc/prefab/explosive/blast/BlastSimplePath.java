package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 1/28/2015.
 */
public abstract class BlastSimplePath extends Blast
{
    private List<Location> pathed = new ArrayList();
    private Location center;

    @Override
    public void getEffectedBlocks(List<BlockEdit> list)
    {
        center = new Location(world(), x(), y(), z());
        //TODO get side of explosive to restrict directions that can be accessed.
        //TODO This way we avoid passing threw walls if the source block was not converted.
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            //Center point will be looped back over in the first path call
            path(center.add(dir), list);
        }
    }

    public void path(Location location, List<BlockEdit> list)
    {
        if (location.distance(center) <= size)
        {
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            {
                Location loc = location.add(dir);
                if (shouldPath(location))
                {
                    pathed.add(location);
                    BlockEdit edit = changeBlock(loc);
                    if (edit == null || edit.hasChanged())
                    {
                        if (edit != null)
                            list.add(edit);
                        path(location.add(dir), list);
                    }
                }
            }
        }
    }

    public abstract BlockEdit changeBlock(Location location);

    public boolean shouldPath(Location location)
    {
        return !pathed.contains(location);
    }
}
