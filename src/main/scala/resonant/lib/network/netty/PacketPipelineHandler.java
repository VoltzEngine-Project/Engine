package resonant.lib.network.netty;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fluids.FluidTank;
import resonant.core.ResonantEngine;
import resonant.lib.References;
import resonant.lib.modproxy.ICompatProxy;
import resonant.lib.utility.nbt.ISaveObj;
import universalelectricity.core.transform.vector.IVector2;
import universalelectricity.core.transform.vector.IVector3;

import java.util.*;

/**
 * @since 26/05/14
 * @author tgame14
 */
@ChannelHandler.Sharable
public class PacketPipelineHandler extends MessageToMessageCodec<FMLProxyPacket, AbstractPacket> implements ICompatProxy
{

    private EnumMap<Side, FMLEmbeddedChannel> channelEnumMap;
    private List<Class<? extends AbstractPacket>> packets;

    public PacketPipelineHandler ()
    {
        this.packets = new LinkedList<Class<? extends AbstractPacket>>();
    }

    @Override
    protected void encode (ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception
    {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends AbstractPacket> clazz = msg.getClass();
        if (!this.packets.contains(msg.getClass()))
            throw new Exception("No packets registered for " + msg.getClass().getSimpleName());

        int packetId = this.packets.indexOf(clazz);
        buffer.writeByte(packetId);
        msg.encodeInto(ctx, buffer);
        FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    @Override
    protected void decode (ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception
    {
        ByteBuf payload = msg.payload();
        int packetId = payload.readByte();
        Class<? extends AbstractPacket> clazz = this.packets.get(packetId);

        if (clazz == null)
            throw new Exception("No packets Registered for packet ID: " + packetId);

        AbstractPacket packet = clazz.newInstance();
        packet.decodeInto(ctx, payload.slice());

        EntityPlayer entityPlayer;
        switch (FMLCommonHandler.instance().getEffectiveSide())
        {
        case CLIENT:
            entityPlayer = Minecraft.getMinecraft().thePlayer;
            packet.handleClientSide(entityPlayer);
            break;

        case SERVER:
            INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
            entityPlayer = ((NetHandlerPlayServer) netHandler).playerEntity;
            packet.handleServerSide(entityPlayer);
            break;
        default:
            break;
        }

        out.add(packet);
    }

    public boolean registerPacket (Class<? extends AbstractPacket> clazz)
    {
        if (this.packets.size() > 256)
        {
            References.LOGGER.fatal("Reached Max limit of packets! this is a fatal error, Report to: " + ResonantEngine.metadata.url);
            return false;
        }

        if (this.packets.contains(clazz))
        {
            References.LOGGER.warn("Packet is attempted to be registered twice! this is a programming error");
            return false;
        }

        if (!Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION))
        {
            References.LOGGER.warn("Error, hasn't reached postinit yet");
            return false;
        }
        this.packets.add(clazz);
        return true;
    }

    @Override
    public void preInit ()
    {
        // stub
    }

    @Override
    public void init ()
    {
        this.channelEnumMap = NetworkRegistry.INSTANCE.newChannel(References.CHANNEL, this);
    }

    @Override
    public void postInit ()
    {
        Collections.sort(this.packets, new Comparator<Class<? extends AbstractPacket>>()
        {
            @Override
            public int compare (Class<? extends AbstractPacket> o1, Class<? extends AbstractPacket> o2)
            {
                int com = String.CASE_INSENSITIVE_ORDER.compare(o1.getCanonicalName(), o2.getCanonicalName());

                if (com == 0)
                {
                    com = o1.getCanonicalName().compareTo(o2.getCanonicalName());
                }
                return com;
            }
        });
    }

    @Override
    public String modId ()
    {
        return ResonantEngine.metadata.modId;
    }

    /// *** THIS IS SPECIAL PACKET SENDING METHODS SIMILAR TO 1.6.4 IMPLEMENTATIONS *** ///

    /**
     * @param packet the packet to send to the player
     * @param player the player MP object
     */
    @SideOnly(Side.SERVER)
    public void sendToPlayer (AbstractPacket packet, EntityPlayerMP player)
    {
        this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
    }

    /**
     *
     * @param packet the packet to send to the players in the dimension
     * @param dimId the dimension ID to send to.
     */
    public void sendToAllInDimension (AbstractPacket packet, int dimId)
    {
        this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimId);
        this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
    }

    /** sends to all clients connected to the server
     *
     * @param packet the packet to send.
     */
    public void sendToAll (AbstractPacket packet)
    {
        this.channelEnumMap.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channelEnumMap.get(Side.CLIENT).writeAndFlush(packet);
    }

    public void sendToAllAround (AbstractPacket message, NetworkRegistry.TargetPoint point)
    {
        this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channelEnumMap.get(Side.SERVER).writeAndFlush(message);
    }

    @SideOnly(Side.CLIENT)
    public void sendToServer (AbstractPacket packet)
    {
        this.channelEnumMap.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channelEnumMap.get(Side.CLIENT).writeAndFlush(packet);
    }

    public void writeData (ByteBuf data, Object... sendData)
    {
        for (Object dataValue : sendData)
        {
            if (dataValue instanceof Integer)
            {
                data.writeInt((Integer) dataValue);
            }
            else if (dataValue instanceof Float)
            {
                data.writeFloat((Float) dataValue);
            }
            else if (dataValue instanceof Double)
            {
                data.writeDouble((Double) dataValue);
            }
            else if (dataValue instanceof Byte)
            {
                data.writeByte((Byte) dataValue);
            }
            else if (dataValue instanceof Boolean)
            {
                data.writeBoolean((Boolean) dataValue);
            }
            else if (dataValue instanceof String)
            {
                ByteBufUtils.writeUTF8String(data, (String) dataValue);
            }
            else if (dataValue instanceof Short)
            {
                data.writeShort((Short) dataValue);
            }
            else if (dataValue instanceof Long)
            {
                data.writeLong((Long) dataValue);
            }
            else if (dataValue instanceof IVector3)
            {
                data.writeDouble(((IVector3) dataValue).x());
                data.writeDouble(((IVector3) dataValue).y());
                data.writeDouble(((IVector3) dataValue).z());
            }
            else if (dataValue instanceof IVector2)
            {
                data.writeDouble(((IVector2) dataValue).x());
                data.writeDouble(((IVector2) dataValue).y());
            }
            else if (dataValue instanceof NBTTagCompound)
            {
                ByteBufUtils.writeTag(data, (NBTTagCompound) dataValue);
            }
            else if (dataValue instanceof FluidTank)
            {
                data.writeInt(((FluidTank) dataValue).getCapacity());
                ByteBufUtils.writeTag(data, ((FluidTank) dataValue).writeToNBT(new NBTTagCompound()));
            }
            else if (dataValue instanceof ISaveObj)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                ((ISaveObj) dataValue).save(nbt);
                ByteBufUtils.writeTag(data, nbt);
            }
            else
            {
                References.LOGGER.fatal("Resonant Engine packet attempt to write an invalid type: " + dataValue.getClass());
            }
        }
    }

}


