package resonant.lib.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import resonant.engine.References;
import resonant.engine.ResonantEngine;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketAnnotation extends PacketType
{
	protected int classID;
	protected int packetSetID;

	public PacketAnnotation()
	{

	}

	public PacketAnnotation(Object obj)
	{
		this(obj, 0);
	}

	public PacketAnnotation(Object obj, int packetSetID)
	{
		PacketAnnotationManager.INSTANCE.constructPacketSets(obj.getClass());

		int classID = PacketAnnotationManager.INSTANCE.classPacketIDMap.get(obj.getClass());

		if (PacketAnnotationManager.INSTANCE.packetSetIDMap.size() > classID)
		{
			PacketAnnotationManager.PacketSet packetSet = PacketAnnotationManager.INSTANCE.packetSetIDMap.get(classID).get(packetSetID);

			if (packetSet != null)
			{
				this.classID = classID;
				this.packetSetID = packetSetID;

				if (obj instanceof TileEntity)
				{
					ResonantEngine.instance.packetHandler.writeData(data, ((TileEntity) obj).xCoord, ((TileEntity) obj).yCoord, ((TileEntity) obj).zCoord);
				}

				data.writeBytes(packetSet.getPacketArrayData(obj));
			}
		}

	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(this.classID);
		buffer.writeInt(this.packetSetID);
		buffer.writeBytes(this.data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.classID = buffer.readInt();
		this.packetSetID = buffer.readInt();
		this.data = buffer.slice();
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		try
		{
			if (TileEntity.class.isAssignableFrom(PacketAnnotationManager.INSTANCE.classPacketIDMap.inverse().get(this.classID)))
			{
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();

				TileEntity tile = player.getEntityWorld().getTileEntity(x, y, z);

				if (tile != null)
				{
					PacketAnnotationManager.INSTANCE.packetSetIDMap.get(this.classID).get(this.packetSetID).read(tile, data.slice());

					if (tile instanceof IPacketReceiver)
					{
						((IPacketReceiver) tile).onReceivePacket(data, player, x, y, z);
					}
				}
				else
				{
					References.LOGGER.error("Sent Annotation packet to null Tile: " + x + " : " + y + " : " + z);
				}
			}
		}
		catch (Exception e)
		{
			References.LOGGER.fatal("Failed to read Annotation Packet", e);
			throw new UnsupportedOperationException(e);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		try
		{
			if (TileEntity.class.isAssignableFrom(PacketAnnotationManager.INSTANCE.classPacketIDMap.inverse().get(this.classID)))
			{
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();

				TileEntity tile = player.getEntityWorld().getTileEntity(x, y, z);

				if (tile != null)
				{
					PacketAnnotationManager.INSTANCE.packetSetIDMap.get(this.classID).get(this.packetSetID).read(tile, data.slice());

					if (tile instanceof IPacketReceiver)
					{
						((IPacketReceiver) tile).onReceivePacket(data, player, x, y, z);
					}
				}
				else
				{
					References.LOGGER.error("Sent Annotation packet to null Tile: " + x + " : " + y + " : " + z);
				}
			}
		}
		catch (Exception e)
		{
			References.LOGGER.fatal("Failed to read Annotation Packet", e);
			throw new UnsupportedOperationException(e);
		}
	}
}
