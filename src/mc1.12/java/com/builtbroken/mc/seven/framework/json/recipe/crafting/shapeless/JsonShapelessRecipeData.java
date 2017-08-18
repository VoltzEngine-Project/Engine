package com.builtbroken.mc.seven.framework.json.recipe.crafting.shapeless;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.seven.framework.json.recipe.crafting.JsonCraftingRecipeData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Holds onto recipe data until it can be converted.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class JsonShapelessRecipeData extends JsonCraftingRecipeData
{
    public final Object[] data;
    public final boolean largeGrid;

    public JsonShapelessRecipeData(IJsonProcessor processor, Object output, Object[] data, boolean largeGrid)
    {
        super(processor, output, data);
        this.data = data;
        this.largeGrid = largeGrid;
    }

    @Override
    protected void convertData()
    {

        //Shapeless is an array of string data
        for (int i = 0; i < data.length; i++)
        {
            Object dataObject = data[i];
            if (canConvertToItem(dataObject))
            {
                //Convert entries to correct outputs
                Object out = convertItemEntry(dataObject);
                if (out != null)
                {
                    data[i] = out;
                }
                else
                {
                    Engine.logger().error("The item value of [" + dataObject + "] could not be parsed into a valid recipe item entry. Recipe -> " + this);
                    return;
                }
            }
            else
            {
                Engine.logger().error("The item value of [" + dataObject + "] is not a valid string for parsing. Recipe -> " + this);
                return;
            }
        }
    }

    @Override
    protected IRecipe getRecipe()
    {
        //Create recipe
        if (output instanceof Block)
        {
            return new ShapelessOreRecipe((Block) output, data);
        }
        else if (output instanceof Item)
        {
            return  new ShapelessOreRecipe((Item) output, data);
        }
        else if (output instanceof ItemStack)
        {
            return new ShapelessOreRecipe((ItemStack) output, data);
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
        return "JsonShapelessRecipeData[ out = " + output + ", data = " + data + "]";
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}
