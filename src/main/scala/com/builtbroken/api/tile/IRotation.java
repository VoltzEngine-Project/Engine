package com.builtbroken.api.tile;

import net.minecraftforge.common.util.ForgeDirection;

/** Used by blocks that have a placement direction in the world
 *
 * Created by robert on 12/9/2014.
 */
public interface IRotation
{
    /** Gets the facing direction of the TileEntity
     * @return  Front of the tile */
    public ForgeDirection getDirection();
}
