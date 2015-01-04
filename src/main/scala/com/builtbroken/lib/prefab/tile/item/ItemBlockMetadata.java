package com.builtbroken.lib.prefab.tile.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import com.builtbroken.lib.utility.LanguageUtility;

public class ItemBlockMetadata extends ItemBlockTooltip
{
	public ItemBlockMetadata(Block block)
	{
		super(block);
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
		{
			return getUnlocalizedName() + "." + itemstack.getItemDamage();
		}
		return getUnlocalizedName();
	}
}
