package com.builtbroken.mc.api.recipe;

import java.util.List;

/** Handles recipe for a series of machines that share the same recipe list.
 * For example grinding ore into dust is always about the same process
 * Created by robert on 1/9/2015.
 */
public interface IMachineRecipeHandler<O extends Object>
{
    /**
     * Registers a recipe to this handler
     * @return true if the recipe was registered
     */
    public RecipeRegisterResult registerRecipe(IMachineRecipe recipe);

    /**
     * Called to get a recipe for the list of inputs. Extra and failure
     * chance are up to you to handle. This includes what to return and
     * how many of each to return.
     *
     * @param items - Array of input data, Most of the
     *              time it will be ItemStacks, or FluidStacks
     * @param extraChance - (0.0 - 1.0) chance for the recipe to output more
     *                    then one run worth. Is optional and not
     *                    supported by all machines.
     * @param failureChance - (0.0 - 1.0) chance for the recipe to fail and output
     *                      something other than expected. Also optional
     *                      and not supported by all machines.
     *
     * @return outputs for a valid recipe, or null for anything else
     */
    public O getRecipe(Object[] items, float extraChance, float failureChance);

    /** Gets all recipes registered to this machine type */
    public List<IMachineRecipe> getRecipes();
}
