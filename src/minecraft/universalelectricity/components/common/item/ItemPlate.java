package universalelectricity.components.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemPlate extends ItemBasic
{
	public static final String[] PLATES = { "plateCopper", "plateTin", "plateBronze", "plateSteel", "plateIron", "plateGold" };

	public ItemPlate(int id)
	{
		super("plate", id, 32);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public String getItemNameIS(ItemStack itemStack)
	{
		return "item." + PLATES[itemStack.getItemDamage()];
	}

	public int getIconFromDamage(int metadata)
	{
		return this.iconIndex + metadata;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List list)
	{
		for (int i = 0; i < PLATES.length; i++)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}
}
