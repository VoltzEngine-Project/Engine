package com.builtbroken.mc.api.tile;

import com.builtbroken.mc.lib.transform.vector.Location;

/** Used to receive feed back when machines are linked to each other.
 * Called normally in {@link com.builtbroken.mc.api.tile.ILinkable#link(com.builtbroken.mc.lib.transform.vector.Location, short)}
 * Created by robert on 4/16/2015.
 */
public interface ILinkFeedback
{
    /**
     * Called when this tile is linked to another tile
     * @param location - location of the machine this one was linked to
     */
    void onLinked(Location location);
}
