package calclavia.components;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import calclavia.lib.Calclavia;
import calclavia.lib.CalclaviaCoreLoader;

public class BlockBase extends Block
{
	public BlockBase(String name, int id)
	{
		super(Calclavia.CONFIGURATION.getItem(name, id).getInt(id), Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(CalclaviaCoreLoader.PREFIX + name);
		this.setTextureName(CalclaviaCoreLoader.PREFIX + name);
		this.setHardness(2f);
	}
}
