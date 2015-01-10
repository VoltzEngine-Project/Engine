package com.builtbroken.mc.prefab.recipe;

import com.builtbroken.mc.api.recipe.IMachineRecipe;
import com.builtbroken.mc.api.recipe.IMachineRecipeHandler;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.api.recipe.RecipeRegisterResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robert on 1/9/2015.
 */
public abstract class MRHandler<O extends Object> implements IMachineRecipeHandler
{
    protected MachineRecipeType type;
    protected Map<Object, List<IMachineRecipe>> recipes = new HashMap();

    public MRHandler(MachineRecipeType type)
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
                if (!isValidOutput(recipe.getOutput()) || toOutputType(recipe.getOutput()) == null)
                {
                    return RecipeRegisterResult.INVALID_OUTPUT;
                }
                for (Object object : recipe.getValidInputs())
                {
                    if (!isValidInput(object) || getKeyFor(object) == null)
                        return RecipeRegisterResult.INVALID_INPUT;
                }

                RecipeRegisterResult result = registerRecipe(recipe);
                if (result == RecipeRegisterResult.REGISTERED)
                {
                    for (Object o : recipe.getValidInputs())
                    {
                        Object object = getKeyFor(o);
                        List<IMachineRecipe> list = null;
                        if (recipes.containsKey(object))
                            list = recipes.get(object);
                        if (list == null)
                            list = new ArrayList();
                        list.add(recipe);
                        recipes.put(object, list);
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
    public Object getKeyFor(Object object)
    {
        return object;
    }

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
            List<IMachineRecipe> handlers = null;
            if (items.length == 1 && recipes.containsKey(getKeyFor(items[0])))
            {
                handlers = recipes.get(getKeyFor(items[0]));
            }
            else if (recipes.containsKey(getKeyFor(items)))
            {
                handlers = recipes.get(getKeyFor(items));
            }
            if (handlers != null && handlers.size() > 0)
            {
                for (IMachineRecipe handler : handlers)
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
        }
        return null;
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
