package com.builtbroken.mc.core;

import cpw.mods.fml.common.Loader;
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

	}

    public int getPlayerDim()
    {
        throw new UnsupportedOperationException("This method can not be called server side");
    }
}
