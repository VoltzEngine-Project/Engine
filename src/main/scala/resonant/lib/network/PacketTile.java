package resonant.lib.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketTile extends PacketType
{
	protected int x;
	protected int y;
	protected int z;
	protected int id;

	public PacketTile(int x, int y, int z, int id, Object[] args)
	{
		super(args);
		this.x = x;
		this.y = y;
		this.z = y;
		this.id = id;
	}

	public PacketTile(int x, int y, int z, Object[] args)
	{
		this(x, y, z, -1, args);
	}

	public PacketTile(TileEntity tile, Object[] args)
	{
		this(tile.xCoord, tile.yCoord, tile.zCoord, args);
	}

	public PacketTile(TileEntity tile, int id, Object[] args)
	{
		this(tile.xCoord, tile.yCoord, tile.zCoord, id, args);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(id);

		buffer.writeBytes(this.data);

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.id = buffer.readInt();

		this.data = buffer.slice();
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		TileEntity tile = player.getEntityWorld().getTileEntity(this.x, this.y, this.z);
		if (this.id == -1)
		{
			if (tile instanceof IPacketReceiver)
			{
				IPacketReceiver receiver = (IPacketReceiver) player.getEntityWorld().getTileEntity(this.x, this.y, this.z);
				receiver.onReceivePacket(data.slice(), player, this.x, this.y, this.z);
			}
			else
			{
				throw new UnsupportedOperationException("Packet was sent to a tile not implementing IPacketReceiver, this is a coding error");
			}
		}
		else
		{
			if (tile instanceof IPacketReceiverWithID)
			{
				IPacketReceiverWithID receiver = (IPacketReceiverWithID) player.getEntityWorld().getTileEntity(this.x, this.y, this.z);
				receiver.onReceivePacket(this.id, data.slice(), player, this.x, this.y, this.z);
			}
			else
			{
				throw new UnsupportedOperationException("Packet was sent to a tile not implementing IPacketReceiverWithID, this is a coding error");
			}
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		TileEntity tile = player.getEntityWorld().getTileEntity(this.x, this.y, this.z);
		if (this.id == -1)
		{
			if (tile instanceof IPacketReceiver)
			{
				IPacketReceiver receiver = (IPacketReceiver) player.getEntityWorld().getTileEntity(this.x, this.y, this.z);
				receiver.onReceivePacket(data.slice(), player, this.x, this.y, this.z);
			}
			else
			{
				throw new UnsupportedOperationException("Packet was sent to a tile not implementing IPacketReceiver, this is a coding error");
			}
		}
		else
		{
			if (tile instanceof IPacketReceiverWithID)
			{
				IPacketReceiverWithID receiver = (IPacketReceiverWithID) player.getEntityWorld().getTileEntity(this.x, this.y, this.z);
				receiver.onReceivePacket(this.id, data.slice(), player, this.x, this.y, this.z);
			}
			else
			{
				throw new UnsupportedOperationException("Packet was sent to a tile not implementing IPacketReceiverWithID, this is a coding error");
			}
		}
	}
}
