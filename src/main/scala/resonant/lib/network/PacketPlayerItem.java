package resonant.lib.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;

import com.google.common.io.ByteArrayDataInput;

/** @author Calclavi */
public class PacketPlayerItem extends PacketType
{
    public PacketPlayerItem(String channel)
    {
        super(channel);
    }

    public Packet getPacket(int slotID, Object... args)
    {
        List newArgs = new ArrayList();

        newArgs.add(slotID);

        for (Object obj : args)
        {
            newArgs.add(obj);
        }

        return super.getPacket(newArgs.toArray());
    }

    public Packet getPacket(EntityPlayer player, Object... args)
    {
        return this.getPacket(player.inventory.currentItem, args);
    }

    @Override
    public void receivePacket(ByteArrayDataInput data, EntityPlayer player)
    {
        ItemStack itemStack = player.inventory.getStackInSlot(data.readInt());

        if (itemStack != null && itemStack.getItem() instanceof IPacketReceiver)
        {
            ((IPacketReceiver) itemStack.getItem()).onReceivePacket(data, player, itemStack);
        }
    }
}
