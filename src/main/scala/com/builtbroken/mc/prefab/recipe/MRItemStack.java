package com.builtbroken.mc.prefab.recipe;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.lib.helper.MathUtility;
import net.minecraft.item.ItemStack;

/**
 * Created by robert on 1/10/2015.
 */
public class MRItemStack extends MachineRecipe<ItemStack, ItemStack>
{
    public final ItemStack output;

    public MRItemStack(MachineRecipeType type, ItemStack output)
    {
        super(type);
        this.output = output;
    }

    @Override
    public ItemStack getOutput()
    {
        return output;
    }

    @Override
    public boolean shouldHandleRecipe(Object[] inputs)
    {
        return inputs != null && inputs.length == 1 && getValidInputs().contains(MachineRecipeType.toItemStack(inputs[0]));
    }

    @Override
    public ItemStack handleRecipe(Object[] inputs, float extraChance, float failureChance)
    {
        return output.copy();
    }
}
