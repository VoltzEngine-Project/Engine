package basiccomponents.item;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import basiccomponents.block.BlockBasicMachine;

public class ItemBlockBasicMachine extends ItemBlock
{
	public ItemBlockBasicMachine(int id)
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
	public String getItemNameIS(ItemStack itemstack)
	{
		int metadata = 0;

		if (itemstack.getItemDamage() >= BlockBasicMachine.ELECTRIC_FURNACE_METADATA)
		{
			metadata = 2;
		}
		else if (itemstack.getItemDamage() >= BlockBasicMachine.BATTERY_BOX_METADATA)
		{
			metadata = 1;
		}

		return Block.blocksList[this.getBlockID()].getBlockName() + "." + metadata;
	}

	@Override
	public String getItemName()
	{
		return Block.blocksList[this.getBlockID()].getBlockName() + ".0";
	}
}
