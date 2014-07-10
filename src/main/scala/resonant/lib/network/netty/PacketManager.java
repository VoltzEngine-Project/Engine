package resonant.lib.network.netty;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import resonant.engine.References;
import resonant.lib.modproxy.ICompatProxy;
import resonant.lib.network.ByteBufWrapper;
import universalelectricity.core.transform.vector.Vector3;

import java.util.EnumMap;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketManager implements ICompatProxy
{
	protected EnumMap<Side, FMLEmbeddedChannel> channelEnumMap;

	public final String channel;

	public PacketManager(String channel)
	{
		this.channel = channel;
	}

	@Deprecated
	public static void writeData(ByteBuf data, Object... sendData)
	{
		new ByteBufWrapper.ByteBufWrapper(data).$less$less$less(sendData);
	}

	public Packet toMCPacket(AbstractPacket packet)
	{
		return channelEnumMap.get(FMLCommonHandler.instance().getEffectiveSide()).generatePacketFrom(packet);
	}

	@Override
	public void preInit()
	{

	}

	@Override
	public void init()
	{
		this.channelEnumMap = NetworkRegistry.INSTANCE.newChannel(channel, new ResonantChannelHandler(), new ResonantPacketHandler());
	}

	@Override
	public void postInit()
	{

	}

	@Override
	public String modId()
	{
		return References.NAME;
	}

	/**
	 * @param packet the packet to send to the player
	 * @param player the player MP object
	 */
	public void sendToPlayer(AbstractPacket packet, EntityPlayerMP player)
	{
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
	}

	/**
	 * @param packet the packet to send to the players in the dimension
	 * @param dimId  the dimension ID to send to.
	 */
	public void sendToAllInDimension(AbstractPacket packet, int dimId)
	{
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimId);
		this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
	}

	public void sendToAllInDimension(AbstractPacket packet, World world)
	{
		sendToAllInDimension(packet, world.provider.dimensionId);
	}

	/**
	 * sends to all clients connected to the server
	 *
	 * @param packet the packet to send.
	 */
	public void sendToAll(AbstractPacket packet)
	{
		this.channelEnumMap.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channelEnumMap.get(Side.CLIENT).writeAndFlush(packet);
	}

	public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point)
	{
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		this.channelEnumMap.get(Side.SERVER).writeAndFlush(message);
	}

	public void sendToAllAround(AbstractPacket message, World world, Vector3 point, double range)
	{
		sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.dimensionId, point.x(), point.y(), point.z(), range));
	}

	@SideOnly(Side.CLIENT)
	public void sendToServer(AbstractPacket packet)
	{
		this.channelEnumMap.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channelEnumMap.get(Side.CLIENT).writeAndFlush(packet);
	}
}


