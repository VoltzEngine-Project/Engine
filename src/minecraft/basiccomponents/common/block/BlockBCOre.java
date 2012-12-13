package basiccomponents.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.UETab;
import basiccomponents.common.BasicComponents;

public class BlockBCOre extends Block
{
	public BlockBCOre(int id)
	{
		super(id, 14, Material.rock);
		this.setCreativeTab(UETab.INSTANCE);
		this.setBlockName("bcOre");
		this.setHardness(2f);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		return this.blockIndexInTexture + metadata;
	}

	@Override
	public int damageDropped(int metadata)
	{
		return metadata;
	}

	@Override
	public String getTextureFile()
	{
		return BasicComponents.BLOCK_TEXTURE_FILE;
	}

	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
	}
}
