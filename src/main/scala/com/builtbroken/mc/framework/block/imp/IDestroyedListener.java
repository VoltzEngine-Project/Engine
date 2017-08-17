package com.builtbroken.mc.framework.block.imp;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Explosion;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface IDestroyedListener extends ITileEventListener
{
    /**
     * Called when tile is destroyed by an explosion
     *
     * @param ex
     */
    default void onDestroyedByExplosion(Explosion ex)
    {

    }

    /**
     * Called when the tile is broken
     *
     * @param block
     * @param meta
     */
    default void breakBlock(Block block, int meta)
    {

    }

    /**
     * Called when the tile is broken by a player
     *
     * @param player      - player who broke the block
     * @param willHarvest - if the block will drop, or is being harvested
     * @return true if handled, e.g. block was removed during this call
     */
    default boolean removedByPlayer(EntityPlayer player, boolean willHarvest)
    {
        return false;
    }

    @Override
    default String getListenerKey()
    {
        return "break";
    }
}
