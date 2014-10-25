package resonant.content.factory.resources.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import resonant.engine.ResonantEngine;
import resonant.lib.utility.LanguageUtility;

import java.util.List;

/**
 * An item used for auto-generated dusts based on registered ingots in the OreDict.
 *
 * @author Calclavia
 */
public class ItemResourceDust extends Item
{
	private boolean dirt = false;
	private boolean dust = true;

	public ItemResourceDust()
	{
		this(false, false);
	}

	public ItemResourceDust(boolean dirt, boolean dust)
	{
		this(dirt);
		this.dust = dust;
	}

	public ItemResourceDust(boolean dirt)
	{
		this.dirt = dirt;
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	public static String getMaterialFromStack(ItemStack itemStack)
	{
		return ResonantEngine.resourceFactory.getName(itemStack);
	}

	@Override
	public String getItemStackDisplayName(ItemStack is)
	{
		String material = getMaterialFromStack(is);

		if (material != null)
		{
			List<ItemStack> list = OreDictionary.getOres("ingot" + material.substring(0, 1).toUpperCase() + material.substring(1));

			if (list.size() > 0)
			{
				ItemStack type = list.get(0);

				String name = type.getDisplayName().replace(LanguageUtility.getLocal("misc.resonantinduction.ingot"), "").replaceAll("^ ", "").replaceAll(" $", "");
				return (LanguageUtility.getLocal(getUnlocalizedName() + ".name")).replace("%v", name).replace("  ", " ");
			}
		}

		return "";
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		java.util.Iterator<String> it = ResonantEngine.resourceFactory.materials.iterator();
		while (it.hasNext())
		{
			par3List.add(it.next());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int par2)
	{
		return ResonantEngine.resourceFactory.getColor(ItemResourceDust.getMaterialFromStack(itemStack));
	}
}
