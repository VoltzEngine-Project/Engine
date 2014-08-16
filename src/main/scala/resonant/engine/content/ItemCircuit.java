package resonant.engine.content;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import resonant.engine.References;

import java.util.List;

public class ItemCircuit extends ItemBase
{
	public static final String[] TYPES = { "circuitBasic", "circuitAdvanced", "circuitElite" };

	public ItemCircuit(int texture)
	{
		super("circuit");
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "item." + References.PREFIX + TYPES[itemStack.getItemDamage()];
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list)
	{
		for (int i = 0; i < TYPES.length; i++)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}
}
