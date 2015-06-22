package com.builtbroken.mc.prefab.recipe.fluid;

import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import com.builtbroken.mc.prefab.recipe.extend.MRHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Dark on 6/21/2015.
 */
public class MRHandlerFluidStack extends MRHandler<FluidStack, ItemStackWrapper>
{
    public MRHandlerFluidStack(String type)
    {
        super(type);
    }

    @Override
    protected boolean isValidInput(Object object)
    {
        return getKeyFor(object) != null;
    }

    @Override
    protected boolean isValidOutput(Object object)
    {
        return toOutputType(object) != null;
    }

    @Override
    public ItemStackWrapper getKeyFor(Object i)
    {
        Object input = i;
        if (input instanceof Object[])
        {
            input = ((Object[]) input)[0];
        }
        if (input instanceof ItemStackWrapper)
        {
            return (ItemStackWrapper) input;
        }
        else if (input instanceof Block)
        {
            return new ItemStackWrapper((Block) input);
        }
        else if (input instanceof Item)
        {
            return new ItemStackWrapper((Item) input);
        }
        else if (input instanceof ItemStack)
        {
            return new ItemStackWrapper((ItemStack) input);
        }
        return null;
    }

    @Override
    protected FluidStack toOutputType(Object result)
    {
        if (result instanceof FluidStack)
        {
            return (FluidStack) result;
        }
        else if (result instanceof Fluid)
        {
            return new FluidStack((Fluid) result, FluidContainerRegistry.BUCKET_VOLUME);
        }
        return null;
    }
}
