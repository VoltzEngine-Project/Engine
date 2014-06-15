package resonant.lib.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @since 26/05/14
 * @author tgame14
 */
public class PacketEntity extends PacketType
{
    protected int entityId;

    public PacketEntity(Entity entity, Object[] args)
    {
        super(args);
        this.entityId = entity.getEntityId();
    }


    @Override
    public void encodeInto (ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(this.entityId);
        buffer.writeBytes(this.data);
    }

    @Override
    public void decodeInto (ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.entityId = buffer.readInt();
        this.data = buffer.slice();

    }

    @Override
    public void handleClientSide (EntityPlayer player)
    {
        Entity entity = player.getEntityWorld().getEntityByID(this.entityId);
        if (entity instanceof IPacketReceiver)
        {
            ((IPacketReceiver) entity).onReceivePacket(data, player);
        }
    }

    @Override
    public void handleServerSide (EntityPlayer player)
    {
        Entity entity = player.getEntityWorld().getEntityByID(this.entityId);
        if (entity instanceof IPacketReceiver)
        {
            ((IPacketReceiver) entity).onReceivePacket(data, player);
        }
    }
}
