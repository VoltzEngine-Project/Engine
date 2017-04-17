package com.builtbroken.mc.framework.logic.imp;

import com.builtbroken.mc.api.data.IPacket;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketType;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/30/2017.
 */
public interface ITileDesc extends IPacketIDReceiver
{
    IPacket getDescPacket();

    default boolean canHandlePackets()
    {
        return true;
    }
}
