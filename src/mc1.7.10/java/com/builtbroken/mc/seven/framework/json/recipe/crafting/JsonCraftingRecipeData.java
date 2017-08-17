package com.builtbroken.mc.seven.framework.json.recipe.crafting;

import com.builtbroken.mc.core.registry.implement.IRecipeContainer;
import com.builtbroken.mc.framework.json.data.JsonRecipeData;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

/**
 * Holds onto recipe data until it can be converted.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public abstract class JsonCraftingRecipeData extends JsonRecipeData implements IRecipeContainer
{
    public final Object[] data;

    public JsonCraftingRecipeData(IJsonProcessor processor, Object output, Object[] data)
    {
        super(processor, output);
        this.data = data;
    }

    @Override
    public void genRecipes(List<IRecipe> recipes)
    {
        //If output is a string convert
        if (canConvertToItem(output))
        {
            Object out = convertItemEntry(output);
            if (out != null)
            {
                output = out;
            }
        }

        convertData();

        IRecipe recipe = getRecipe();
        if (recipe != null)
        {
            recipes.add(recipe);
        }
    }

    /**
     * Called to convert input recipe data
     * into something usable
     */
    protected abstract void convertData();

    /**
     * Called to get the recipe
     *
     * @return
     */
    protected abstract IRecipe getRecipe();

    @Override
    public String toString()
    {
        return "JsonRecipeData[ out = " + output + ", data = " + data + "]";
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}
