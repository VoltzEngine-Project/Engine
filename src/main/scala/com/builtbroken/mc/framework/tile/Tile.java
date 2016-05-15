package com.builtbroken.mc.framework.tile;

/**
 * Logic object that takes over for the TileEntity class in Minecraft
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2016.
 */
public class Tile
{
    /**
     * Called the very first tick of the world
     */
    public void firstTick()
    {
    }

    /**
     * Called each tick of the world, so long as tick > 0
     *
     * @param tick  - current tick, resets to one when value = Long.MAX_VALUE -=5
     * @param delta - percent of time that has passed, should be near 1 if
     *              world is moving at recommended speed. If the time increases
     *              then something is overriding the tick calls. If the time slows
     *              then the world is slowing down. Do not use this to do normal
     *              operations, just use it to animation or progress updated. En
     *              sure that recipe or other operations move at world's speed.
     */
    public void update(long tick, double delta)
    {

    }
}
