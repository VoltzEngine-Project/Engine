package com.builtbroken.mc.core.content;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.prefab.recipe.MRItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by robert on 1/10/2015.
 */
public class CrusherRecipeLoad extends MachineRecipeLoader
{
    public CrusherRecipeLoad()
    {
        super(MachineRecipeType.ITEM_GRINDER);
    }

    @Override
    protected void generateRecipes(List<MRItemStack> recipes)
    {
        recipes.add(newRecipe(Blocks.cobblestone).addInputOption(Blocks.stone));
        recipes.add(newRecipe(new ItemStack(Blocks.cobblestone, 4)).addInputOption(Blocks.stonebrick));
    }
}
