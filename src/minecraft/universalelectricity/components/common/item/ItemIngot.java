package universalelectricity.components.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemIngot extends ItemBasic
{
	public static final String[] INGOTS = { "ingotCopper", "ingotTin", "ingotBronze", "ingotSteel" };

	public ItemIngot(int id)
	{
		super("ingot", id);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "item." + INGOTS[itemStack.getItemDamage()];
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
