package calclavia.lib.content;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import calclavia.lib.Calclavia;

import com.builtbroken.common.Pair;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientRegistryProxy extends CommonRegistryProxy
{
	@Override
	public void registerBlock(Block block, Class<? extends ItemBlock> itemClass, String name, String modID)
	{
		super.registerBlock(block, itemClass, name, modID);

		if (block instanceof IBlockInfo)
		{
			List<Pair<Class<? extends TileEntity>, TileEntitySpecialRenderer>> set = new ArrayList<Pair<Class<? extends TileEntity>, TileEntitySpecialRenderer>>();
			((IBlockInfo) block).getClientTileEntityRenderers(set);
			for (Pair<Class<? extends TileEntity>, TileEntitySpecialRenderer> par : set)
			{
				ClientRegistry.bindTileEntitySpecialRenderer(par.left(), par.right());
			}
		}
	}

	@Override
	public void regiserTileEntity(String name, Class<? extends TileEntity> tileClass)
	{
		super.regiserTileEntity(name, tileClass);

		TileEntitySpecialRenderer tileRenderer = null;

		try
		{

			String rendererName = tileClass.getName().replaceFirst("Tile", "Render");
			Class renderClass = Class.forName(rendererName);
			tileRenderer = (TileEntitySpecialRenderer) renderClass.newInstance();
		}
		catch (Exception e)
		{
			Calclavia.LOGGER.severe("Failed to register TileEntity renderer for " + name);
		}

		if (tileRenderer != null)
		{
			ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tileRenderer);
		}
	}
}
