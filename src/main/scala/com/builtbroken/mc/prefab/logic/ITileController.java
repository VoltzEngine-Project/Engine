package com.builtbroken.mc.prefab.logic;

/**
 * Replaces {@link com.builtbroken.mc.prefab.tile.Tile}
 * <p>
 * Object that is used to control the logic behind a tile
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/30/2017.
 */
public interface ITileController
{
    /**
     * Called for the first tick of the controller
     */
    void firstTick();

    /**
     * Called each tick
     *
     * @param tick
     */
    void update(long tick);

    /**
     * Called to destroy the controller
     */
    void destroy();

    /**
     * Called every so often to clean up data
     * and refresh the tile's cache values.
     */
    void doCleanupCheck();

    /**
     * How long to wait between cleanup calls
     *
     * @return
     */
    int getNextCleanupTick();

    /**
     * Unique save ID used to ID this tile
     *
     * @return
     */
    String getUniqueID();
}
