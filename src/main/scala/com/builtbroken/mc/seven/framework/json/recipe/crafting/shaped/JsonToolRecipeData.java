package com.builtbroken.mc.seven.framework.json.recipe.crafting.shaped;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.recipe.item.RecipeTool;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/**
 * Holds onto recipe data until it can be converted.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class JsonToolRecipeData extends JsonShapedRecipeData
{
    public JsonToolRecipeData(IJsonProcessor processor, Object output, Object[] data, boolean largeGrid)
    {
        super(processor, output, data, largeGrid);
    }

    @Override
    public IRecipe getRecipe()
    {
        //Create recipe
        if (output instanceof Block)
        {
            return new RecipeTool((Block) output, data);
        }
        else if (output instanceof Item)
        {
            return new RecipeTool((Item) output, data);
        }
        else if (output instanceof ItemStack)
        {
            return new RecipeTool((ItemStack) output, data);
        }
        else
        {
            Engine.logger().error("The type of output value [" + output + "] could not be recognized for recipe creation. Recipe -> " + this);
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "JsonToolRecipeData[ out = " + output + ", data = " + data + "]";
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}
