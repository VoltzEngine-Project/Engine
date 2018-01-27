package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.prefab.PacketBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketEntity extends PacketBase<PacketEntity>
{
    protected int entityId;
    protected int id;

    public PacketEntity()
    {
        //Needed for forge to construct the packet
    }

    public PacketEntity(Entity entity, int id)
    {
        this.entityId = entity.getEntityId();
        this.id = id;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(entityId);
        buffer.writeInt(id);
        super.encodeInto(ctx, buffer);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.entityId = buffer.readInt();
        this.id = buffer.readInt();
        super.decodeInto(ctx, buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        handleServerSide(player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        Entity entity = player.getEntityWorld().getEntityByID(this.entityId);

        if (entity instanceof IPacketIDReceiver)
        {
            ((IPacketIDReceiver) entity).read(data(), id, player, this);
        }
    }
}
