package com.builtbroken.mc.core.content.tool.screwdriver;

import com.builtbroken.mc.api.tile.ConnectionType;
import com.builtbroken.mc.api.tile.connection.ConnectionColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class ToolMode
{
    private static final List<ToolMode> REGISTRY = new ArrayList<>();
    public static ToolModeGeneral MODE_GENERAL = new ToolModeGeneral();

    static
    {
        //Default modes
        ToolMode.addMode(ToolMode.MODE_GENERAL);
        ToolMode.addMode(new ToolModeRotation());
        ToolMode.addMode(new ToolModeConnection("item", ConnectionType.INVENTORY));
        ToolMode.addMode(new ToolModeConnection("fluid", ConnectionType.FLUID));
        ToolMode.addMode(new ToolModeConnection("power", ConnectionType.POWER));
    }

    public static ToolMode getMode(int index)
    {
        ToolMode mode = REGISTRY.get(index);
        if (mode != null)
        {
            return mode;
        }
        return MODE_GENERAL;
    }

    public static int addMode(ToolMode mode)
    {
        REGISTRY.add(mode);
        return REGISTRY.size() - 1;
    }

    public static int getModeCount()
    {
        return REGISTRY.size();
    }

    public static List<ToolMode> getModes()
    {
        return REGISTRY;
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, ConnectionColor connectionColor)
    {
        return itemStack;
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, ConnectionColor connectionColor, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, ConnectionColor connectionColor, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * The name of the tool mode for display.
     * <p>
     * Used for tool tip info but might be used
     * int the future for GUIs.
     *
     * @return item.modname:wrench.mode.type.info (type -> your mode, modname -> your mod)
     */
    public abstract String getInfoName();

    /**
     * Gets the unlocalized name to use for the item name
     * <p>
     * When a mode is switched the item name changes to reflect the mode.
     * This is to improve user interaction and reduce mistakes
     *
     * @return item.modname:wrench.mode.type (type -> your mode, modname -> your mod)
     */
    public abstract String getItemUnlocalizedName(ConnectionColor color);

    public String getTexture(String iconString)
    {
        return null;
    }

    public String getColorizedTexture(String iconString)
    {
        return null;
    }

    public boolean canBeColored()
    {
        return getColorizedTexture(null) != null;
    }
}
