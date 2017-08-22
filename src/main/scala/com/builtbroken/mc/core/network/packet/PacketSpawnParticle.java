package com.builtbroken.mc.core.network.packet;


import com.builtbroken.mc.client.effects.VisualEffectProvider;
import com.builtbroken.mc.client.effects.VisualEffectRegistry;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IEffectData;
import com.builtbroken.mc.core.Engine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

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
    public NBTTagCompound otherData;

    public boolean endPoint = false;

    public PacketSpawnParticle()
    {
        //Needed for forge to construct the packet
    }

    public PacketSpawnParticle(String name, World world, double x, double y, double z, double vx, double vy, double vz)
    {
        this(name, world.provider.getDimension(), x, y, z, vx, vy, vz);
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
        buffer.writeBoolean(endPoint);
        buffer.writeBoolean(otherData != null);
        if (otherData != null)
        {
            ByteBufUtils.writeTag(buffer, otherData);
        }
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
        endPoint = buffer.readBoolean();
        if (buffer.readBoolean())
        {
            otherData = ByteBufUtils.readTag(buffer);
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        if (player.world.provider.getDimension() == dim)
        {
            try
            {
                if (name.startsWith("JSON_"))
                {
                    String key = name.substring(5, name.length()).toLowerCase();
                    IEffectData data = ClientDataHandler.INSTANCE.getEffect(key);
                    if (data != null)
                    {
                        data.trigger(player.getEntityWorld(), x, y, z, vx, vy, vz, endPoint, otherData != null ? otherData : new NBTTagCompound());
                    }
                    else if (Engine.runningAsDev)
                    {
                        Engine.logger().error("Failed to find a effect data for key '" + name + "'");
                    }
                }
                else if (name.startsWith("VEP_"))
                {
                    String key = name.substring(4, name.length());
                    VisualEffectProvider provider = VisualEffectRegistry.main.get(key);
                    if (provider != null)
                    {
                        provider.displayEffect(player.getEntityWorld(), x, y, z, vx, vy, vz, endPoint, otherData != null ? otherData : new NBTTagCompound());
                    }
                    else if (Engine.runningAsDev)
                    {
                        Engine.logger().error("Failed to find a visual effect provider for name '" + name + "'");
                    }
                }
                else if (name.startsWith("MC_"))
                {
                    String key = name.substring(3, name.length());
                    EnumParticleTypes type = EnumParticleTypes.getByName(key);
                    if (type != null)
                    {
                        player.world.spawnParticle(type, x, y, z, vx, vy, vz);
                    }
                }
            }
            catch (Exception e)
            {
                Engine.logger().error("Failed handling particle spawn packet with [name=" + name + ", dim=" + dim + ",pos=" + x + ", " + y + ", " + z + ", Vel=" + vx + ", " + vy + ", " + vz + "]", e);
            }
        }
    }
}
