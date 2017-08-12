package com.builtbroken.mc.seven.framework.json.recipe.crafting;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.implement.IRecipeContainer;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.data.JsonRecipeData;
import com.builtbroken.mc.framework.recipe.item.grid.RecipeShapedOreLarge;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;

/**
 * Holds onto recipe data until it can be converted.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class JsonCraftingRecipeData extends JsonRecipeData implements IRecipeContainer
{
    public final Object[] data;
    public final boolean shaped;
    public final boolean largeGrid;

    public JsonCraftingRecipeData(IJsonProcessor processor, Object output, Object[] data, boolean shaped, boolean largeGrid)
    {
        super(processor, output);
        this.data = data;
        this.shaped = shaped;
        this.largeGrid = largeGrid;
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
        //else TODO make sure output exists and can be used

        if (shaped)
        {
            //Shaped starts with a series of strings representing the grid, then goes "char, item, char2, item2...."
            boolean convert = false;
            int gridEnd = 0;
            for (int i = 0; i < data.length; i++)
            {
                Object dataObject = data[i];
                //Do not convert until after the first char entry
                if (dataObject instanceof Character)
                {
                    if (gridEnd == 0)
                    {
                        gridEnd = i;
                    }
                    convert = true;
                }
                else if (convert)
                {
                    Object out = convertItemEntry(dataObject);
                    if (out != null)
                    {
                        data[i] = out;
                    }
                    else
                    {
                        Engine.logger().error("JsonCraftingRecipeData: The item value of [" + dataObject + "] could not be parsed into a valid recipe item entry. Recipe -> " + this);
                        return;
                    }
                }
            }
            //TODO validate grid to ensure all items exist in items list

            //Create recipe
            if (output instanceof Block)
            {
                if(largeGrid)
                {
                    recipes.add(new RecipeShapedOreLarge((Block) output, data));
                }
                else
                {
                    recipes.add(new ShapedOreRecipe((Block) output, data));
                }
            }
            else if (output instanceof Item)
            {
                if(largeGrid)
                {
                    recipes.add(new RecipeShapedOreLarge((Item) output, data));
                }
                else
                {
                    recipes.add(new ShapedOreRecipe((Item) output, data));
                }
            }
            else if (output instanceof ItemStack)
            {
                if(largeGrid)
                {
                    recipes.add(new RecipeShapedOreLarge((ItemStack) output, data));
                }
                else
                {
                    recipes.add(new ShapedOreRecipe((ItemStack) output, data));
                }
            }
            else
            {
                Engine.logger().error("The type of output value [" + output + "] could not be recognized for recipe creation. Recipe -> " + this);
            }
        }
        else
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

            //Create recipe
            if (output instanceof Block)
            {
                recipes.add(new ShapelessOreRecipe((Block) output, data));
            }
            else if (output instanceof Item)
            {
                recipes.add(new ShapelessOreRecipe((Item) output, data));
            }
            else if (output instanceof ItemStack)
            {
                recipes.add(new ShapelessOreRecipe((ItemStack) output, data));
            }
            else
            {
                Engine.logger().error("The type of output value [" + output + "] could not be recognized for recipe creation. Recipe -> " + this);
            }
        }
    }

    @Override
    public String toString()
    {
        return "JsonRecipeData[ out = " + output + ", shaped = " + shaped + ", data = " + data + "]";
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}
