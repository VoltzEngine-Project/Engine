package com.builtbroken.mc.framework.block.imp;

import net.minecraft.entity.Entity;

/**
 * More of a getter than a listener, but provides a way to modify the default return of the provided methods.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface IResistanceListener extends ITileEventListener
{
    /**
     * Called to get the explosion resistance of the tile.
     * <p>
     * Least resistance value of all listeners is used. Though in the future this may be
     * changed to allow the user to invert this check.
     *
     * @param entity
     * @return
     */
    default float getExplosionResistance(Entity entity)
    {
        return -1;
    }

    /**
     * Called to get the explosion resistance of the tile.
     * <p>
     * Least resistance value of all listeners is used. Though in the future this may be
     * changed to allow the user to invert this check.
     *
     * @param entity
     * @param explosionX
     * @param explosionY
     * @param explosionZ
     * @return
     */
    default float getExplosionResistance(Entity entity, double explosionX, double explosionY, double explosionZ)
    {
        return getExplosionResistance(entity);
    }

    @Override
    default String getListenerKey()
    {
        return "resistance";
    }
}
