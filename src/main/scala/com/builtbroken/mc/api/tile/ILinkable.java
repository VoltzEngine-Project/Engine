package com.builtbroken.mc.api.tile;

import com.builtbroken.mc.lib.transform.vector.Location;

/** Used to allow interfacing with tiles to link machines to them
 *
 * Created by robert on 4/16/2015.
 */
public interface ILinkable
{
    /**
     * Called, normally by a hand tool, to link a machine to the selected tile
     * @param pos - location of the other machine
     * @param pass - pass code, if the machine had one, zero == not used
     * @return translation key of the response to linking, if it starts with error
     * it will not link
     */
    public String link(Location pos, short pass);
}
