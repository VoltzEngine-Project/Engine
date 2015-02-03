package com.builtbroken.mc.core;

import com.builtbroken.mc.core.content.entity.EntityExCreeper;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.player.EntityPlayer;
import com.builtbroken.mc.lib.mod.AbstractProxy;

/**
 * The Resonant Engine common proxy
 *
 * @author Calclavia
 */
public class CommonProxy extends AbstractProxy
{
	public boolean isPaused()
	{
		return false;
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}

	@Override
	public void init()
	{
        EntityRegistry.registerGlobalEntityID(EntityExCreeper.class, "ExCreeper", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityExCreeper.class, "ExCreeper", 55, Engine.instance, 100, 1, true);
	}

    public int getPlayerDim()
    {
        throw new UnsupportedOperationException("This method can not be called server side");
    }
}
