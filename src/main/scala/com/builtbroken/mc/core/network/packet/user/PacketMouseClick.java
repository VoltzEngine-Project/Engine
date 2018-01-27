package com.builtbroken.mc.core.network.packet.user;

import com.builtbroken.mc.api.items.IMouseButtonHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketPlayerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/18/2016.
 */
public class PacketMouseClick extends PacketPlayerItem
{
    public PacketMouseClick()
    {
        //Needed for forge to construct the packet
    }

    public PacketMouseClick(int slotId, int button, boolean state)
    {
        super(slotId);
        addData(button, state); //TODO move to encode and decode
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
        if (stack != null && stack.getItem() instanceof IMouseButtonHandler)
        {
            ((IMouseButtonHandler) stack.getItem()).mouseClick(stack, player, data().readInt(), data().readBoolean());
        }
        else
        {
            Engine.logger().error("Failed to handle mouse click packet due to item in slot " + this.slotId + " is not an instance of ILeftClickBypass");
        }
    }
}
