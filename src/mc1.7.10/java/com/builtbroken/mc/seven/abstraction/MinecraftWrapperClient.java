package com.builtbroken.mc.seven.abstraction;

import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.seven.abstraction.world.WorldWrapper;
import com.builtbroken.mc.seven.abstraction.world.WorldWrapperClient;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
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
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.CLIENT)
        {
            if (Minecraft.getMinecraft().theWorld != null && (worldWrapperClient == null || worldWrapperClient.getWorld() != Minecraft.getMinecraft().theWorld))
            {
                worldWrapperClient = (WorldWrapperClient) newWorldWrapper(Minecraft.getMinecraft().theWorld);
            }
            return worldWrapperClient; //Only 1 world exists on the client
        }
        return super.getWorld(dim);
    }

    @Override
    protected WorldWrapper newWorldWrapper(World world)
    {
        if (world != null && world.isRemote)
        {
            return new WorldWrapperClient(world);
        }
        return super.newWorldWrapper(world);
    }

    @Override
    public boolean isShiftHeld()
    {
        return Keyboard.isCreated() && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
    }
}
