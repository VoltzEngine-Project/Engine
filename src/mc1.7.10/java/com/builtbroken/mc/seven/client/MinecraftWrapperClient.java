package com.builtbroken.mc.seven.client;

import com.builtbroken.mc.seven.abstraction.MinecraftWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/14/2017.
 */
public class MinecraftWrapperClient extends MinecraftWrapper
{
    @Override
    public void spawnParticle(String name, World world, double x, double y, double z, double xx, double yy, double zz)
    {
        Minecraft.getMinecraft().renderGlobal.spawnParticle(name, x, y, z, (float) xx, (float) yy, (float) zz);
    }
}
