package calclavia.lib.utility;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import buildcraft.api.tools.IToolWrench;
import calclavia.components.ItemScrewdriver;

public class WrenchUtility
{
    public static boolean isWrench(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof IToolWrench)
            {
                return true;
            }
            else if (itemStack.getItem() instanceof ItemScrewdriver)
            {
                return true;
            }

            /** Industrialcraft */
            try
            {
                Class wrenchClass = itemStack.getItem().getClass();

                if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
                {
                    return true;
                }
            }
            catch (Exception e)
            {
            }
        }

        return false;
    }

    /** A function that denotes if an itemStack is a wrench that can be used. Override this for more
     * wrench compatibility. Compatible with Buildcraft and IC2 wrench API via reflection.
     * 
     * @return True if it is a wrench. */
    public static boolean isUsableWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
    {
        if (player != null && itemStack != null)
        {
            if (itemStack.getItem() instanceof IToolWrench)
            {
                return ((IToolWrench) itemStack.getItem()).canWrench(player, x, y, z);
            }
            else if (itemStack.getItem() instanceof ItemScrewdriver)
            {
                return true;
            }

            /** Industrialcraft */
            try
            {
                Class wrenchClass = itemStack.getItem().getClass();

                if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
                {
                    return itemStack.getItemDamage() < itemStack.getMaxDamage();
                }
            }
            catch (Exception e)
            {
            }
        }

        return false;
    }

    /** This function damages a wrench. Works with Buildcraft and Industrialcraft wrenches.
     * 
     * @return True if damage was successfull. */
    public static boolean damageWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
    {
        if (isUsableWrench(player, itemStack, x, y, z))
        {
            if (itemStack.getItem() instanceof IToolWrench)
            {
                ((IToolWrench) itemStack.getItem()).wrenchUsed(player, x, y, z);
                return true;
            }

            /** Industrialcraft */
            try
            {
                Class wrenchClass = itemStack.getItem().getClass();

                if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
                {
                    Method methodWrenchDamage = wrenchClass.getMethod("damage", ItemStack.class, Integer.TYPE, EntityPlayer.class);
                    methodWrenchDamage.invoke(itemStack.getItem(), itemStack, 1, player);
                    return true;
                }
            }
            catch (Exception e)
            {
            }
        }

        return false;
    }

}
