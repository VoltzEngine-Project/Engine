package com.builtbroken.mc.prefab.recipe.item;

import com.builtbroken.mc.prefab.recipe.extend.MachineRecipeLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Dark on 6/21/2015.
 */
public class MRLoaderItemStack extends MachineRecipeLoader<MRItemStack>
{
    public MRLoaderItemStack(String type)
    {
        super(type);
    }

    protected MRItemStack newRecipe(Block output)
    {
        return new MRItemStack(type, new ItemStack(output));
    }

    protected MRItemStack newRecipe(Item output)
    {
        return new MRItemStack(type, new ItemStack(output));
    }

    protected MRItemStack newRecipe(ItemStack output)
    {
        return new MRItemStack(type, output);
    }
}
