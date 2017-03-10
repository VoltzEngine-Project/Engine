package com.builtbroken.mc.lib.json.recipe;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

/**
 * Prefab for any recipe that has a single output
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public abstract class JsonRecipeData implements IJsonGenObject
{
    /** Output of the recipe */
    public Object output;
    private boolean convertedOutput = false;

    public JsonRecipeData(Object output)
    {
        this.output = output;
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

    /**
     * Output of the recipe
     *
     * @return
     */
    public ItemStack getOutput()
    {
        if (!convertedOutput)
        {
            convertedOutput = true;

            //Convert from string data
            if (output instanceof String)
            {
                output = convert((String) output);
            }
            ItemStack stack = toStack(output);
            if (stack != null)
            {
                output = stack;
            }
        }
        return output instanceof ItemStack ? (ItemStack) output : null;
    }

    public ItemStack toStack(Object object)
    {
        //Convert to itemstack
        if (object instanceof Item)
        {
            return new ItemStack((Block) object);
        }
        else if (object instanceof Block)
        {
            return new ItemStack((Block) object);
        }
        else if (object instanceof String)
        {
            String orename = (String) object;
            ArrayList<ItemStack> stacks = OreDictionary.getOres(orename);
            for (ItemStack stack : stacks)
            {
                if (stack != null && stack.getItem() != null)
                {
                    return stack;
                }
            }
        }
        return object instanceof ItemStack ? (ItemStack) object : null;
    }

    @Override
    public void register()
    {

    }

    @Override
    public String toString()
    {
        return "JsonRecipeData[" + output + "]@" + hashCode();
    }
}
