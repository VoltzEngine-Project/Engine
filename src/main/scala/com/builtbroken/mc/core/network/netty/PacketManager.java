package com.builtbroken.mc.core.network.netty;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.lib.mod.loadable.ILoadable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import com.builtbroken.mc.lib.helper.wrapper.ByteBufWrapper;
import com.builtbroken.mc.core.network.packet.PacketEntity;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;

import java.util.EnumMap;

/**
 * @author tgame14
 * @since 26/05/14
 */
public class PacketManager implements ILoadable
{
    public final String channel;
    protected EnumMap<Side, FMLEmbeddedChannel> channelEnumMap;

    public PacketManager(String channel)
    {
        this.channel = channel;
    }

    @Deprecated
    public static void writeData(ByteBuf data, Object... sendData)
    {
        new ByteBufWrapper.ByteBufWrapper(data).$less$less$less(sendData);
    }

    public static PacketType getPacketFor(Object obj)
    {
        if (obj instanceof TileEntity)
        {
            return new PacketTile((TileEntity) obj);
        }

        if (obj instanceof Entity)
        {
            return new PacketEntity((Entity) obj);
        }

        return null;
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

    /**
     * @param packet the packet to send to the player
     * @param player the player MP object
     */
    public void sendToPlayer(AbstractPacket packet, EntityPlayerMP player)
    {
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
        }
        else
        {
            Engine.error("Packet sent to player[" + player + "]");
        }
    }

    /**
     * @param packet the packet to send to the players in the dimension
     * @param dimId  the dimension ID to send to.
     */
    public void sendToAllInDimension(AbstractPacket packet, int dimId)
    {
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimId);
            this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
        }
        else
        {
            Engine.error("Packet sent to dim[" + dimId + "]");
        }
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
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
            this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
        }
        else
        {
            Engine.error("Packet sent to all");
        }
    }

    public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point)
    {
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
            this.channelEnumMap.get(Side.SERVER).writeAndFlush(message);
        }
        else
        {
            Engine.error("Packet sent to target point: " + point);
        }
    }

    public void sendToAllAround(AbstractPacket message, IWorldPosition point, double range)
    {
        sendToAllAround(message, point.world(), point.x(), point.y(), point.z(), range);
    }

    public void sendToAllAround(AbstractPacket message, World world, IPos3D point, double range)
    {
        sendToAllAround(message, world, point.x(), point.y(), point.z(), range);
    }

    public void sendToAllAround(AbstractPacket message, TileEntity tile)
    {
        sendToAllAround(message, tile, 64);
    }

    public void sendToAllAround(AbstractPacket message, TileEntity tile, double range)
    {
        sendToAllAround(message, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, range);
    }

    public void sendToAllAround(AbstractPacket message, World world, double x, double y, double z, double range)
    {
        if (world != null)
            sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, range));
    }

    @SideOnly(Side.CLIENT)
    public void sendToServer(AbstractPacket packet)
    {
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            this.channelEnumMap.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
            this.channelEnumMap.get(Side.CLIENT).writeAndFlush(packet);
        }
        else
        {
            Engine.error("Packet sent to server");
        }
    }
}


