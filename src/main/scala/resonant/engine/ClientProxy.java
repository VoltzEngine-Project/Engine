package resonant.engine;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import resonant.content.wrapper.BlockRenderHandler$;
import resonant.engine.content.debug.GuiCreativeBuilder;
import resonant.engine.content.debug.TileCreativeBuilder;
import resonant.lib.render.model.FixedTechneModelLoader;
import universalelectricity.core.transform.vector.Vector3;

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
		if (world.getTileEntity(x, y, z) instanceof TileCreativeBuilder)
		{
			return new GuiCreativeBuilder(new Vector3(x, y, z));
		}

		return null;
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
}
