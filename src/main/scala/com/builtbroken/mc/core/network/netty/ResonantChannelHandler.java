package com.builtbroken.mc.core.network.netty;

import com.builtbroken.mc.api.data.IPacket;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.*;
import com.builtbroken.mc.core.network.packet.callback.PacketAudio;
import com.builtbroken.mc.core.network.packet.callback.PacketBlast;
import com.builtbroken.mc.core.network.packet.callback.PacketOpenGUI;
import com.builtbroken.mc.core.network.packet.callback.chunk.PacketRequestData;
import com.builtbroken.mc.core.network.packet.callback.chunk.PacketSendData;
import com.builtbroken.mc.core.network.packet.user.PacketMouseClick;
import com.builtbroken.mc.core.network.packet.user.PacketPlayerItemMode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;

/**
 * @author tgame14
 * @since 31/05/14
 */
public class ResonantChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket>
{
    public boolean silenceStackTrace = false; //TODO add command and config

    private int nextID = 0;

    public ResonantChannelHandler()
    {
        addPacket(PacketTile.class);
        addPacket(PacketEntity.class);
        addPacket(PacketPlayerItem.class);
        addPacket(PacketPlayerItemMode.class);
        addPacket(PacketSelectionData.class);
        addPacket(PacketSpawnParticle.class);
        addPacket(PacketSpawnStream.class);
        addPacket(PacketSpawnParticleCircle.class);
        addPacket(PacketBiomeData.class);
        addPacket(PacketSpawnParticleStream.class);
        addPacket(PacketMouseClick.class);
        addPacket(PacketBlast.class);
        addPacket(PacketAudio.class);
        addPacket(PacketOpenGUI.class);
        addPacket(PacketGui.class);
        //addPacket(PacketAccessGui.class); TODO re-add when access system is added
        addPacket(PacketSendData.class);
        addPacket(PacketRequestData.class);
    }

    public void addPacket(Class<? extends IPacket> clazz)
    {
        addDiscriminator(nextID++, clazz);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket packet, ByteBuf target) throws Exception
    {
        try
        {
            packet.encodeInto(ctx, target);
        }
        catch (Exception e)
        {
            if (!silenceStackTrace)
            {
                Engine.logger().error("Failed to encode packet " + packet, e);
            }
            else
            {
                Engine.logger().error("Failed to encode packet " + packet + " E: " + e.getMessage());
            }
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket packet)
    {
        try
        {
            packet.decodeInto(ctx, source);
        }
        catch (Exception e)
        {
            if (!silenceStackTrace)
            {
                Engine.logger().error("Failed to decode packet " + packet, e);
            }
            else
            {
                Engine.logger().error("Failed to decode packet " + packet + " E: " + e.getMessage());
            }
        }
    }
}
