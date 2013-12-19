package calclavia.lib.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

/**
 * @author Calclavia
 */
public class PacketTile extends PacketType
{
	public PacketTile(String channel)
	{
		super(channel);
	}

	public Packet getPacket(TileEntity tileEntity, Object... args)
	{
		List newArgs = new ArrayList();

		newArgs.add(tileEntity.xCoord);
		newArgs.add(tileEntity.yCoord);
		newArgs.add(tileEntity.zCoord);

		for (Object obj : args)
		{
			newArgs.add(obj);
		}

		return super.getPacket(newArgs.toArray());
	}

	@Override
	public void receivePacket(ByteArrayDataInput data, EntityPlayer player)
	{
		TileEntity tileEntity = player.worldObj.getBlockTileEntity(data.readInt(), data.readInt(), data.readInt());

		if (tileEntity instanceof IPacketReceiver)
		{
			((IPacketReceiver) tileEntity).onReceivePacket(data, player);
		}
	}
}
