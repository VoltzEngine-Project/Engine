package com.builtbroken.mc.seven.framework.json.recipe.crafting.shaped;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.tool.ItemSheetMetalTools;
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
public class JsonSheetMetalRecipeData extends JsonToolRecipeData
{
    public JsonSheetMetalRecipeData(IJsonProcessor processor, Object output, Object[] data, boolean largeGrid)
    {
        super(processor, output, data, largeGrid);
    }

    @Override
    protected Object convert(Object in)
    {
        if(Engine.itemSheetMetalTools != null)
        {
            if(in instanceof String)
            {
                String value = (String) in;
                //TODO setup with registry containing several values per entry that uses an array as the return [value1, value2, value]
                if(value.equalsIgnoreCase("tool@hammer"))
                {
                    return ItemSheetMetalTools.getHammer();
                }
                else if(value.equalsIgnoreCase("tool@shear"))
                {
                    return ItemSheetMetalTools.getShears();
                }
            }
            return convertItemEntry(in);
        }
        return null; //Will trigger broken state
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
        return "JsonSheetMetalRecipeData[ out = " + output + ", data = " + data + "]";
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}
