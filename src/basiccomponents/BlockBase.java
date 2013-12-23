package basiccomponents;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBase extends Block
{
	public BlockBase(String name, int id)
	{
		super(BasicComponents.CONFIGURATION.getItem(name, id).getInt(id), Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(BasicComponents.PREFIX + name);
		this.setTextureName(BasicComponents.PREFIX + name);
		this.setHardness(2f);
	}
}
