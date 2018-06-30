package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.IPacketReceiver;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketPlayerItem extends PacketType
{
    public int slotId;

    public PacketPlayerItem()
    {
        //Needed for forge to construct the packet
    }

    public PacketPlayerItem(int slotId, Object... args)
    {
        add(this.slotId = slotId);
        add(args);
    }

    public PacketPlayerItem(EntityPlayer player, Object... args)
    {
        this(player.inventory.currentItem, args);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        super.decodeInto(ctx, buffer);
        slotId = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        handleServerSide(player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        if (slotId < 0)
        {
            final Item item = Item.getItemById(Math.abs(this.slotId));
            if (item != null)
            {
                if (item instanceof IPacketReceiver)
                {
                    ((IPacketReceiver) item).read(getDataToRead(), player, this);
                }
                else if (item instanceof IPacketIDReceiver)
                {
                    ((IPacketIDReceiver) item).read(getDataToRead(), getDataToRead().readInt(), player, this);
                }
            }
        }
        else
        {
            final ItemStack stack = player.inventory.getStackInSlot(this.slotId);
            if (stack != null)
            {
                if (stack.getItem() instanceof IPacketReceiver)
                {
                    ((IPacketReceiver) stack.getItem()).read(getDataToRead(), player, this);
                }
                else if (stack.getItem() instanceof IPacketIDReceiver)
                {
                    ((IPacketIDReceiver) stack.getItem()).read(getDataToRead(), getDataToRead().readInt(), player, this);
                }
            }
        }
    }
}
