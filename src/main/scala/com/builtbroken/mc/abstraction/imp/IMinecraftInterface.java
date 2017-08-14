package com.builtbroken.mc.abstraction.imp;

import com.builtbroken.mc.abstraction.tile.ITileMaterial;
import com.builtbroken.mc.abstraction.world.IWorld;

/**
 * Applied to abstraction providers that wrapper Minecraft data to Voltz Engine data providers
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public interface IMinecraftInterface
{
    /**
     * Called to get a world by dim id
     *
     * @param dim
     * @return
     */
    IWorld getWorld(int dim);

    ///TODO get ITEM, BLOCK, MATERIAL, etc

    /**
     * Called to get a tile material by name
     *
     * @param name - string value of the material, forces lowercase
     * @return material, or null if it doesn't exist
     */
    ITileMaterial getTileMaterial(String name);
}
