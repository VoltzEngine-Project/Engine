package resonant.content;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import resonant.content.prefab.itemblock.ItemBlockTooltip;

public class CommonRegistryProxy
{
	public void registerTileEntity(String name, Class<? extends TileEntity> clazz)
	{
		GameRegistry.registerTileEntityWithAlternatives(clazz, name, "CL" + name);
	}

	public void registerDummyRenderer(Class<? extends TileEntity> clazz)
	{

	}
}
