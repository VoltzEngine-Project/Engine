package basiccomponents.item;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemBlockOre extends ItemBlock
{
	private String[] ores =
	{ "Copper Ore", "Tin Ore" };

	public ItemBlockOre(int id)
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
		return (new StringBuilder()).append(super.getItemName()).append(".").append(this.ores[itemstack.getItemDamage()]).toString();
	}
}
