package com.builtbroken.mc.lib.recipe.item;

import com.builtbroken.mc.lib.recipe.extend.MachineRecipeLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

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

    protected void processOreRecipes(List<MRItemStack> recipes, ItemStack output, String input)
    {
        List<ItemStack> oreStacks = OreDictionary.getOres(input);
        if (oreStacks != null && !oreStacks.isEmpty())
        {
            for (ItemStack stack : oreStacks)
            {
                if (stack != null && stack.getItem() != null)
                {
                    recipes.add(newRecipe(output).addInputOption(stack));
                }
            }
        }
    }
}
