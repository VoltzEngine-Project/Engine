package com.builtbroken.mc.framework.logic;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.framework.logic.imp.ITileNodeHost;
import net.minecraft.world.World;

/**
 * Replaces {@link com.builtbroken.mc.prefab.tile.Tile}
 * <p>
 * Object that is used to control the logic behind a tile
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/30/2017.
 */
public interface ITileNode extends IWorldPosition
{
    /**
     * Sets the host of the tile
     *
     * @param host
     */
    void setHost(ITileNodeHost host);

    /**
     * Gets the host of the tile
     *
     * @return
     */
    ITileNodeHost getHost();

    /**
     * Called for the first tick of the controller
     */
    default void firstTick()
    {

    }

    /**
     * Called each tick
     *
     * @param tick
     */
    default void update(long tick)
    {

    }

    /**
     * Called to destroy the controller
     */
    default void destroy()
    {

    }

    /**
     * Called every so often to clean up data
     * and refresh the tile's cache values.
     */
    default void doCleanupCheck()
    {

    }

    /**
     * How long to wait between cleanup calls
     *
     * @return
     */
    default int getNextCleanupTick()
    {
        return 200; //every 10 seconds (20 ticks a second, or 50ms a tick with 1000ms a second)
    }

    //=============================================
    //========== Position data ====================
    //=============================================

    default World world()
    {
        return getHost().world();
    }

    default double x()
    {
        return getHost().x();
    }

    default double y()
    {
        return getHost().y();
    }

    default double z()
    {
        return getHost().z();
    }

    default int xi()
    {
        return getHost().xi();
    }

    default int yi()
    {
        return getHost().yi();
    }

    default int zi()
    {
        return getHost().zi();
    }

    default float xf()
    {
        return getHost().xf();
    }

    default float yf()
    {
        return getHost().yf();
    }

    default float zf()
    {
        return getHost().zf();
    }
}
