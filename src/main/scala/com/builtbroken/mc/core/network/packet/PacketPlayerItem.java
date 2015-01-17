package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.core.network.IPacketReceiver;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketPlayerItem extends AbstractPacket
{
	public int slotId;

	public PacketPlayerItem()
	{

	}

	public PacketPlayerItem(int id, int slotId, Object... args)
	{
		super(id, args);
		this.slotId = slotId;
	}

	public PacketPlayerItem(int id, EntityPlayer player, Object... args)
	{
		this(id, player.inventory.currentItem, args);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(slotId);
        super.encodeInto(ctx, buffer);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		slotId = buffer.readInt();
		super.decodeInto(ctx, buffer);
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		ItemStack stack = player.inventory.getStackInSlot(this.slotId);

		if (stack != null && stack.getItem() instanceof IPacketReceiver)
		{
			((IPacketReceiver) stack.getItem()).read(player, this);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		ItemStack stack = player.inventory.getStackInSlot(this.slotId);

		if (stack != null && stack.getItem() instanceof IPacketReceiver)
		{
			((IPacketReceiver) stack.getItem()).read(player, this);
		}
	}
}
