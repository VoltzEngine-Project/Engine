package net.minecraft.src.universalelectricity.api;

import net.minecraft.src.ItemStack;

public class UERecipe
{
	public ItemStack output;
	public Object[] input;
	
	public UERecipe(ItemStack output, Object[] input)
	{
		this.output = output;
		this.input = input;
	}
}
