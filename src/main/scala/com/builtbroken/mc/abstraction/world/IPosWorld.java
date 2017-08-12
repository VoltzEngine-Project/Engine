package com.builtbroken.mc.abstraction.world;

import com.builtbroken.jlib.data.vector.IPos3D;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public interface IPosWorld extends IPos3D
{
    IWorld getWorld();
}
