package universalelectricity.components.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemPlate extends ItemBasic
{
	public static final String[] PLATES = { "plateCopper", "plateTin", "plateBronze", "plateSteel", "plateIron", "plateGold" };

	public ItemPlate(int id)
	{
		super("plate", id);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "item." + PLATES[itemStack.getItemDamage()];
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
