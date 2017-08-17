package com.builtbroken.mc.core.network.packet.user;

import com.builtbroken.mc.api.items.tools.IModeItem;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketPlayerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/** Version of the item packet designed just to set the mode
 * of an item
 * Created by robert on 2/14/2015.
 */
public class PacketPlayerItemMode extends PacketPlayerItem
{
    public PacketPlayerItemMode()
    {
        //Needed for forge to construct the packet
    }

    public PacketPlayerItemMode(int slotId, int mode)
    {
        super(slotId, mode);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        handleServerSide(player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        ItemStack stack = player.inventory.getStackInSlot(this.slotId);
        if(stack != null && stack.getItem() instanceof IModeItem)
        {
            ((IModeItem) stack.getItem()).setMode(stack, data().readInt());
        }
        else
        {
            Engine.logger().error("Failed to handle setMode packet due to item in slot " + this.slotId + " is not an instance of IModeItem");
        }
    }
}
