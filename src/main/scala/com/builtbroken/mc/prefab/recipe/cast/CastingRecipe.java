package com.builtbroken.mc.prefab.recipe.cast;

import com.builtbroken.mc.api.items.ICastItem;
import com.builtbroken.mc.api.recipe.IMachineRecipe;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dark on 6/23/2015.
 */
public class CastingRecipe implements IMachineRecipe<ItemStack, FluidStack>
{
    protected final ItemStack output;
    protected final List<FluidStack> inputs;
    protected final String cast_type;

    public CastingRecipe(String cast_type, ItemStack output)
    {
        this.cast_type = cast_type;
        this.output = output;
        this.inputs = new ArrayList();
    }

    @Override
    public String getType()
    {
        return MachineRecipeType.FLUID_CAST.name();
    }

    @Override
    public Collection<FluidStack> getValidInputs()
    {
        return inputs;
    }

    @Override
    public ItemStack getOutput()
    {
        return output.copy();
    }

    @Override
    public boolean shouldHandleRecipe(Object[] inputs)
    {
        if(inputs != null)
        {
            if(inputs.length == 2)
            {
                if(!(inputs[0] instanceof ItemStack && ((ItemStack)inputs[0]).getItem() instanceof ICastItem))
                {
                    return false;
                }
                if(inputs[1] instanceof Fluid)
                {
                    return getValidInputs().contains(new FluidStack((Fluid)inputs[0], MRHandlerCast.INSTANCE.getVolumeForCast(cast_type)));
                }
                else if(inputs[1] instanceof FluidStack)
                {
                    FluidStack stack = ((FluidStack)inputs[1]).copy();
                    stack.amount = MRHandlerCast.INSTANCE.getVolumeForCast(cast_type);
                    return getValidInputs().contains(stack);
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack handleRecipe(Object[] inputs, float extraChance, float failureChance)
    {
        return getOutput();
    }
}
