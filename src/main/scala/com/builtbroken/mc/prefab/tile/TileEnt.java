package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by robert on 1/12/2015.
 */
public class TileEnt extends Tile
{
    protected static int DESCRIPTION_PACKET_ID = -1;

    public TileEnt(String name, Material material)
    {
        super(name, material);
    }

    @Override
    public Tile newTile()
    {
        try
        {
            return getClass().newInstance();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if(id == DESCRIPTION_PACKET_ID)
        {
            readDescPacket(buf);
            return true;
        }
        return false;
    }

    public void readDescPacket(ByteBuf buf)
    {

    }

    public void writeDescPacket(ByteBuf buf)
    {

    }

    public void sendDescPacket()
    {
        PacketTile packet = new PacketTile(this, DESCRIPTION_PACKET_ID);
        writeDescPacket(packet.data());
        sendPacket(packet);
    }
}
