package calclavia.lib.prefab.item;

import calclavia.lib.utility.LanguageUtility;
import net.minecraft.client.resources.Language;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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
		if (LanguageUtility.getLocal(this.getUnlocalizedName() + "." + itemstack.getItemDamage()) != "")
			return this.getUnlocalizedName() + "." + itemstack.getItemDamage();
		return this.getUnlocalizedName();
	}
}
