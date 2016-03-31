package com.builtbroken.mc.core.network.packet;


import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2016.
 */
public class PacketSpawnParticle extends PacketType
{
    String name;
    public int dim;
    public double x;
    public double y;
    public double z;
    public double vx;
    public double vy;
    public double vz;

    public PacketSpawnParticle()
    {

    }

    public PacketSpawnParticle(String name, int dim, double x, double y, double z, double vx, double vy, double vz)
    {
        this.name = name;
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        ByteBufUtils.writeUTF8String(buffer, name);
        buffer.writeInt(dim);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeDouble(vx);
        buffer.writeDouble(vy);
        buffer.writeDouble(vz);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        name = ByteBufUtils.readUTF8String(buffer);
        dim = buffer.readInt();
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        vx = buffer.readDouble();
        vy = buffer.readDouble();
        vz = buffer.readDouble();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        if (player.worldObj.provider.dimensionId == dim)
        {
            player.worldObj.spawnParticle(name, x, y, z, vx, vy, vz);
        }
    }
}
