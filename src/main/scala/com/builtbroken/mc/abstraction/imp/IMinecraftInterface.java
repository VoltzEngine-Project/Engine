package com.builtbroken.mc.abstraction.imp;

import com.builtbroken.mc.abstraction.entity.IEntityData;
import com.builtbroken.mc.abstraction.world.IWorld;

/**
 * Applied to abstraction providers that wrapper Minecraft data to Voltz Engine data providers
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public interface IMinecraftInterface
{
    IWorld getWorld(int dim);

    IEntityData getEntity(int entityID);

    ///TODO get ITEM, BLOCK, MATERIAL, etc
}
