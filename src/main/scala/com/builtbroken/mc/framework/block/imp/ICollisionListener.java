package com.builtbroken.mc.framework.block.imp;

import net.minecraft.entity.Entity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2017.
 */
public interface ICollisionListener extends ITileEventListener
{
    default void onEntityCollidedWithBlock(Entity entity)
    {

    }

    default String getListenerKey()
    {
        return "collision";
    }
}
