package resonant.core.content;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import resonant.lib.References;
import resonant.lib.content.module.TileBlock;

public class BlockBase extends TileBlock
{
    public BlockBase(String name, Material mat)
    {
        super(name, mat);
        this.creativeTab(CreativeTabs.tabBlock);
        this.blockHardness(2f);
    }
}
