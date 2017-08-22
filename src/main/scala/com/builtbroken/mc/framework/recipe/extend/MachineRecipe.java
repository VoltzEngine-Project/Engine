package com.builtbroken.mc.framework.recipe.extend;

import com.builtbroken.mc.api.recipe.IMachineRecipe;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.lib.data.item.ItemStackWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 1/10/2015.
 */
@SuppressWarnings("TypeParameterExplicitlyExtendsObject")
public abstract class MachineRecipe<O extends Object, I extends Object> implements IMachineRecipe<O, I>
{
    protected final String type;
    protected List<I> inputs = new ArrayList();

    public MachineRecipe(String type)
    {
        this.type = type;
    }

    @Override
    public String getType()
    {
        return type;
    }

    public MachineRecipe addInputOption(I input)
    {
        if (!inputs.contains(input))
        {
            inputs.add(input);
        }
        return this;
    }


    @Override
    public List<I> getValidInputs()
    {
        return inputs;
    }

    @Override
    public String toString()
    {
        String clazzName = getClass().getSimpleName();
        clazzName = clazzName.replaceFirst("MR", "MachineRecipe");
        return clazzName + "[" + getOutput() + "]";
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof IMachineRecipe)
        {
            IMachineRecipe other = (IMachineRecipe) object;
            if (other.getType() == getType())
            {
                if (isOutputEqual(other.getOutput()))
                {
                    return other.getValidInputs().equals(getValidInputs());
                }
            }
        }
        return false;
    }

    /**
     * Used by the equals method to compare outputs, allows for the recipe to
     * override the default output instance's equals method
     */
    public boolean isOutputEqual(Object out)
    {
        return out == getOutput();
    }


    /**
     * Checks if the object can be considere an Item & be wrapped to an ItemStack
     * @param object instanceof Block, Item, ItemStack, ItemStackWrapper
     * @return true if Block, Item, ItemStack, ItemStackWrapper
     */
    public boolean isItem(Object object)
    {
        return object instanceof Block || object instanceof Item || object instanceof ItemStack || object instanceof ItemStackWrapper;
    }

    /**
     * Wraps the object to an ItemStackWrapper
     * @param object needs to return true for isItem
     * @return ItemStackWrapper or null if the input is not valid
     */
    public ItemStackWrapper wrapToItemStack(Object object)
    {
        if(object instanceof ItemStackWrapper)
            return (ItemStackWrapper) object;

        ItemStack stack = MachineRecipeType.toItemStack(object);
        if(stack != null)
        {
            return new ItemStackWrapper(stack);
        }
        return null;
    }

    /**
     * Checks if the object can be considered a Fluid
     * @param object instanceof Fluid, FluidStack
     * @return true if Fluid or FluidStack
     */
    public boolean isFluid(Object object)
    {
        return object instanceof FluidStack || object instanceof Fluid;
    }

    /**
     * Converts the input to a fluid stack
     * @param object - FluidStack or Fluid
     * @return FluidStack
     */
    public FluidStack toFluidStack(Object object)
    {
        if(object instanceof FluidStack)
            return (FluidStack) object;
        if(object instanceof Fluid)
            return new FluidStack((Fluid) object, Fluid.BUCKET_VOLUME);
        return null;
    }
}
