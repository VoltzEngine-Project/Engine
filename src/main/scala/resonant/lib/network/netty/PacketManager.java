package resonant.lib.network.netty;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import resonant.engine.References;
import resonant.lib.modproxy.ICompatProxy;
import resonant.lib.utility.nbt.ISaveObj;
import universalelectricity.core.transform.vector.IVector2;
import universalelectricity.core.transform.vector.IVector3;
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

	public static void writeData(ByteBuf data, Object... sendData)
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
			else if (dataValue instanceof IByteBufObject)
			{
				((IByteBufObject) dataValue).writeBytes(data);
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
				References.LOGGER.fatal("Resonant Engine packet attempt to write an invalid object: " + dataValue + " with class: " + dataValue.getClass());
			}
		}
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
	@SideOnly(Side.SERVER)
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


