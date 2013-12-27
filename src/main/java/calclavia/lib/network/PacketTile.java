package calclavia.lib.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

/**
 * Packet handler for blocks and tile entities.
 * 
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
		int x = data.readInt();
		int y = data.readInt();
		int z = data.readInt();
		TileEntity tileEntity = player.worldObj.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IPacketReceiver)
		{
			((IPacketReceiver) tileEntity).onReceivePacket(data, player);
		}
		else
		{
			int blockID = player.worldObj.getBlockId(x, y, z);

			if (Block.blocksList[blockID] instanceof IPacketReceiver)
			{
				((IPacketReceiver) Block.blocksList[blockID]).onReceivePacket(data, player, x, y, z);
			}
		}
	}
}
