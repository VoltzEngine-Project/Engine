package basiccomponents.item;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import basiccomponents.block.BlockBasicMachine;

public class ItemBasicMachine extends ItemBlock
{
	private String[] names =
	{ "Coal Generator", "Battery Box", "Electric Furnace" };

	public ItemBasicMachine(int id)
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

		return (new StringBuilder()).append(super.getItemName()).append(".").append(this.names[metadata]).toString();
	}
}
