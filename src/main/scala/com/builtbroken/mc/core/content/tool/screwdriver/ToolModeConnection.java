package com.builtbroken.mc.core.content.tool.screwdriver;

import com.builtbroken.mc.api.tile.ConnectionType;
import com.builtbroken.mc.api.tile.connection.ConnectionColor;
import com.builtbroken.mc.api.tile.connection.IAdjustableConnections;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ToolModeConnection extends ToolMode
{
    public final String modeName;
    public final ConnectionType connectionType;

    public ToolModeConnection(String modeName, ConnectionType connectionType)
    {
        this.modeName = modeName;
        this.connectionType = connectionType;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, ConnectionColor connectionColor, World world, int x, int y, int z, int sideIndex, float hitX, float hitY, float hitZ)
    {
        IAdjustableConnections machine = null;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IAdjustableConnections)
        {
            machine = (IAdjustableConnections) tile;
        }
        else if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof IAdjustableConnections)
        {
            machine = (IAdjustableConnections) ((ITileNodeHost) tile).getTileNode();
        }

        if (machine != null)
        {
            ForgeDirection side = ForgeDirection.getOrientation(sideIndex);
            if (machine.onToolUsed(stack, player, connectionColor, connectionType, side, hitX, hitY, hitZ))
            {
                return true;
            }
            else if (machine.supportsConnection(connectionType, connectionColor, side))
            {
                if (!player.worldObj.isRemote)
                {
                    String reply = machine.cycleConnection(connectionType, connectionColor, side);
                    if (reply != null)
                    {
                        String translate = LanguageUtility.getLocal(reply);
                        if (translate != null && !translate.isEmpty())
                        {
                            player.addChatComponentMessage(new ChatComponentText(translate));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String getInfoName()
    {
        return Engine.itemWrench.getUnlocalizedName() + ".mode." + modeName + ".info";
    }

    @Override
    public String getItemUnlocalizedName(ConnectionColor color)
    {
        return Engine.itemWrench.getUnlocalizedName() + "." + modeName + "." + color.name().toLowerCase();
    }

    @Override
    public String getTexture(String iconString)
    {
        return iconString + "." + modeName;
    }

    @Override
    public String getColorizedTexture(String iconString)
    {
        return iconString + "." + modeName + ".color";
    }
}
