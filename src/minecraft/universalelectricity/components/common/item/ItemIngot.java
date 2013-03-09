package universalelectricity.components.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemIngot extends ItemBasic
{
	public static final String[] INGOTS = { "ingotCopper", "ingotTin", "ingotBronze", "ingotSteel" };

	public ItemIngot(int id)
	{
		super("ingot", id, 2);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public String getItemNameIS(ItemStack itemStack)
	{
		return "item." + INGOTS[itemStack.getItemDamage()];
	}

	public int getIconFromDamage(int metadata)
	{
		return this.iconIndex + metadata;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List list)
	{
		for (int i = 0; i < INGOTS.length; i++)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}
}
