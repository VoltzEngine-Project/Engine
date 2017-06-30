package com.builtbroken.mc.lib.recipe.item;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import com.builtbroken.mc.lib.recipe.extend.MRHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by robert on 1/9/2015.
 */
public class MRHandlerItemStack extends MRHandler<ItemStack, ItemStackWrapper>
{
    public MRHandlerItemStack(String type)
    {
        super(type);
    }

    @Override
    protected boolean isValidInput(Object object)
    {
        return toOutputType(object) != null;
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
        if(input instanceof Object[])
        {
            input = ((Object[])input)[0];
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
        else if(input instanceof ItemStack)
        {
            return new ItemStackWrapper((ItemStack)input);
        }
        return null;
    }

    @Override
    protected ItemStack toOutputType(Object result)
    {
        if (result instanceof ItemStackWrapper)
        {
            return ((ItemStackWrapper) result).itemStack;
        }
        return MachineRecipeType.toItemStack(result);
    }
}
