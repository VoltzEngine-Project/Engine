package com.builtbroken.mc.core;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import com.builtbroken.mc.lib.render.block.BlockRenderHandler$;
import com.builtbroken.mc.lib.render.model.loader.FixedTechneModelLoader;

import javax.swing.*;

/**
 * The Resonant Engine client proxy
 */
public class ClientProxy extends CommonProxy
{
	static
	{
		AdvancedModelLoader.registerModelHandler(new FixedTechneModelLoader());
	}

	@Override
	public void preInit()
	{
		RenderingRegistry.registerBlockHandler(BlockRenderHandler$.MODULE$);
	}

    @Override
    public void init()
    {
        if(Loader.isModLoaded("UniversalElectricity"))
        {
            JOptionPane.showMessageDialog(null, "UniversalElectricity is now part of Resonant Engine and should no longer be installed. \n To prevent world corruption the game will now close with a warning.", "Install Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("UniversalElectricity is already contained within Resonant Engine and shouldn't be installed as a standalone");
        }
    }

	@Override
	public boolean isPaused()
	{
		if (FMLClientHandler.instance().getClient().isSingleplayer() && !FMLClientHandler.instance().getClient().getIntegratedServer().getPublic())
		{
			GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;

			if (screen != null)
			{
				if (screen.doesGuiPauseGame())
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
}
