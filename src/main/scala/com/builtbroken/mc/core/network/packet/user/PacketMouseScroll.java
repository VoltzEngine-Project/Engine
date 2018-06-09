package com.builtbroken.mc.core.network.packet.user;

import com.builtbroken.mc.api.data.IPacket;
import com.builtbroken.mc.api.items.tools.IItemMouseScroll;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/29/2018.
 */
public class PacketMouseScroll implements IPacket
{
    int slot;
    boolean ctrl;
    boolean shift;
    boolean forward;

    public PacketMouseScroll()
    {
        //empty for packet system
    }

    public PacketMouseScroll(int slot, boolean ctrl, boolean shift, boolean forward)
    {
        this.slot = slot;
        this.ctrl = ctrl;
        this.shift = shift;
        this.forward = forward;
    }


    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(slot);
        buffer.writeBoolean(forward);
        buffer.writeBoolean(shift);
        buffer.writeBoolean(ctrl);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        slot = buffer.readInt();
        forward = buffer.readBoolean();
        shift = buffer.readBoolean();
        ctrl = buffer.readBoolean();
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (stack != null && stack.getItem() instanceof IItemMouseScroll) //TODO add interface when more than wrench use
        {
            ((IItemMouseScroll) stack.getItem()).onMouseWheelScrolled(player, stack, ctrl, shift, forward);
        }
    }
}
