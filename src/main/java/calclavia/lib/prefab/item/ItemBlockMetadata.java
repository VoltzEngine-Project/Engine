package calclavia.lib.prefab.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import calclavia.lib.utility.LanguageUtility;

public class ItemBlockMetadata extends ItemBlock
{
	public ItemBlockMetadata(int id)
	{
		super(id);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		String localized = LanguageUtility.getLocal(getUnlocalizedName() + "." + itemstack.getItemDamage() + ".name");
		if (localized != null && !localized.isEmpty())
			return getUnlocalizedName() + "." + itemstack.getItemDamage();
		return getUnlocalizedName();
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer par2EntityPlayer, List info, boolean par4)
	{
		String tooltip = LanguageUtility.getLocal(getUnlocalizedName(itemstack) + ".tooltip");

		if (tooltip != null && tooltip.length() > 0)
		{
			info.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
		}
	}
}
