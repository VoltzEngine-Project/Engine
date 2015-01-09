package com.builtbroken.mc.api.recipe;

import java.util.Objects;

/** Used to handle a more complex recipe than input -> output. For example adding NBT or processing
 * item's NBT. Can also be used for standard recipe and intercepting standard recipes.
 *
 * As for the generic you can default to Object, of Object[] if you want. The recipe handler if
 * created by the voltz engine will handle arrays,
 * Created by robert on 1/9/2015.
 */
public interface IMachineRecipe<O extends Object>
{
    /** Gets the input items, mainly used for NEI */
    public O getInput();

    /** Gets the output items, mainly used for NEI */
    public O getOutput();

    /**
     * Called to handle the recipe
     * @param inputs - input objects, make sure to fully check if the recipe machines
     * @param extraChance - chance to produce extra items, optional
     * @param failureChance - chance to fail, optional
     * @return result
     */
    public O handleRecipe(Object[] inputs, float extraChance, float failureChance);
}
