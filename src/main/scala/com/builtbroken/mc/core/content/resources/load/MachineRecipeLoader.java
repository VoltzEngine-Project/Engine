package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.api.recipe.RecipeRegisterResult;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.prefab.recipe.MRItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 1/10/2015.
 */
public class MachineRecipeLoader extends AbstractLoadable
{
    protected final MachineRecipeType type;

    public MachineRecipeLoader(MachineRecipeType type)
    {
        this.type = type;
    }

    @Override
    public final void init()
    {
        List<MRItemStack> recipes = new ArrayList();
        generateRecipes(recipes);
        for(MRItemStack recipe : recipes)
        {
            RecipeRegisterResult result = type.registerRecipe(recipe);
            if(result != RecipeRegisterResult.REGISTERED)
            {
                References.LOGGER.warn("" + this.getClass().getSimpleName() +" failed to register recipe " + recipe);
            }
        }
    }

    protected void generateRecipes(List<MRItemStack> recipes)
    {
        //recipes.add(newRecipe(Blocks.cobblestone).addInputOption(Blocks.stone));
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
