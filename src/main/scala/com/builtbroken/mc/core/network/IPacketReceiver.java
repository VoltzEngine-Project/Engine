package com.builtbroken.mc.core.network;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.core.network.packet.PacketType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Applied to tiles that read packets
 * Created by robert on 8/5/2014.
 */
@Deprecated //Just use the ID version
public interface IPacketReceiver
{
    /**
     * Reads a packet
     *
     * @param buf    - data encoded into the packet
     * @param player - player that is receiving the packet
     * @param packet - The packet instance that was sending this packet.
     */
    void read(ByteBuf buf, EntityPlayer player, PacketType packet);

    /**
     * Called to check if the packet should be read at all.
     * <p>
     * Use this to validate packet data to prevent users with hacked clients from corrupting data.
     *
     * @param player          - player who sent the packet
     * @param receiveLocation - location the packet should be read at, will return player location for
     *                        world packets without postion data, and null if non-world packets
     * @return true if the packet should be read
     */
    default boolean shouldReadPacket(EntityPlayer player, IWorldPosition receiveLocation, PacketType packet)
    {
        return true;
    }
}
