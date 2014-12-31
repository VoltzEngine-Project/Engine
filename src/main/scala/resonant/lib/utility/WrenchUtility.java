package resonant.lib.utility;

import buildcraft.api.tools.IToolWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import resonant.engine.References;
import resonant.engine.ResonantEngine;
import resonant.engine.content.ItemScrewdriver;
import resonant.lib.mod.compat.ModEnum;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility to handle several different types of wrench implementation better.
 *
 * @author Calclavia, Darkguardsman
 */
public class WrenchUtility
{
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
            if (itemStack.getItem() instanceof IToolWrench)
            {
                return true;
            }
            else if (itemStack.getItem() instanceof ItemScrewdriver)
            {
                return true;
            }

            /** Industrialcraft */
            if (ModEnum.IC2.isLoaded())
            {
                try
                {
                    Class wrenchClass = itemStack.getItem().getClass();

                    if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
                    {
                        return true;
                    }
                } catch (ClassNotFoundException e)
                {
                    References.LOGGER.error("Failed to use reflection for IC2 Wrench support");
                    if (ResonantEngine.runningAsDev)
                    {
                        References.LOGGER.catching(e);
                    }
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
            if (itemStack.getItem() instanceof IToolWrench)
            {
                return ((IToolWrench) itemStack.getItem()).canWrench(player, x, y, z);
            }
            else if (itemStack.getItem() instanceof ItemScrewdriver)
            {
                return true;
            }

            /** Industrialcraft */
            if (ModEnum.IC2.isLoaded())
            {
                try
                {
                    Class wrenchClass = itemStack.getItem().getClass();

                    if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
                    {
                        return itemStack.getItemDamage() < itemStack.getMaxDamage();
                    }
                } catch (ClassNotFoundException e)
                {
                    References.LOGGER.error("Failed to use reflection for IC2 Wrench support");
                    if (ResonantEngine.runningAsDev)
                    {
                        References.LOGGER.catching(e);
                    }
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
            if (itemStack.getItem() instanceof IToolWrench)
            {
                ((IToolWrench) itemStack.getItem()).wrenchUsed(player, x, y, z);
                return true;
            }

            /** Industrialcraft */
            if (ModEnum.IC2.isLoaded())
            {
                try
                {
                    Class wrenchClass = itemStack.getItem().getClass();

                    if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
                    {
                        Method methodWrenchDamage = wrenchClass.getMethod("damage", ItemStack.class, Integer.TYPE, EntityPlayer.class);
                        methodWrenchDamage.invoke(itemStack.getItem(), itemStack, 1, player);
                        return true;
                    }
                } catch (ClassNotFoundException e)
                {
                    References.LOGGER.error("Failed to use reflection to get IC2 wrench classes");
                    if (ResonantEngine.runningAsDev)
                    {
                        References.LOGGER.catching(e);
                    }
                } catch (NoSuchMethodException e)
                {
                    References.LOGGER.error("Failed to use reflection to access IC2 wrench methods");
                    if (ResonantEngine.runningAsDev)
                    {
                        References.LOGGER.catching(e);
                    }
                } catch (InvocationTargetException e)
                {
                    References.LOGGER.error("Failed to use reflection to invoke IC2 wrench methods");
                    if (ResonantEngine.runningAsDev)
                    {
                        References.LOGGER.catching(e);
                    }
                } catch (IllegalAccessException e)
                {
                    References.LOGGER.error("Reflection failure: Something is blocking access to IC2 wrench methods");
                    if (ResonantEngine.runningAsDev)
                    {
                        References.LOGGER.catching(e);
                    }
                }
            }
        }

        return false;
    }

}
