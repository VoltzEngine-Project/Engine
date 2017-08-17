package com.builtbroken.mc.seven.framework.json.recipe.crafting.shaped;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.recipe.item.grid.RecipeShapedOreLarge;
import com.builtbroken.mc.seven.framework.json.recipe.crafting.JsonCraftingRecipeData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Holds onto recipe data until it can be converted.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class JsonShapedRecipeData extends JsonCraftingRecipeData
{
    public final Object[] data;
    public final boolean largeGrid;

    private boolean broken = false;

    public JsonShapedRecipeData(IJsonProcessor processor, Object output, Object[] data, boolean largeGrid)
    {
        super(processor, output, data);
        this.data = data;
        this.largeGrid = largeGrid;
    }

    @Override
    public void convertData()
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
                Object out = convert(dataObject);
                if (out != null)
                {
                    data[i] = out;
                }
                else
                {
                    Engine.logger().error("JsonCraftingRecipeData: The item value of [" + dataObject + "] could not be parsed into a valid recipe item entry. Recipe -> " + this);
                    broken = true;
                }
            }
        }
    }

    protected Object convert(Object in)
    {
        return convertItemEntry(in);
    }

    @Override
    public IRecipe getRecipe()
    {
        if (!broken)
        {
            //Create recipe
            if (output instanceof Block)
            {
                if (largeGrid)
                {
                    return new RecipeShapedOreLarge((Block) output, data);
                }
                else
                {
                    return new ShapedOreRecipe((Block) output, data);
                }
            }
            else if (output instanceof Item)
            {
                if (largeGrid)
                {
                    return new RecipeShapedOreLarge((Item) output, data);
                }
                else
                {
                    return new ShapedOreRecipe((Item) output, data);
                }
            }
            else if (output instanceof ItemStack)
            {
                if (largeGrid)
                {
                    return new RecipeShapedOreLarge((ItemStack) output, data);
                }
                else
                {
                    return new ShapedOreRecipe((ItemStack) output, data);
                }
            }
            else
            {
                Engine.logger().error("The type of output value [" + output + "] could not be recognized for recipe creation. Recipe -> " + this);
            }
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "JsonShapedRecipeData[ out = " + output + ", data = " + data + "]";
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}
