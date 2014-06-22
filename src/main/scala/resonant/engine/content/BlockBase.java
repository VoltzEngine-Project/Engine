package resonant.engine.content;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import resonant.content.wrapper.TileBlock;
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
