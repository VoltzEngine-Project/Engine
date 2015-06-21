package com.builtbroken.mc.prefab.recipe.extend;

import com.builtbroken.mc.api.recipe.IMachineRecipe;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.api.recipe.RecipeRegisterResult;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.prefab.recipe.item.MRItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 1/10/2015.
 */
public class MachineRecipeLoader<I extends IMachineRecipe> extends AbstractLoadable
{
    protected final String type;

    public MachineRecipeLoader(String type)
    {
        this.type = type;
    }

    @Override
    public final void init()
    {
        List<I> recipes = new ArrayList();
        generateRecipes(recipes);
        for(I recipe : recipes)
        {
            RecipeRegisterResult result = MachineRecipeType.getHandler(type).registerRecipe(recipe);
            if(result != RecipeRegisterResult.REGISTERED)
            {
                References.LOGGER.warn("" + this.getClass().getSimpleName() +" failed to register recipe " + recipe);
            }
        }
    }

    protected void generateRecipes(List<I> recipes)
    {
        //recipes.add(newRecipe(Blocks.cobblestone).addInputOption(Blocks.stone));
    }


}
