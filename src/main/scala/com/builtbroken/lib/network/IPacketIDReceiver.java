package com.builtbroken.lib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import com.builtbroken.lib.network.packet.PacketType;

/**
 * Created by Darkguardsman on 8/5/2014.
 */
public interface IPacketIDReceiver
{
	public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type);
}
