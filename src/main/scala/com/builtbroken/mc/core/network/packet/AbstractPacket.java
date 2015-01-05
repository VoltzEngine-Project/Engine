package com.builtbroken.mc.core.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * For custom packets extend this Class and register on Mod loading phase
 * <p/>
 * Without registering a NPE will be thrown as the {@link com.builtbroken.mc.core.network.netty.PacketManager} won't know how to handle it
 * <p/>
 * To send this packet also look at {@link com.builtbroken.mc.core.network.netty.PacketManager#sendToAll(AbstractPacket)}
 * And other implementations there.
 *
 * @author tgame14
 * @since 26/05/14
 */
public abstract class AbstractPacket
{

	/**
	 * Encode the packet data into the ByteBuf stream. Complex data sets may need specific data handlers
	 *
	 * @param ctx    channel context
	 * @param buffer the buffer to encode into
	 * @see {@link cpw.mods.fml.common.network.ByteBufUtils}
	 * @see {@link com.builtbroken.mc.core.network.netty.PacketManager#writeData(io.netty.buffer.ByteBuf, Object...)}
	 */
	public abstract void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer);

	/**
	 * Decode the packet data from the ByteBuf stream. Complex data sets may need specific data handlers
	 *
	 * @param ctx    channel context
	 * @param buffer the buffer to decode from
	 * @See {@link cpw.mods.fml.common.network.ByteBufUtils}
	 */
	public abstract void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer);

	/**
	 * Handle a packet on the client side. Note this occurs after decoding has completed.
	 *
	 * @param player the player reference
	 */
	public void handleClientSide(EntityPlayer player)
	{
		throw new UnsupportedOperationException("Unsupported operation for Packet: " + getClass().getSimpleName());
	}

	/**
	 * Handle a packet on the server side. Note this occurs after decoding has completed.
	 *
	 * @param player the player reference
	 */
	public void handleServerSide(EntityPlayer player)
	{
		throw new UnsupportedOperationException("Unsupported operation for Packet: " + getClass().getSimpleName());
	}
}
