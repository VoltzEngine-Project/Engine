package com.builtbroken.mc.prefab.recipe.extend;

import com.builtbroken.mc.api.recipe.IMachineRecipe;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.api.recipe.RecipeRegisterResult;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;

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
    public void init()
    {
        List<I> recipes = new ArrayList();
        generateRecipes(recipes);
        for(I recipe : recipes)
        {
            RecipeRegisterResult result = MachineRecipeType.getHandler(type).registerRecipe(recipe);
            if(result != RecipeRegisterResult.REGISTERED)
            {
                Engine.instance.logger().error("" + this.getClass().getSimpleName() +" failed to register recipe " + recipe + "  ErrorCode:"+ result);
            }
        }
    }

    protected void generateRecipes(List<I> recipes)
    {
        //recipes.add(newRecipe(Blocks.cobblestone).addInputOption(Blocks.stone));
    }


}
