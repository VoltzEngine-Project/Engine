package basiccomponents.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBCOre extends ItemBlock
{
	private String[] ores = { "Copper Ore", "Tin Ore" };

	public ItemBlockBCOre(int id)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack par1ItemStack)
	{
		return Block.blocksList[this.getBlockID()].getBlockName() + "." + (par1ItemStack.getItemDamage());
	}

	@Override
	public String getItemName()
	{
		return Block.blocksList[this.getBlockID()].getBlockName() + ".0";
	}
}
