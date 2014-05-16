package resonant.lib.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import resonant.lib.References;

import com.google.common.io.ByteArrayDataInput;

/** Packet handler for blocks and tile entities.
 * 
 * @author Calclavia */
public class PacketTile extends PacketType
{
    public PacketTile(String channel)
    {
        super(channel);
    }

    public Packet getPacket(TileEntity tileEntity, Object... args)
    {
        return this.getPacket(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, args);
    }

    public Packet getPacketWithID(int id, TileEntity tileEntity, Object... args)
    {
        return this.getPacketWithID(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, id, args);
    }

    public Packet getPacket(int x, int y, int z, Object... args)
    {
        List newArgs = new ArrayList();

        newArgs.add(x);
        newArgs.add(y);
        newArgs.add(z);

        for (Object obj : args)
        {
            newArgs.add(obj);
        }

        return super.getPacket(newArgs.toArray());
    }

    public Packet getPacketWithID(int x, int y, int z, int id, Object... args)
    {
        List newArgs = new ArrayList();

        newArgs.add(x);
        newArgs.add(y);
        newArgs.add(z);
        newArgs.add(id);

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

        try
        {
            if (tileEntity instanceof IPacketReceiverWithID)
            {
                ((IPacketReceiverWithID) tileEntity).onReceivePacket(data.readInt(), data, player);
            }
            else if (tileEntity instanceof IPacketReceiver)
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
        catch (Exception e)
        {
            References.LOGGER.severe("Resonant Engine packet failed at: " + tileEntity + " in " + x + ", " + y + ", " + z);
            e.printStackTrace();
        }
    }
}
