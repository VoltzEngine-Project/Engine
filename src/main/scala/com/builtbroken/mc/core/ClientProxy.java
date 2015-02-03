package com.builtbroken.mc.core;

import com.builtbroken.mc.lib.render.block.BlockRenderHandler;
import com.builtbroken.mc.prefab.entity.EntityCreeperEx;
import com.builtbroken.mc.prefab.entity.RenderCreeper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import com.builtbroken.mc.lib.render.model.loader.FixedTechneModelLoader;

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
		RenderingRegistry.registerBlockHandler(new BlockRenderHandler());
	}

    @Override
    public void init()
    {
        super.init();
        RenderingRegistry.registerEntityRenderingHandler(EntityCreeperEx.class, new RenderCreeper());
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
	public EntityClientPlayerMP getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

    public World getClientWorld()
    {
        return getClientPlayer() != null ? getClientPlayer().worldObj : null;
    }

    @Override
    public int getPlayerDim()
    {
        return getClientWorld() != null ? getClientWorld().provider.dimensionId : 0;
    }
}
