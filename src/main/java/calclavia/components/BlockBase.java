package calclavia.components;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import calclavia.lib.Calclavia;

public class BlockBase extends Block
{
	public BlockBase(String name, int id)
	{
		super(Calclavia.CONFIGURATION.getItem(name, id).getInt(id), Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(CalclaviaLoader.PREFIX + name);
		this.setTextureName(CalclaviaLoader.PREFIX + name);
		this.setHardness(2f);
	}
}
