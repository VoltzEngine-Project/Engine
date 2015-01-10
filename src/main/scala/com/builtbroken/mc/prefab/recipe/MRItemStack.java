package com.builtbroken.mc.prefab.recipe;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by robert on 1/10/2015.
 */
public class MRItemStack extends MachineRecipe<ItemStack, ItemStackWrapper>
{
    public final ItemStackWrapper output;

    public MRItemStack(MachineRecipeType type, ItemStackWrapper output)
    {
        super(type);
        this.output = output;
        this.inputs = new ItemStackList();
    }

    public MRItemStack(MachineRecipeType type, ItemStack output)
    {
        this(type, new ItemStackWrapper(output));
    }

    public MRItemStack(MachineRecipeType type, Item output)
    {
        this(type, new ItemStackWrapper(output));
    }

    public MRItemStack(MachineRecipeType type, Item output, int meta)
    {
        this(type, new ItemStack(output, meta));
    }

    public MRItemStack(MachineRecipeType type, Block output)
    {
        this(type, new ItemStackWrapper(output));
    }

    public MRItemStack(MachineRecipeType type, Block output, int meta)
    {
        this(type, new ItemStack(output, meta));
    }

    @Override
    public ItemStack getOutput()
    {
        return output.itemStack;
    }

    @Override
    public boolean shouldHandleRecipe(Object[] inputs)
    {
        return inputs != null && inputs.length == 1 && getValidInputs().contains(MachineRecipeType.toItemStack(inputs[0]));
    }

    @Override
    public ItemStack handleRecipe(Object[] inputs, float extraChance, float failureChance)
    {
        return output.itemStack.copy();
    }

    public MRItemStack addInputOption(ItemStack input)
    {
        if (!inputs.contains(input))
        {
            inputs.add(new ItemStackWrapper(input));
        }
        return this;
    }

    public MRItemStack addInputOption(Block input)
    {
        ItemStackWrapper wrapper = new ItemStackWrapper(new ItemStack(input));
        wrapper.meta_compare = false;
        wrapper.nbt_compare = false;
        return (MRItemStack) super.addInputOption(wrapper);
    }

    public MRItemStack addInputOption(Item input)
    {
        ItemStackWrapper wrapper = new ItemStackWrapper(new ItemStack(input));
        wrapper.meta_compare = false;
        wrapper.nbt_compare = false;
        return (MRItemStack) super.addInputOption(wrapper);
    }

    @Override
    public boolean isOutputEqual(Object out)
    {
        return output.equals(out);
    }
}
