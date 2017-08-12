package com.builtbroken.mc.framework.block.imp;

import net.minecraft.world.World;

import java.util.Random;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/17/2017.
 */
public interface IUpdateListener extends ITileEventListener
{
    default String getListenerKey()
    {
        return "update";
    }

    /**
     * Update trigger from a tile
     *
     * @param ticks
     */
    default void update(long ticks)
    {

    }

    /**
     * Update trigger from a block
     *
     * @param par5Random
     */
    default void updateTick(Random par5Random)
    {

    }

    /**
     * Update trigger from a block for graphics
     *
     * @param par5Random
     */
    default void randomDisplayTick(Random par5Random)
    {

    }

    /**
     * Called to get the tick rate
     * <p>
     * does not support location injection
     * but does pass in world
     *
     * @return
     */
    default int tickRate(World world)
    {
        return -1;
    }
}
