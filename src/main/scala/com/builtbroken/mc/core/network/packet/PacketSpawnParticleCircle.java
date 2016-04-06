package com.builtbroken.mc.core.network.packet;


import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.rotation.EulerAngle;
import com.builtbroken.mc.lib.transform.vector.Pos;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2016.
 */
public class PacketSpawnParticleCircle extends PacketType
{
    /** Name of the particle effect */
    public String name;
    /** Dim to spawn particles in, used to check against client world's dim id to ensure no late packets spawn in correctly */
    public int dim;

    public double x;
    public double y;
    public double z;

    public double vx;
    public double vy;
    public double vz;

    public double distance;

    public PacketSpawnParticleCircle()
    {
        //Needed for forge to construct the packet
    }

    /**
     *
     * @param dim
     * @param x - start
     * @param y - start
     * @param z - start
     * @param vx - end
     * @param vy - end
     * @param vz - end
     */
    public PacketSpawnParticleCircle(String name, int dim, double x, double y, double z, double distance, double vx, double vy, double vz)
    {
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.distance = distance;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(dim);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeDouble(vx);
        buffer.writeDouble(vy);
        buffer.writeDouble(vz);
        buffer.writeDouble(distance);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        dim = buffer.readInt();
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        vx = buffer.readDouble();
        vy = buffer.readDouble();
        vz = buffer.readDouble();
        distance = buffer.readDouble();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        if (player.worldObj.provider.dimensionId == dim)
        {
            if(distance <= 1)
            {
                Engine.logger().error("PacketSpawnParticleCircle: Size of " + distance + " is to small to render effectively. " + new Pos(x, y, z).toString());
                return;
            }
            double c = 2 * Math.PI * distance;
            int particles = (int)Math.ceil(c) / 2;
            double angleSeg = 360.0 / (double)particles;

            for(int i = 0; i < particles; i++)
            {
                EulerAngle angle = new EulerAngle(angleSeg, 0);
                Pos pos = angle.toPos().add(x, y, z);
                player.worldObj.spawnParticle(name, pos.x(), pos.y(), pos.z(), vx, vy, vz);
            }
        }
    }
}
