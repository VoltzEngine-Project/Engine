package com.builtbroken.mc.core.network.ex;

import com.builtbroken.mc.lib.transform.vector.Location;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2015.
 */
public class PacketIDException extends PacketTileReadException
{
    public PacketIDException(Location location)
    {
        super(location, "Packet provided an invalid or missing ID.");
    }
}
