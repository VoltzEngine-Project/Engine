package com.builtbroken.mc.seven.client;

import com.builtbroken.mc.seven.abstraction.MinecraftWrapper;
import com.builtbroken.mc.seven.abstraction.world.WorldWrapper;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/14/2017.
 */
public class MinecraftWrapperClient extends MinecraftWrapper
{
    @Override
    protected WorldWrapper newWorldWrapper(World world)
    {
        return new WorldWrapperClient(world);
    }

    @Override
    public boolean isShiftHeld()
    {
        return Keyboard.isCreated() && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
    }
}
