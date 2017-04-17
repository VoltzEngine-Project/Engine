package com.builtbroken.mc.core.network.packet.callback;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.api.data.IPacket;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class PacketAudio implements IPacket
{
    int dim;
    double x, y, z;
    float pitch, volume;
    String audioKey;

    public PacketAudio()
    {

    }

    public PacketAudio(World world, String key, double x, double y, double z, float pitch, float volume)
    {
        dim = world != null && world.provider != null ? world.provider.dimensionId : 0;
        audioKey = key;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.volume = volume;
    }


    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(dim);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeFloat(pitch);
        buffer.writeFloat(volume);
        ByteBufUtils.writeUTF8String(buffer, audioKey);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        dim = buffer.readInt();
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        pitch = buffer.readFloat();
        volume = buffer.readFloat();
        audioKey = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld.provider != null && Minecraft.getMinecraft().theWorld.provider.dimensionId == dim)
        {
            Engine.proxy.playJsonAudio(Minecraft.getMinecraft().theWorld, audioKey, x, y, z, pitch, volume);
        }
    }
}
