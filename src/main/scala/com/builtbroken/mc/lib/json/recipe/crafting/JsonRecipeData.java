package com.builtbroken.mc.lib.json.recipe.crafting;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class JsonRecipeData implements IJsonGenObject, IPostInit
{
    public final Object output;
    public final Object[] data;
    public final boolean shaped;

    public JsonRecipeData(Object output, Object[] data, boolean shaped)
    {
        this.output = output;
        this.data = data;
        this.shaped = shaped;
    }

    @Override
    public void register()
    {

    }

    @Override
    public void onPostInit()
    {
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
                    Object out = convert((String) dataObject);
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
            }
            //TODO validate grid to ensure all items exist in items list
        }
        else
        {
            //Shapeless is an array of string data
            for (int i = 0; i < data.length; i++)
            {
                Object dataObject = data[i];
                if (dataObject instanceof String)
                {
                    //Convert entries to correct outputs
                    Object out = convert((String) dataObject);
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
    }

    /**
     * Used to convert string item entries
     * into output data that can be used in
     * recipes
     *
     * @param in
     * @return
     */
    public Object convert(String in)
    {
        if (in.startsWith("ore@"))
        {
            String oreName = in.substring(4, in.length());
            if (!OreDictionary.doesOreNameExist(oreName))
            {
                Engine.logger().error("The ore value of [" + oreName + "] is not register and will prevent the recipe from working. Recipe -> " + this);
            }
            return oreName;
        }
        else if (in.startsWith("item@"))
        {
            String itemName = in.substring(5, in.length());
            if (itemName.contains("@"))
            {
                String[] data = itemName.split("@");
                itemName = data[0];
                int meta = Integer.parseInt(data[1]);
                Item item = InventoryUtility.getItem(itemName);
                if (item == null)
                {
                    Engine.logger().error("The item value of [" + itemName + "] is not register and will prevent the recipe from working. Recipe -> " + this);
                    return null;
                }
                return new ItemStack(item, 1, meta);
            }
            return InventoryUtility.getItem(itemName);
        }
        else if (in.startsWith("block@"))
        {
            String blockName = in.substring(6, in.length());
            if (blockName.contains("@"))
            {
                String[] data = blockName.split("@");
                blockName = data[0];
                int meta = Integer.parseInt(data[1]);
                Block block = InventoryUtility.getBlock(blockName);
                if (block == null)
                {
                    Engine.logger().error("The block value of [" + blockName + "] is not register and will prevent the recipe from working. Recipe -> " + this);
                    return null;
                }
                return new ItemStack(block, 1, meta);
            }
            return InventoryUtility.getBlock(blockName);
        }
        else
        {
            //TODO search blocks, items, then ore
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "JsonRecipeData[ out = " + output + ", shaped = " + shaped + ", data = " + data + "]";
    }
}
