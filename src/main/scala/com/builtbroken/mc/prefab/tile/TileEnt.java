package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;

import javax.swing.*;
import java.awt.*;

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
            if (Engine.runningAsDev && !GraphicsEnvironment.isHeadless())
            {
                JOptionPane.showMessageDialog(null, "Class: " + getClass() + " needs to implement the newTile() call! Rather than using reflection!", "InfoBox: Missing newTile()", JOptionPane.INFORMATION_MESSAGE);
            }
            return getClass().newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

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

    @Override
    public AbstractPacket getDescPacket()
    {
        try
        {
            PacketTile packet = new PacketTile(this, DESCRIPTION_PACKET_ID);
            writeDescPacket(packet.data());
            return packet;
        }
        catch (Exception e)
        {
            Engine.instance.logger().error("Failed to write description packet for " + this + "  ", e);
        }
        return null;
    }
}
