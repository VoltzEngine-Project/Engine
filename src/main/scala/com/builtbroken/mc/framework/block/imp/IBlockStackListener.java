package com.builtbroken.mc.framework.block.imp;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;

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
    default ItemStack getPickBlock(RayTraceResult target, EntityPlayer player)
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
     *  @param drops
     * @param block
     * @param fortune
     */
    default void collectDrops(List<ItemStack> drops, IBlockState block, int fortune)
    {

    }


    /**
     * Gets the block to show int he creative tab
     * This is a client only method
     *
     */
    default void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {

    }

    default String getListenerKey()
    {
        return "blockStack";
    }
}
