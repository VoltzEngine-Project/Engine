package resonant.content;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import resonant.content.wrapper.RenderTileDummy;
import resonant.engine.References;

public class ClientRegistryProxy extends CommonRegistryProxy
{
	@Override
	public void registerTileEntity(String name, Class<? extends TileEntity> tileClass)
	{
		super.registerTileEntity(name, tileClass);

		TileEntitySpecialRenderer tileRenderer = null;

		try
		{
			String rendererName = tileClass.getName().replaceFirst("Tile", "Render");
			Class renderClass = Class.forName(rendererName);
			tileRenderer = (TileEntitySpecialRenderer) renderClass.newInstance();
		}
		catch (ClassNotFoundException e)
		{
			// From DarkGuardsman: Ignore these types of exceptions as this just means the block has
			// no renderer for the tile given
		}
		catch (Exception e)
		{
			References.LOGGER.entry("[ClientContentRegistry] Failed to register TileEntity renderer for " + name);
			e.printStackTrace();

		}

		if (tileRenderer != null)
		{
			ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tileRenderer);
		}
	}

	@Override
	public void registerDummyRenderer(Class<? extends TileEntity> clazz)
	{
		if (!TileEntityRendererDispatcher.instance.mapSpecialRenderers.containsKey(clazz))
		{
			ClientRegistry.bindTileEntitySpecialRenderer(clazz, new RenderTileDummy());
		}
	}
}
