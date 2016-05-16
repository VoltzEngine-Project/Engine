package com.builtbroken.mc.prefab.tile.interfaces.tile;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.framework.tile.api.ITileHost;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Applied to objects that handle logic processing for a Block.
 * <p>
 * Keep in mind that in some cases the host of this tile is not a TileEntity/Block. It could
 * also be an Entity or Simulator. So do not assume that a location will always be the same, or
 * all data is valid based on the location. So make sure to check every so often if the location
 * data has invalidated.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public interface ITile extends IPos3D, IWorldPosition
{
    /**
     * Sets the host of the tile, called
     * when a new instance of tile is
     * created or moved.
     *
     * @param host - new host
     */
    void setTileHost(ITileHost host);

    /**
     * Gets the current host of the tile
     *
     * @return current host
     */
    ITileHost getTileHost();

    /**
     * Called the very first tick of the world
     */
    void firstTick();

    /**
     * Called each tick of the world, so long as tick > 0
     *
     * @param tile  - if true the tick was called from the TileEntity, false is from the block
     * @param tick  - current tick, resets to one when value >= Long.MAX_VALUE - 5
     * @param delta - percent of time that has passed, should be near 1 if
     *              world is moving at recommended speed. If the time increases
     *              then something is overriding the tick calls. If the time slows
     *              then the world is slowing down. Do not use this to do normal
     *              operations, just use it for animation or progress updated.
     *              En sure that recipe or other operations move at world's speed.
     */
    void update(boolean tile, long tick, double delta);

    /** Called when the tile is added to the world. */
    void onAdded();

    /** Called when the tile is removed from the world */
    void onRemove();

    /**
     * Called to invalidate the tile, normally
     * called when the tile is destroyed,
     * saved, or cleaned up.
     */
    void invalidate();

    /**
     * Called when a player removes the block, use this to enforce harvesting rules.
     *
     * @param player      - player
     * @param willHarvest - will the player harvest the block, pre-check normally just creative and tools checks
     * @return true if the block was changed
     */
    boolean removeByPlayer(EntityPlayer player, boolean willHarvest);

    /**
     * Light value produced by this tile
     *
     * @return
     */
    int getLightValue();

    /**
     * Is this tile solid on the side given
     *
     * @param side
     * @return
     */
    boolean isSolid(int side);
}
