package com.builtbroken.mc.framework.block.imp;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2017.
 */
public interface IBlockStackListener extends ITileEventListener
{
    /**
     * Gets the stack when the player uses the pick block feature (middle mouse button)
     * <p>
     * This is a client only method
     *
     * @param target
     * @param player
     * @return
     */
    default ItemStack getPickBlock(MovingObjectPosition target, EntityPlayer player)
    {
        return toStack();
    }

    /**
     * Gets the stack of the listener,
     * this is normally only used for
     * Tiles and nodes. Thus its only
     * a call back for defaults.
     *
     * @return
     */
    default ItemStack toStack()
    {
        return null;
    }

    /**
     * Gets the items to drop when the block is broken
     *
     * @param drops
     * @param metadata
     * @param fortune
     */
    default void collectDrops(List<ItemStack> drops, int metadata, int fortune)
    {

    }


    /**
     * Gets the itemstack to drop when a player silk harvests the block
     *
     * @param player
     * @param meta
     * @return true to disable normal drop
     */
    default boolean collectSilkHarvestDrops(List<ItemStack> drops, EntityPlayer player, int meta)
    {
        return false;
    }


    /**
     * Gets the block to show int he creative tab
     * This is a client only method
     *
     * @param item
     * @param creativeTabs
     * @param list
     */
    default void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {

    }

    /**
     * Called to drop the block as an item
     *
     * @param itemStack
     */
    default void dropBlockAsItem(ItemStack itemStack)
    {

    }

    default String getListenerKey()
    {
        return BlockListenerKeys.BLOCK_STACK;
    }
}
