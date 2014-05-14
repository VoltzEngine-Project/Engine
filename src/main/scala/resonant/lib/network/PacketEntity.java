package resonant.lib.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;

import com.google.common.io.ByteArrayDataInput;

/** @author Calclavia */
public class PacketEntity extends PacketType
{
    public PacketEntity(String channel)
    {
        super(channel);
    }

    public Packet getPacket(Entity entity, Object... args)
    {
        List newArgs = new ArrayList();

        newArgs.add(entity.entityId);

        for (Object obj : args)
        {
            newArgs.add(obj);
        }

        return super.getPacket(newArgs.toArray());
    }

    @Override
    public void receivePacket(ByteArrayDataInput data, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(data.readInt());

        if (entity instanceof IPacketReceiver)
        {
            ((IPacketReceiver) entity).onReceivePacket(data, player);
        }
    }
}
