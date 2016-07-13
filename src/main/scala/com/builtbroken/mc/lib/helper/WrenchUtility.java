package com.builtbroken.mc.lib.helper;

import com.builtbroken.mc.core.content.tool.ItemScrewdriver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Utility to handle several different types of wrench implementation better.
 *
 * @author Calclavia, Darkguardsman
 */
public class WrenchUtility
{
    /** List of wrench data objects used to complete method called, basicly a proxy system. */
    private static List<WrenchType> wrenchTypes = new ArrayList();
    /** Internal cache for quicker look up on some items */
    private static HashMap<Item, WrenchType> cache = new HashMap();

    /**
     * Is the item the player is hold a wrench
     */
    public static boolean isHoldingWrench(EntityPlayer player)
    {
        return isWrench(player.getHeldItem());
    }

    /**
     * Is the item a wrench
     */
    public static boolean isWrench(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof ItemScrewdriver)
            {
                return true;
            }
            //Checked each time for support of power tools, and selective items that are not always wrenches
            WrenchType type = getWrenchTypeFor(itemStack);
            if (type != null && type.isWrench(itemStack))
            {
                return true;
            }
            for (WrenchType t : wrenchTypes)
            {
                if (t.isWrench(itemStack))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Used to check if the item held by the player is a usable wrench by anything calling this
     * method. Checks for common wrenches used by most mods including interface checks. The item
     * itself doesn't have to be called or even look like a wrench. This simply looks for any item
     * can can act like a wrench
     *
     * @param player - player using & currently hold the wrench
     * @param x      - xCoord of the target block
     * @param y      - yCoord of the target block
     * @param z      - zCoord of the target block
     * @return true if the item the player is using is a wrench
     */
    public static boolean isUsableWrench(EntityPlayer player, int x, int y, int z)
    {
        return isUsableWrench(player, player.getHeldItem(), x, y, z);
    }

    /**
     * Used to check if the item is a usable wrench by anything calling this method. Checks for
     * common wrenches used by most mods including interface checks. The item itself doesn't have to
     * be called or even look like a wrench. This simply looks for any item can can act like a
     * wrench
     *
     * @param player    - player using the wrench
     * @param itemStack - the wrench itself
     * @param x         - xCoord of the target block
     * @param y         - yCoord of the target block
     * @param z         - zCoord of the target block
     * @return true if the item the player is using is a wrench
     */
    public static boolean isUsableWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
    {
        if (player != null && itemStack != null)
        {
            if (itemStack.getItem() instanceof ItemScrewdriver)
            {
                return true;
            }

            WrenchType type = getWrenchTypeFor(itemStack);
            if (type != null && type.isUsableWrench(player, itemStack, x, y, z))
            {
                return true;
            }
            for (WrenchType t : wrenchTypes)
            {
                if (t.isUsableWrench(player, itemStack, x, y, z))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Called to damage the wrench itemStack after it has been used
     *
     * @param player - player using & holding the wrench
     * @param x      - xCoord of the target block
     * @param y      - yCoord of the target block
     * @param z      - zCoord of the target block
     * @return true if the wrench was damaged, or action went threw without issues
     */
    public static boolean damageWrench(EntityPlayer player, int x, int y, int z)
    {
        return damageWrench(player, player.getHeldItem(), x, y, z);
    }

    /**
     * Called to damage the wrench itemStack after it has been used
     *
     * @param player    - player using the wrench
     * @param itemStack - the wrench itself
     * @param x         - xCoord of the target block
     * @param y         - yCoord of the target block
     * @param z         - zCoord of the target block
     * @return true if the wrench was damaged, or action went threw without issues
     */
    public static boolean damageWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
    {
        if (isUsableWrench(player, itemStack, x, y, z))
        {
            WrenchType type = getWrenchTypeFor(itemStack);
            if (type != null && type.damageWrench(player, itemStack, x, y, z))
            {
                return true;
            }
            for (WrenchType t : wrenchTypes)
            {
                if (t.damageWrench(player, itemStack, x, y, z))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Cache lookup
     *
     * @param stack
     * @return
     */
    public static WrenchType getWrenchTypeFor(ItemStack stack)
    {
        if (cache.containsKey(stack.getItem()))
        {
            return cache.get(stack.getItem());
        }
        return null;
    }

    public static void registerWrenchType(WrenchType type)
    {
        if (!wrenchTypes.contains(type))
        {
            wrenchTypes.add(type);
        }
    }

    /**
     * Data Object used to act as a proxy layer between the above methods
     * and any interfaces. This way if the interfaces or classes are not
     * loaded the game does not crash.
     */
    public static class WrenchType
    {
        public boolean isWrench(ItemStack itemStack)
        {
            return false;
        }

        public boolean damageWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
        {
            return false;
        }

        public boolean isUsableWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
        {
            return false;
        }
    }

}
