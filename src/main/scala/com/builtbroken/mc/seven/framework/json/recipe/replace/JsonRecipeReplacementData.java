package com.builtbroken.mc.seven.framework.json.recipe.replace;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
public class JsonRecipeReplacementData extends JsonGenData
{
    /** Toggle to clear all recipes, currently always true */
    public boolean remove_all;
    /** String value of the recipe output to match for removal */
    public String outputValue;
    /** Type of crafting recipe to remove */
    public String craftingType;
    /** Object value for recipe output to match for removal */
    public Object output;
    /** Json data that will need processed after recipe removal is finished */
    public HashMap<String, JsonElement> subProcessingData = new HashMap();

    public JsonRecipeReplacementData(IJsonProcessor processor, String output, String type, boolean remove_all)
    {
        super(processor);
        this.outputValue = output;
        this.craftingType = type;
        this.remove_all = remove_all;
    }

    /**
     * Called to check if the recipe result matches the {@link #outputValue}
     *
     * @param result - recipe output
     * @return true if matches
     */
    public boolean doesMatchForReplacement(ItemStack result)
    {
        if (output instanceof Block && result.getItem() instanceof ItemBlock)
        {
            return Item.getItemFromBlock((Block) output) == result.getItem();
        }
        else if (output instanceof Item)
        {
            return output == result.getItem();
        }
        else if (output instanceof ItemStack)
        {
            return InventoryUtility.stacksMatch(result, (ItemStack) output);
        }
        //Ore name
        else if (output instanceof String)
        {
            int[] ids = OreDictionary.getOreIDs(result);
            for (int i = 0; i < ids.length; i++)
            {
                String oreName = OreDictionary.getOreName(ids[i]);
                if (oreName.equals((String) output))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}
