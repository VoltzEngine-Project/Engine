package com.builtbroken.mc.core.network.packet.callback;

import com.builtbroken.mc.api.tile.access.IGuiTile;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.imp.transform.vector.Location;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/23/2017.
 */
public class PacketOpenGUI extends PacketTile
{
    public PacketOpenGUI()
    {
        //Needed for forge to construct the packet
    }

    public PacketOpenGUI(TileEntity tile, int guiID)
    {
        super("openGUI#" + guiID, tile, guiID);
    }

    @Override
    public void handle(EntityPlayer player, TileEntity tile)
    {
        if (!player.world.isRemote)
        {
            IGuiTile guiTile = null;
            if (tile instanceof IGuiTile)
            {
                guiTile = (IGuiTile) tile;
            }
            else if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IGuiTile)
            {
                guiTile = (IGuiTile) ((ITileNodeHost) tile).getTileNode();
            }
            if (guiTile != null)
            {
                ByteBuf buf = data().slice();
                int guiID = buf.readInt();
                if (!guiTile.openGui(player, guiID))
                {
                    Engine.logger().error("Failed to open gui with ID(" + guiID + ") at location " + new Location(player.world, x, y, z) + ", tile = " + tile);
                }
            }
            else if (Engine.runningAsDev)
            {
                Engine.logger().error("Tile at location " + new Location(player.world, x, y, z) + " is not an instance of ITileGUI, tile = " + tile);
            }
        }
        else if (Engine.runningAsDev)
        {
            Engine.logger().error("Can not open GUI on client using PacketOpenGUI, tile = " + tile);
        }
    }

}
