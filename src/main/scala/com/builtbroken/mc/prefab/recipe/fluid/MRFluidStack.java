package com.builtbroken.mc.prefab.recipe.fluid;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.prefab.items.ItemStackList;
import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import com.builtbroken.mc.prefab.recipe.extend.MachineRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Dark on 6/21/2015.
 */
public class MRFluidStack extends MachineRecipe<FluidStack, ItemStackWrapper>
{
    public final FluidStack output;

    public MRFluidStack(String type, FluidStack output)
    {
        super(type);
        this.output = output;
        this.inputs = new ItemStackList();
    }

    @Override
    public FluidStack getOutput()
    {
        return output;
    }

    @Override
    public boolean shouldHandleRecipe(Object[] inputs)
    {
        return inputs != null && inputs.length == 1 && getValidInputs().contains(wrap(inputs[0]));
    }

    public ItemStackWrapper wrap(Object object)
    {
        if (object instanceof ItemStackWrapper)
            return (ItemStackWrapper) object;
        ItemStack stack = MachineRecipeType.toItemStack(object);
        if (stack != null)
        {
            return new ItemStackWrapper(stack);
        }
        return null;
    }

    @Override
    public FluidStack handleRecipe(Object[] inputs, float extraChance, float failureChance)
    {
        return output.copy();
    }

    public MRFluidStack addInputOption(ItemStack input)
    {
        if (!inputs.contains(input))
        {
            inputs.add(new ItemStackWrapper(input));
        }
        return this;
    }

    public MRFluidStack addInputOption(Block input)
    {
        ItemStackWrapper wrapper = new ItemStackWrapper(new ItemStack(input));
        wrapper.meta_compare = false;
        wrapper.nbt_compare = false;
        return (MRFluidStack) super.addInputOption(wrapper);
    }

    public MRFluidStack addInputOption(Item input)
    {
        ItemStackWrapper wrapper = new ItemStackWrapper(new ItemStack(input));
        wrapper.meta_compare = false;
        wrapper.nbt_compare = false;
        return (MRFluidStack) super.addInputOption(wrapper);
    }

    @Override
    public boolean isOutputEqual(Object out)
    {
        return output.equals(out);
    }
}
