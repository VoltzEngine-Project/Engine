package com.builtbroken.mc.core.network;

import com.builtbroken.mc.core.network.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Darkguardsman on 8/5/2014.
 *
 * @Deprecated - use IPacketReceiver which just passes in the Packet object
 * allowing it to store data on what the packet contains
 */
@Deprecated
public interface IPacketIDReceiver
{
    public boolean read(ByteBuf buf, int id, EntityPlayer player, AbstractPacket type);
}
