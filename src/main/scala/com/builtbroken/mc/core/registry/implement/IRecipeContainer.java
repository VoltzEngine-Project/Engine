package com.builtbroken.mc.core.registry.implement;

import net.minecraft.item.crafting.IRecipe;

import java.util.List;

/**
 * Applied to objects that when registered provided recipes in the same class. Should be used
 * in place of registering recipes directly in {@link IPostInit} to allow for error checking.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
@Deprecated
public interface IRecipeContainer
{
    /**
     * Called to populate recipes into the map.
     * Does some error checking to ensure the recipe
     * is valid.
     * <p>
     * If in development mode and the recipe
     * is invalid an error will be produced.
     * <p>
     * If a recipe can be improved a warning
     * will be produced to help improve
     * the recipe. Such as, implementing
     * ore dictionary references in place
     * of using static references.
     * <p>
     * Called during PostInit phase of mod loading.
     *
     * @param recipes - list of recipes
     */
    void genRecipes(final List<IRecipe> recipes);
}
