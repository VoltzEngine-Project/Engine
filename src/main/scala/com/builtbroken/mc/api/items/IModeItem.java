package com.builtbroken.mc.api.items;

import com.builtbroken.jlib.type.IntRange;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/** Designed to be used as a layer between an item and tool mode switch utility. AKA scrolling of middle
 * mouse button.
 *
 * Created by robert on 2/14/2015.
 */
public interface IModeItem
{
    /**
     * Current selected mode of the item
     * @param stack - stack to pull data from
     * @return current mode
     */
    int getMode(ItemStack stack);

    /** Sets the current mode of the tool
     * @param stack - stack to set the mode into
     */
    void setMode(ItemStack stack, int mode);

    /**
     * Sub version of the interface that will automatically be
     * handled by Voltz Engine.
     */
    interface IModeScrollItem extends IModeItem
    {
        /**
         * Called to handle scrolling to next mode. Due to mouse
         * input being client side this method is only called
         * client side. However, it will send a setMode packet
         * to the sever. This way the server side code does
         * update with the returned value
         *
         * @param stack - stack to edit
         * @param player - player who scrolled with the item
         * @param delta - change in mode
         * @return the new mode
         */
        int cycleMode(ItemStack stack, EntityPlayer player, int delta);
    }
}
