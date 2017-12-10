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

    /**
     * Can the player harvest the block at the location. This is not
     * called by MC's canHarvest check but the wrapper block's own harvest
     * checks.
     *
     * @param player - player who is harvesting the block
     * @param meta   - meta value of the block
     * @return true if can harvest
     */
    default boolean canHarvest(EntityPlayer player, int meta)
    {
        return true;
    }

    /**
     * Can the player harvest the block at the location using silk touch enchantment.
     *
     * @param player - player who is harvesting the block
     * @param meta   - meta value of the block
     * @return true if can harvest
     */
    default boolean canSilkHarvest(EntityPlayer player, int meta)
    {
        return false;
    }


    @Override
    default String getListenerKey()
    {
        return "break";
    }
}
