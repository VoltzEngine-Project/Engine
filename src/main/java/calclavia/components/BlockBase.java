package calclavia.components;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBase extends Block
{
	public BlockBase(String name, int id)
	{
		super(CalclaviaCore.CONFIGURATION.getItem(name, id).getInt(id), Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(CalclaviaCore.PREFIX + name);
		this.setTextureName(CalclaviaCore.PREFIX + name);
		this.setHardness(2f);
	}
}
