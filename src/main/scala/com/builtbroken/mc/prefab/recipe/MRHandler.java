package com.builtbroken.mc.prefab.recipe;

import com.builtbroken.mc.api.recipe.IMachineRecipe;
import com.builtbroken.mc.api.recipe.IMachineRecipeHandler;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.api.recipe.RecipeRegisterResult;

import java.util.*;

/**
 * Created by robert on 1/9/2015.
 */
public abstract class MRHandler<O extends Object, K extends Object> implements IMachineRecipeHandler
{
    public final String type;
    public Map<K, List<IMachineRecipe>> recipes = new HashMap();

    public MRHandler(String type)
    {
        this.type = type;
    }

    @Override
    public final RecipeRegisterResult registerRecipe(IMachineRecipe recipe)
    {
        if (recipe != null)
        {
            if (recipe.getType() != type)
            {
                return RecipeRegisterResult.INVALID_TYPE;
            }
            else if (recipe.getValidInputs() != null && recipe.getOutput() != null)
            {
                if (!isValidOutput(recipe.getOutput()))
                {
                    return RecipeRegisterResult.INVALID_OUTPUT;
                }
                for (Object object : recipe.getValidInputs())
                {
                    if (!isValidInput(object))
                        return RecipeRegisterResult.INVALID_INPUT;
                }

                RecipeRegisterResult result = isValidRecipe(recipe);
                if (result == RecipeRegisterResult.REGISTERED)
                {
                    for (Object o : recipe.getValidInputs())
                    {
                        K key = getKeyFor(o);
                        List<IMachineRecipe> list = null;
                        if (recipes.containsKey(key))
                            list = recipes.get(key);
                        if (list == null)
                            list = new ArrayList();
                        list.add(recipe);
                        recipes.put(key, list);
                    }
                }
                return result;
            }
            else
            {
                return RecipeRegisterResult.INCOMPLETE;
            }
        }
        return RecipeRegisterResult.FAILED;
    }

    /**
     * Gets the key that is used to refer to the input
     * of the recipe
     */
    public abstract K getKeyFor(Object object);

    protected abstract boolean isValidInput(Object object);

    protected abstract boolean isValidOutput(Object object);

    protected RecipeRegisterResult isValidRecipe(IMachineRecipe recipe)
    {
        return RecipeRegisterResult.REGISTERED;
    }

    protected abstract O toOutputType(Object result);

    @Override
    public O getRecipe(Object[] items, float extraChance, float failureChance)
    {
        if (items != null)
        {
            for (IMachineRecipe handler : getRecipes(items))
            {
                if (handler.shouldHandleRecipe(items))
                {
                    Object result = handler.handleRecipe(items, extraChance, failureChance);
                    if (result != null)
                    {
                        return toOutputType(result);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public final Collection<IMachineRecipe> getRecipes(Object[] items)
    {
        return getRecipes(getKeyFor(items));
    }

    public List<IMachineRecipe> getRecipes(K key)
    {
        if(key != null)
        {
            if (recipes.containsKey(key))
            {
                List<IMachineRecipe> handlers = recipes.get(key);
                if(handlers != null)
                    return handlers;
            }
        }
        return new ArrayList();
    }

    @Override
    public List<IMachineRecipe> getRecipes()
    {
        List<IMachineRecipe> list = new ArrayList();
        for (List<IMachineRecipe> l : recipes.values())
        {
            list.addAll(l);
        }
        return list;
    }
}
