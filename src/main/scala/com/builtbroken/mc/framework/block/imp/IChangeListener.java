package com.builtbroken.mc.framework.block.imp;

import net.minecraft.block.Block;

/**
 * Applied to objects that listen for changes to the block or around the block
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/2/2017.
 */
public interface IChangeListener extends ITileEventListener
{
    /**
     * Called when a block near changes
     *
     * @param block
     */
    default void onNeighborBlockChange(Block block)
    {
        onBlockChanged();
    }

    /**
     * Called when a block near this location changes
     *
     * @param tileX
     * @param tileY
     * @param tileZ
     */
    default void onNeighborChange(int tileX, int tileY, int tileZ)
    {
        onBlockChanged();
    }

    /**
     * Redirect by default methods to avoid having to implement each method.
     * Is not called outside of the interface.
     */
    default void onBlockChanged()
    {

    }

    @Override
    default String getListenerKey()
    {
        return "change";
    }
}
