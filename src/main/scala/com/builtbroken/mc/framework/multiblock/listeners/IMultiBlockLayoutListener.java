package com.builtbroken.mc.framework.multiblock.listeners;

import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import net.minecraft.item.ItemStack;

/**
 * Listener for properties about a multi-block
 * <p>
 * Normally used to get info about a block when its not placed (aka in item form)
 * <p>
 * Uses key {@link com.builtbroken.mc.framework.block.imp.BlockListenerKeys#MULTI_BLOCK_LAYOUT_LISTENER}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/5/2018.
 */
public interface IMultiBlockLayoutListener extends ITileEventListener
{
    /**
     * Gets the layout key for the block as an item
     * <p>
     * Will be called without setting world position
     *
     * @param stack - block as an item
     * @return
     */
    default String getMultiBlockLayoutKey(ItemStack stack)
    {
        return getMultiBlockLayoutKey();
    }

    /**
     * Gets the layout key for the block as an item
     *
     * @return
     */
    default String getMultiBlockLayoutKey()
    {
        return null;
    }
}
