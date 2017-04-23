package com.builtbroken.mc.framework.logic;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.jlib.data.vector.Pos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.data.IPacket;
import com.builtbroken.mc.api.tile.IPlayerUsing;
import com.builtbroken.mc.api.tile.ITile;
import com.builtbroken.mc.api.tile.node.ITileNode;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.framework.logic.imp.ITileDesc;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
public class TileNode implements ITileNode, IPacketIDReceiver, ITileDesc, ITile
{
    protected static int DESCRIPTION_PACKET_ID = -1;

    protected ITileNodeHost host;

    protected final String id;
    protected final String mod;

    public TileNode(String id, String mod)
    {
        this.id = id;
        this.mod = mod;
    }

    @Override
    public void setHost(ITileNodeHost host)
    {
        this.host = host;
    }

    @Override
    public ITileNodeHost getHost()
    {
        return host;
    }

    public final boolean isServer()
    {
        return world() != null && !world().isRemote;
    }

    public final boolean isClient()
    {
        return world() != null && world().isRemote;
    }

    //=============================================
    //============== to string ====================
    //=============================================

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getClassDisplayName());
        builder.append("(");
        toStringData(builder);
        builder.append(")@");
        builder.append(hashCode());
        return builder.toString();
    }

    /**
     * Gets the debug display name of the class.
     *
     * @return name
     */
    protected String getClassDisplayName()
    {
        return getClass().getName();
    }

    /**
     * Called to build data about the object.
     *
     * @param builder - builder to append data to
     */
    protected void toStringData(StringBuilder builder)
    {
        builder.append("host = ");
        //only do host name@hash to prevent inf loop
        //      Host has 'this' in its toString()
        builder.append(host.getClass().getName());
        builder.append("@");
        builder.append(host.hashCode());
    }

    //=============================================
    //========== Position data ====================
    //=============================================

    public final Pos toPos()
    {
        return new Pos(x(), y(), z());
    }

    public final Location toLocation()
    {
        return new Location(world(), x(), y(), z());
    }

    //=============================================
    //========== Save Code     ====================
    //=============================================

    @Override
    public void load(NBTTagCompound nbt)
    {

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        return nbt;
    }

    //=============================================
    //========== Network code  ====================
    //=============================================

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if (id == DESCRIPTION_PACKET_ID)
        {
            if (isClient())
            {
                readDescPacket(buf);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldReadPacket(EntityPlayer player, IWorldPosition receiveLocation, PacketType packet)
    {
        return isClient() || new Location(player).distance(receiveLocation.x(), receiveLocation.y(), receiveLocation.z()) < 10 && packet instanceof PacketTile;
    }

    @Override
    public IPacket getDescPacket()
    {
        try
        {
            IPacket packet = getPacketForData(DESCRIPTION_PACKET_ID);
            writeDescPacket(packet.data());
            return packet;
        }
        catch (Exception e)
        {
            Engine.instance.logger().error("Failed to write description packet for " + this + "  ", e);
        }
        return null;
    }

    /**
     * Called client side only to read
     * the data from a description packet
     *
     * @param buf - what to read data from
     */
    public void readDescPacket(ByteBuf buf)
    {

    }

    /**
     * Called server side to write the
     * data for a description packet
     *
     * @param buf - what to write data to
     */
    public void writeDescPacket(ByteBuf buf)
    {

    }


    //===========================
    //==== Helpers ==============
    //===========================

    public double distance(Entity entity)
    {
        return distance(entity.posX, entity.posY, entity.posZ);
    }

    public double distance(IPos3D pos)
    {
        if (pos instanceof Pos3D)
        {
            return ((Pos3D) pos).distance(x() + 0.5, y() + 0.5, z() + 0.5);
        }
        return distance(pos.x(), pos.y(), pos.z());
    }

    public double distance(double x, double y, double z)
    {
        double xx = x() + 0.5 - x;
        double yy = y() + 0.5 - y;
        double zz = z() + 0.5 - z;

        return Math.sqrt(xx * xx + yy * yy + zz * zz);
    }

    /**
     * Sends the desc packet to all players around this tile
     */
    public void sendDescPacket()
    {
        sendPacket(getDescPacket());
    }

    public IPacket getPacketForData(Object... data)
    {
        return getHost().getPacketForData(data);
    }


    /**
     * Sends the packet to all players around this tile
     *
     * @param packet - packet to send
     */
    public void sendPacket(IPacket packet)
    {
        sendPacket(packet, 64);
    }

    /**
     * Sends the packet to all players around this tile
     *
     * @param packet   - packet to send
     * @param distance - distance in blocks to search for players
     */
    public void sendPacket(IPacket packet, double distance)
    {
        if (world() != null && isServer())
        {
            Engine.instance.packetHandler.sendToAllAround(packet, world(), xi(), yi(), zi(), distance);
        }
    }

    public void sendPacketToServer(IPacket packet)
    {
        if (world() != null && isClient())
        {
            Engine.instance.packetHandler.sendToServer(packet);
        }
    }

    public void sendPacketToGuiUsers(IPacket packet)
    {
        if (isServer() && this instanceof IPlayerUsing)
        {
            for (EntityPlayer player : ((IPlayerUsing) this).getPlayersUsing())
            {
                if (player instanceof EntityPlayerMP)
                {
                    Engine.instance.packetHandler.sendToPlayer(packet, (EntityPlayerMP) player);
                }
            }
        }
    }

    public boolean isInvalid()
    {
        if (getHost() instanceof TileEntity)
        {
            return ((TileEntity) getHost()).isInvalid();
        }
        else if (getHost() instanceof Entity)
        {
            return !((Entity) getHost()).isEntityAlive();
        }
        return true;
    }

    public final String getListenerKey()
    {
        return "";
    }

    @Override
    public String uniqueContentID()
    {
        return id;
    }

    @Override
    public String contentType()
    {
        return "node";
    }

    @Override
    public String modID()
    {
        return mod;
    }
}
