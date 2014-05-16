package resonant.core.content;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import resonant.core.ResonantEngine;
import resonant.lib.References;
import resonant.lib.prefab.block.BlockAdvanced;

public class BlockBase extends BlockAdvanced
{
    public BlockBase(String name, int id)
    {
        super(References.CONFIGURATION.getItem(name, id).getInt(id), Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setUnlocalizedName(References.PREFIX + name);
        this.setTextureName(References.PREFIX + name);
        this.setHardness(2f);
    }
}
