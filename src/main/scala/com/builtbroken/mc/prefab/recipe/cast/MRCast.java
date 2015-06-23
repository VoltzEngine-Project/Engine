package com.builtbroken.mc.prefab.recipe.cast;

import com.builtbroken.mc.prefab.recipe.extend.MachineRecipe;
import net.minecraft.item.ItemStack;

/**
 * Created by Dark on 6/23/2015.
 */
public class MRCast extends MachineRecipe<ItemStack, CastWrapper>
{
    protected ItemStack output;

    public MRCast(String type, ItemStack output)
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
        if (inputs != null && inputs.length == 2)
        {
            if (isItem(inputs[0]) && isFluid(inputs[1]))
                return true;
            if (isFluid(inputs[0]) && isItem(inputs[1]))
                return true;
            return false;
        }
        return false;
    }


    @Override
    public ItemStack handleRecipe(Object[] inputs, float extraChance, float failureChance)
    {
        return output.copy();
    }
}
