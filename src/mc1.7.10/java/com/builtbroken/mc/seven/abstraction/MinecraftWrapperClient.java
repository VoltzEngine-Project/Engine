package com.builtbroken.mc.seven.abstraction;

import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.seven.abstraction.world.WorldWrapperClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/14/2017.
 */
public class MinecraftWrapperClient extends MinecraftWrapper
{
    WorldWrapperClient worldWrapperClient;

    @Override
    public IWorld getWorld(int dim)
    {
        if (Minecraft.getMinecraft().theWorld != null)
        {
            if (dim != Minecraft.getMinecraft().theWorld.provider.dimensionId)
            {
                if (Engine.runningAsDev) //TODO remove if we load other worlds on the client in the future
                {
                    Engine.logger().error("Something is trying to access a world on the client that is not the world the player is currently inside");
                    return null;
                }
            }
            if (worldWrapperClient == null || worldWrapperClient.getWorld() != Minecraft.getMinecraft().theWorld)
            {
                worldWrapperClient = newWorldWrapper(Minecraft.getMinecraft().theWorld);
            }
            return worldWrapperClient; //Only 1 world exists on the client
        }
        return null;
    }

    @Override
    protected WorldWrapperClient newWorldWrapper(World world)
    {
        if (world != null)
        {
            return new WorldWrapperClient(world);
        }
        return null;
    }

    @Override
    public boolean isShiftHeld()
    {
        return Keyboard.isCreated() && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
    }
}
