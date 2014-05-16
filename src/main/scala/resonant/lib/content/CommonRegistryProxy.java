package resonant.lib.content;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import resonant.lib.prefab.item.ItemBlockTooltip;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonRegistryProxy
{
    public void registerBlock(Block block, Class<? extends ItemBlock> itemClass, String name, String modID)
    {
        if (block != null && name != null)
        {
            GameRegistry.registerBlock(block, itemClass == null ? ItemBlockTooltip.class : itemClass, name, modID);
        }
    }

    public void registerTileEntity(String name, Class<? extends TileEntity> clazz)
    {
        GameRegistry.registerTileEntityWithAlternatives(clazz, name, "CL" + name);
    }

    public void registerDummyRenderer(Class<? extends TileEntity> clazz)
    {

    }
}
