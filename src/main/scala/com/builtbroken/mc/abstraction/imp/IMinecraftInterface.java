package com.builtbroken.mc.abstraction.imp;

import com.builtbroken.mc.abstraction.tile.ITileMaterial;
import com.builtbroken.mc.abstraction.world.IWorld;
import net.minecraft.world.World;

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

    /**
     * Called to spawn a particle in the world.
     * <p>
     * Server side this will send a packet to the client
     *
     * @param name  - name of the particle, this needs to be specific or it will not work
     * @param world - world to spawn the particle inside
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @param xx    - velocity, in some cases this is used as extra data
     * @param yy    - velocity, in some cases this is used as extra data
     * @param zz    - velocity, in some cases this is used as extra data
     */
    void spawnParticle(String name, World world, double x, double y, double z, double xx, double yy, double zz);
}
