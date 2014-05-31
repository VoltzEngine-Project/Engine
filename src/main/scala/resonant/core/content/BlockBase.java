package resonant.core.content;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import resonant.lib.References;
import resonant.lib.content.module.TileBlock;
import resonant.lib.prefab.block.BlockAdvanced;

public class BlockBase extends BlockAdvanced
{
    public BlockBase(String name, Material mat)
    {
        super(mat);
		this.setBlockName(name);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2f);
    }
}
