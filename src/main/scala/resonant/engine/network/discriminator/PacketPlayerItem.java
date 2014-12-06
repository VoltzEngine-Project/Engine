package resonant.engine.network.discriminator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import resonant.engine.network.handle.TPacketReceiver;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketPlayerItem extends PacketType
{
	public int slotId;

	public PacketPlayerItem()
	{

	}

	public PacketPlayerItem(int slotId, Object... args)
	{
		super(args);
		this.slotId = slotId;
	}

	public PacketPlayerItem(EntityPlayer player, Object... args)
	{
		this(player.inventory.currentItem, args);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(slotId);
		buffer.writeBytes(data());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		slotId = buffer.readInt();
		data_$eq(buffer.slice());
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		ItemStack stack = player.inventory.getStackInSlot(this.slotId);

		if (stack != null && stack.getItem() instanceof TPacketReceiver)
		{
			((TPacketReceiver) stack.getItem()).read(data(), player, this);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		ItemStack stack = player.inventory.getStackInSlot(this.slotId);

		if (stack != null && stack.getItem() instanceof TPacketReceiver)
		{
			((TPacketReceiver) stack.getItem()).read(data(), player, this);
		}
	}
}
