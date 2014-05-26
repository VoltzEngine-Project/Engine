package resonant.lib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import resonant.core.ResonantEngine;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketAnnotation extends PacketType
{
	protected int classID;
	protected int packetSetID;

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
					ResonantEngine.INSTANCE.packetHandler.writeData(data, ((TileEntity) obj).xCoord, ((TileEntity) obj).yCoord, ((TileEntity) obj).zCoord);
				}

				ResonantEngine.INSTANCE.packetHandler.writeData(data, packetSet.getPacketArray(obj).toArray());
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
		super.handleClientSide(player);
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		super.handleServerSide(player);
	}
}
