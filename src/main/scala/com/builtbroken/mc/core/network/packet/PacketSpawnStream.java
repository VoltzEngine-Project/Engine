package com.builtbroken.mc.core.network.packet;


import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.abstraction.world.IWorld;
import com.builtbroken.mc.core.Engine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2016.
 */
public class PacketSpawnStream extends PacketType
{
    public int dim;

    public double x;
    public double y;
    public double z;

    public double vx;
    public double vy;
    public double vz;

    public PacketSpawnStream()
    {
        //Needed for forge to construct the packet
    }

    public PacketSpawnStream(int dim, IPos3D pos, IPos3D pos2)
    {
        this(dim, pos.x(), pos.y(), pos.z(), pos2.x(), pos2.y(), pos2.z());
    }

    /**
     * @param dim
     * @param x   - start
     * @param y   - start
     * @param z   - start
     * @param vx  - end
     * @param vy  - end
     * @param vz  - end
     */
    public PacketSpawnStream(int dim, double x, double y, double z, double vx, double vy, double vz)
    {
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
            short short1 = 128;

            final Random rand = player.worldObj.rand;
            for (int l = 0; l < short1; ++l)
            {
                double d6 = (double) l / ((double) short1 - 1.0D);
                float f = (rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
                double d7 = vx + (x - vx) * d6 + (rand.nextDouble() - 0.5D) * 5.0D;
                double d8 = vy + (y - vy) * d6 + (rand.nextDouble() - 0.5D) * 5.0D;
                double d9 = vz + (z - vz) * d6 + (rand.nextDouble() - 0.5D) * 5.0D;
                IWorld world = Engine.minecraft.getWorld(dim);
                if (world != null)
                {
                    world.spawnParticle("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
                }
            }
        }
    }
}
