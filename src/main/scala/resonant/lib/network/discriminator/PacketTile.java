package resonant.lib.network.discriminator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import resonant.lib.network.handle.TPacketReceiver;
import universalelectricity.core.transform.vector.Vector3;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketTile extends PacketType
{
	public int x;
	public int y;
	public int z;

	public PacketTile()
	{

	}

	public PacketTile(int x, int y, int z, Object... args)
	{
		super(args);

		this.x = x;
		this.y = y;
		this.z = z;
	}

	public PacketTile(TileEntity tile, Object... args)
	{
		this(tile.xCoord, tile.yCoord, tile.zCoord, args);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeBytes(data());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		data_$eq(buffer.slice());
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		handle(player);
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		handle(player);
	}

	public void handle(EntityPlayer player)
	{
		TileEntity tile = player.getEntityWorld().getTileEntity(this.x, this.y, this.z);

		if (tile instanceof TPacketReceiver)
		{
			try
			{
				TPacketReceiver receiver = (TPacketReceiver) player.getEntityWorld().getTileEntity(this.x, this.y, this.z);
				receiver.read(data().slice(), player, this);
			}
			catch (Exception e)
			{
				System.out.println("Packet sent to a TileEntity failed to be received [" + tile + "] in " + new Vector3(x, y, z));
				e.printStackTrace();
			}
		}
		else
		{
			throw new UnsupportedOperationException("Packet was sent to a tile not implementing IPacketReceiver, this is a coding error [" + tile + "] in " + new Vector3(x, y, z));
		}
	}
}
