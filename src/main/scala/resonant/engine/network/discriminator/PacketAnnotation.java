package resonant.engine.network.discriminator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import resonant.engine.References;
import resonant.engine.ResonantEngine;
import resonant.engine.network.handle.TPacketReceiver;

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
					ResonantEngine.instance.packetHandler.writeData(data(), ((TileEntity) obj).xCoord, ((TileEntity) obj).yCoord, ((TileEntity) obj).zCoord);
				}

				data().writeBytes(packetSet.getPacketArrayData(obj));
			}
		}

	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(this.classID);
		buffer.writeInt(this.packetSetID);
		buffer.writeBytes(this.data());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.classID = buffer.readInt();
		this.packetSetID = buffer.readInt();
		this.data_$eq(buffer.slice());
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		try
		{
			// Get class for our id and check for null
			Class clazz = PacketAnnotationManager.INSTANCE.getClassForId(classID);
			if (clazz != null)
			{
				// TODO support other class types
				if (TileEntity.class.isAssignableFrom(clazz))
				{
					int x = data().readInt();
					int y = data().readInt();
					int z = data().readInt();

					TileEntity tile = player.getEntityWorld().getTileEntity(x, y, z);

					if (tile != null)
					{
						// If its a TPacketReceiver let it handle the packet
						if (tile instanceof TPacketReceiver)
						{
							((TPacketReceiver) tile).read(data(), player, this);
						}
						//Else assign values with reflection
						else
						{
							PacketAnnotationManager.INSTANCE.getSet(clazz, packetSetID).read(tile, data().slice());
						}
					}
					else
					{
						References.LOGGER.error("Sent Annotation packet to null Tile: " + x + " : " + y + " : " + z);
					}
				}
				else
				{
					References.LOGGER.fatal("PacketAnnotation: Unsupported class type " + clazz);
				}
			}
			else
			{
				References.LOGGER.fatal("PacketAnnotation: Unknown classID " + this.classID);
			}
		}// TODO replace catch with a set area of catches to allow for crashing on bad crashes
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
				int x = data().readInt();
				int y = data().readInt();
				int z = data().readInt();

				TileEntity tile = player.getEntityWorld().getTileEntity(x, y, z);

				if (tile != null)
				{
					PacketAnnotationManager.INSTANCE.packetSetIDMap.get(this.classID).get(this.packetSetID).read(tile, data().slice());

					if (tile instanceof TPacketReceiver)
					{
						((TPacketReceiver) tile).read(data(), player, this);
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
