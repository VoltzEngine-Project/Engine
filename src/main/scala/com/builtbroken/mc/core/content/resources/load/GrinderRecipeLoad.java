package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.prefab.recipe.MRItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by robert on 1/10/2015.
 */
public class GrinderRecipeLoad extends MachineRecipeLoader
{
    public GrinderRecipeLoad()
    {
        super(MachineRecipeType.ITEM_GRINDER);
    }

    @Override
    protected void generateRecipes(List<MRItemStack> recipes)
    {
        recipes.add(newRecipe(Blocks.sand).addInputOption(Blocks.stone).addInputOption(Blocks.cobblestone));
        recipes.add(newRecipe(new ItemStack(Blocks.sand, 4)).addInputOption(Blocks.sandstone));
    }
}
