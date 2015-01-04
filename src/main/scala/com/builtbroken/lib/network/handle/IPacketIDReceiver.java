package com.builtbroken.lib.network.handle;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import com.builtbroken.lib.network.discriminator.PacketType;

/**
 * Created by Darkguardsman on 8/5/2014.
 */
public interface IPacketIDReceiver
{
	public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type);
}
