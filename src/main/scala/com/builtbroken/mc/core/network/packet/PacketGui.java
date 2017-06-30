package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.IPacketReceiver;
import com.builtbroken.mc.core.network.ex.PacketIDException;
import com.builtbroken.mc.core.network.ex.PacketTileReadException;
import com.builtbroken.mc.imp.transform.vector.Location;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Handles sending packets to GUIs or Containers directly.
 * <p>
 * The main use for this is non-tile based GUIs that need data from server or client to function.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2017.
 */
public class PacketGui extends PacketType
{
    public PacketGui()
    {
        //Needed for forge to construct the packet
    }

    public PacketGui(Object... args)
    {
        super(args);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeBytes(data());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        data_$eq(buffer.slice());
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        Location location = new Location(player);
        if (screen instanceof IPacketIDReceiver)
        {
            if (((IPacketIDReceiver) screen).shouldReadPacket(player, location, this))
            {
                try
                {
                    IPacketIDReceiver receiver = (IPacketIDReceiver) screen;
                    ByteBuf buf = data().slice();

                    int id;
                    try
                    {
                        id = buf.readInt();
                    }
                    catch (IndexOutOfBoundsException ex)
                    {
                        Engine.instance.logger().error(new PacketIDException(location));
                        return;
                    }
                    receiver.read(buf, id, player, this);
                }
                catch (IndexOutOfBoundsException e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Packet was read past it's size."));
                    Engine.instance.logger().error("Error: ", e);
                }
                catch (NullPointerException e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Null pointer while reading data", e));
                    Engine.instance.logger().error("Error: ", e);
                }
                catch (Exception e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Failed to read packet", e));
                    Engine.instance.logger().error("Error: ", e);
                }
            }
            else
            {
                Engine.instance.logger().error("Error: " + screen + " rejected packet " + this + " due to invalid conditions.");
            }
        }
        else if (screen instanceof IPacketReceiver)
        {
            if (((IPacketReceiver) screen).shouldReadPacket(player, location, this))
            {
                try
                {
                    IPacketReceiver receiver = (IPacketReceiver) screen;
                    receiver.read(data().slice(), player, this);
                }
                catch (IndexOutOfBoundsException e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Packet was read past it's size."));
                }
                catch (Exception e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Failed to read packet", e));
                    e.printStackTrace();
                }
            }
            else
            {
                Engine.instance.logger().error("Error: " + screen + " rejected packet " + this + " due to invalid conditions.");
            }
        }
        else
        {
            Engine.instance.logger().error(new PacketTileReadException(location, "Unsupported action for " + screen));
        }
    }


    @Override
    public void handleServerSide(EntityPlayer player)
    {
        Container container = player.openContainer;
        Location location = new Location(player);
        if (container instanceof IPacketIDReceiver)
        {
            if (((IPacketIDReceiver) container).shouldReadPacket(player, location, this))
            {
                try
                {
                    IPacketIDReceiver receiver = (IPacketIDReceiver) container;
                    ByteBuf buf = data().slice();

                    int id;
                    try
                    {
                        id = buf.readInt();
                    }
                    catch (IndexOutOfBoundsException ex)
                    {
                        Engine.instance.logger().error(new PacketIDException(location));
                        return;
                    }
                    receiver.read(buf, id, player, this);
                }
                catch (IndexOutOfBoundsException e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Packet was read past it's size."));
                    Engine.instance.logger().error("Error: ", e);
                }
                catch (NullPointerException e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Null pointer while reading data", e));
                    Engine.instance.logger().error("Error: ", e);
                }
                catch (Exception e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Failed to read packet", e));
                    Engine.instance.logger().error("Error: ", e);
                }
            }
            else
            {
                Engine.instance.logger().error("Error: " + container + " rejected packet " + this + " due to invalid conditions.");
            }
        }
        else if (container instanceof IPacketReceiver)
        {
            if (((IPacketReceiver) container).shouldReadPacket(player, location, this))
            {
                try
                {
                    IPacketReceiver receiver = (IPacketReceiver) container;
                    receiver.read(data().slice(), player, this);
                }
                catch (IndexOutOfBoundsException e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Packet was read past it's size."));
                }
                catch (Exception e)
                {
                    Engine.instance.logger().error(new PacketTileReadException(location, "Failed to read packet", e));
                    e.printStackTrace();
                }
            }
            else
            {
                Engine.instance.logger().error("Error: " + container + " rejected packet " + this + " due to invalid conditions.");
            }
        }
        else
        {
            Engine.instance.logger().error(new PacketTileReadException(location, "Unsupported action for " + container));
        }
    }
}
