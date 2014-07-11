package resonant.content;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import resonant.content.wrapper.RenderTileDummy;

public class ClientRegistryProxy extends CommonRegistryProxy
{
	@Override
	public void registerDummyRenderer(Class<? extends TileEntity> clazz)
	{
		if (!TileEntityRendererDispatcher.instance.mapSpecialRenderers.containsKey(clazz))
		{
			ClientRegistry.bindTileEntitySpecialRenderer(clazz, new RenderTileDummy());
		}
	}
}
