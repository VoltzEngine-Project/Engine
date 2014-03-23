package calclavia.components;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import universalelectricity.api.vector.Vector3;
import calclavia.components.creative.BlockCreativeBuilder;
import calclavia.components.creative.GuiCreativeBuilder;
import calclavia.lib.prefab.ProxyBase;
import calclavia.lib.render.block.BlockRenderingHandler;
import calclavia.lib.render.model.TechneAdvancedModelLoader;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends ProxyBase
{
	static
	{
		AdvancedModelLoader.registerModelHandler(new TechneAdvancedModelLoader());
	}

	@Override
	public void preInit()
	{
		RenderingRegistry.registerBlockHandler(BlockRenderingHandler.INSTANCE);
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
		Block block = Block.blocksList[world.getBlockId(x, y, z)];

		if (block instanceof BlockCreativeBuilder)
			return new GuiCreativeBuilder(new Vector3(x, y, z));

		return null;
	}
}
