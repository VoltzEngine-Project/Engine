package com.builtbroken.mc.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Dark on 6/23/2015.
 */
public interface ICastingRecipe extends IMachineRecipe<ItemStack, FluidStack>
{
    String getCastType();
}
