package resonant.lib.network.netty;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

import java.util.EnumMap;

/**
 * This handler serves 2 purposes
 *
 * 1. A Simple packet system designed after cpw's {@link cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper} packet system
 * 2. A Gateway for more advanced packet behaviour. This is the PacketType section where tile / Entities are specified.
 * Or even more advanced systems.
 *
 * cpw's explanation on how to use his simple packet system
 *
 *  * This class is a simplified netty wrapper for those not wishing to deal with the full power of netty.
 * It provides a simple message driven system, based on a discriminator byte over the custom packet channel.
 * It assumes that you have a series of unique message types with each having a unique handler. Generally, context should be
 * derived at message handling time.
 *
 * Usage is simple:<ul>
 * <li>construct, and store, an instance of this class. It will automatically register and configure your underlying netty channel.
 *
 * <li>Then, call {@link #registerPacket(Class, Class, int, cpw.mods.fml.relauncher.Side)} (Class, Class, byte, Side)} for each message type you want to exchange
 * providing an {@link resonant.lib.network.netty.IPacketHander} implementation class as well as an {@link resonant.lib.network.netty.INettyPacket} implementation class. The side parameter
 * to that method indicates which side (server or client) the <em>message processing</em> will occur on. The discriminator byte
 * should be unique for this channelName - it is used to discriminate between different types of message that might
 * occur on this channel (a simple form of message channel multiplexing, if you will).
 * <li>To get a packet suitable for presenting to the rest of minecraft, you can call {@link #getMCPacket(INettyPacket)}. The return result
 * is suitable for returning from things like {@link net.minecraft.tileentity.TileEntity#getDescriptionPacket()} for example.
 * <li>Finally, use the sendXXX to send unsolicited messages to various classes of recipients.
 * </ul>
 *
 * Example
 * <code>
 * <pre>
 *  // Request message
 *  public Message1 implements IMessage {
 *  // message structure
 *   public fromBytes(ByteBuf buf) {
 *    // build message from byte array
 *   }
 *   public toBytes(ByteBuf buf) {
 *    // put message content into byte array
 *   }
 *  }
 *  // Reply message
 *  public Message2 implements IMessage {
 *   // stuff as before
 *  }
 *  // Message1Handler expects input of type Message1 and returns type Message2
 *  public Message1Handler implements IMessageHandler<Message1,Message2> {
 *   public Message2 onMessage(Message1 message, MessageContext ctx) {
 *    // do something and generate reply message
 *    return aMessage2Object;
 *   }
 *  }
 *  // Message2Handler expects input of type Message2 and returns no message (IMessage)
 *  public Message2Handler implements IMessageHandler<Message2,IMessage> {
 *   public IMessage onMessage(Message2 message, MessageContext ctx) {
 *    // handle the message 2 response message at the other end
 *    // no reply for this message - return null
 *    return null;
 *   }
 *  }
 *
 *  // Code in a {@link cpw.mods.fml.common.event.FMLPreInitializationEvent} or {@link cpw.mods.fml.common.event.FMLInitializationEvent} handler
 *  SimpleNetworkWrapper wrapper = NetworkRegistry.newSimpleChannel("MYCHANNEL");
 *  // Message1 is handled by the Message1Handler class, it has discriminator id 1 and it's on the client
 *  wrapper.registerMessage(Message1Handler.class, Message1.class, 1, Side.CLIENT);
 *  // Message2 is handled by the Message2Handler class, it has discriminator id 2 and it's on the server
 *  wrapper.registerMessage(Message2Handler.class, Message2.class, 2, Side.SERVER);
 *  </pre>
 * </code>
 *
 *
 * @author cpw
 *
 * @since 25/05/14
 * @author tgame14
 */
public class PacketManager
{

    private EnumMap<Side, FMLEmbeddedChannel> channels;
    private PacketIndexedCodec packetCodec;

    public PacketManager (String channelName)
    {
        this.packetCodec = new PacketIndexedCodec();
        this.channels = NetworkRegistry.INSTANCE.newChannel(channelName, packetCodec);
    }

    public <REQ extends INettyPacket, REPLY extends INettyPacket> void registerPacket (Class<? extends IPacketHander<REQ, REPLY>> packetHandler, Class<REQ> requestPacketType, int discriminator, Side side)
    {
        packetCodec.addDiscriminator(discriminator, requestPacketType);
        FMLEmbeddedChannel channel = channels.get(side);
        String type = channel.findChannelHandlerNameForType(PacketIndexedCodec.class);
        if (side == Side.SERVER)
        {
            addServerHandlerAfter(channel, type, packetHandler, requestPacketType);
        }
        else
        {
            addClientHandlerAfter(channel, type, packetHandler, requestPacketType);
        }

    }

    private <REQ extends INettyPacket, REPLY extends INettyPacket, NH extends INetHandler> void addServerHandlerAfter (FMLEmbeddedChannel channel, String type, Class<? extends IPacketHander<REQ, REPLY>> packetHandler, Class<REQ> requestPacketType)
    {
        PacketChannelHandlerWrapper<REQ, REPLY> handler = getHandlerWrapper(packetHandler, Side.SERVER, requestPacketType);
        channel.pipeline().addAfter(type, packetHandler.getName(), handler);
    }


    private <REQ extends INettyPacket, REPLY extends INettyPacket> void addClientHandlerAfter (FMLEmbeddedChannel channel, String type, Class<? extends IPacketHander<REQ, REPLY>> packetHandler, Class<REQ> requestPacketType)
    {
        PacketChannelHandlerWrapper<REQ, REPLY> handler = getHandlerWrapper(packetHandler, Side.CLIENT, requestPacketType);
    }

    private <REQ extends INettyPacket, REPLY extends INettyPacket> PacketChannelHandlerWrapper<REQ, REPLY> getHandlerWrapper (Class<? extends IPacketHander<REQ, REPLY>> packetHandler, Side side, Class<REQ> requestPacketType)
    {
        return new PacketChannelHandlerWrapper<REQ, REPLY>(packetHandler, side, requestPacketType);
    }

    /**
     *
     * @param packet the {@link resonant.lib.network.netty.INettyPacket} packet you want to transform
     * @return a {@link net.minecraft.network.Packet} packet version of the parameter given. Can be used for TileEntity packets
     */
    public Packet getMCPacket (INettyPacket packet)
    {
        return channels.get(Side.SERVER).generatePacketFrom(packet);
    }

    /**
     * Send this message to everyone.
     * The {@link resonant.lib.network.netty.IPacketHander} for this message type should be on the CLIENT side.
     *
     * @param packet The message to send
     */
    public void sendToAll (INettyPacket packet)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this message to the specified player.
     * The {@link resonant.lib.network.netty.IPacketHander} for this message type should be on the CLIENT side.
     *
     * @param packet The message to send
     * @param player The player to send it to
     */
    public void sendToPlayer (INettyPacket packet, EntityPlayerMP player)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * The {@link resonant.lib.network.netty.IPacketHander} for this message type should be on the CLIENT side.
     *
     * @param message The message to send
     * @param point The {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint} around which to send
     */
    public void sendToAllAround (INettyPacket message, NetworkRegistry.TargetPoint point)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * The {@link resonant.lib.network.netty.IPacketHander} for this message type should be on the CLIENT side.
     *
     * @param message The message to send
     * @param dimensionId The dimension id to target
     */
    public void sendToDimension (INettyPacket message, int dimensionId)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this message to the server.
     * The {@link resonant.lib.network.netty.IPacketHander} for this message type should be on the SERVER side.
     *
     * @param message The message to send
     */
    public void sendToServer (INettyPacket message)
    {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
