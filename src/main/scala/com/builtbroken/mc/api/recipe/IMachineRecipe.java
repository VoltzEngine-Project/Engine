package com.builtbroken.mc.api.recipe;

import java.util.Collection;

/**
 * Used to handle a more complex recipe than input -> output. For example adding NBT or processing
 * item's NBT. Can also be used for standard recipe and intercepting standard recipes.
 * <p/>
 * As for the generic you can default to Object, of Object[] if you want. The recipe handler if
 * created by the voltz engine will handle arrays if needed. Though at the end the generic is
 * mainly for your own use rather than ours. As it will not exist during run time or when
 * registered with the handler. So use it just to make your life simpler.
 * <p/>
 * Created by robert on 1/9/2015.
 */
public interface IMachineRecipe<O extends Object, I extends Object>
{
    /**
     * Type of recipe
     */
    public MachineRecipeType getType();

    /**
     * Gets the input items as simple as possible
     */
    public Collection<I> getValidInputs();

    /**
     * Gets the output items, mainly used for NEI
     */
    public O getOutput();

    /**
     * Called before processing the recipe, or if more than one handler exists for the input items
     */
    public boolean shouldHandleRecipe(Object[] inputs);

    /**
     * Called to handle the recipe
     *
     * @param inputs        - input objects, make sure to fully check if the recipe machines
     * @param extraChance   - chance to produce extra items, optional
     * @param failureChance - chance to fail, optional
     * @return result
     */
    public O handleRecipe(Object[] inputs, float extraChance, float failureChance);
}
