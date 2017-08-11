package com.builtbroken.mc.framework.json.processors.recipe;

import com.builtbroken.mc.framework.json.imp.IJSONMetaConvert;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Uses to store an item parsed from JSON data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/9/2017.
 */
public class JsonItemEntry
{
    /** Reference to an item or block */
    public String item;
    /** Reference to a damage value or state */
    public String damage;
    /** Stack size */
    public int count;
    /** Extra item data */
    public NBTTagCompound nbt;

    public ItemStack get()
    {
        if (item != null && !item.isEmpty())
        {
            int meta = -1;
            if (damage != null && !damage.isEmpty())
            {
                try
                {
                    meta = Integer.parseInt(damage);
                    if (meta <= 0)
                    {
                        throw new IllegalArgumentException("RecipeItemEntry: damage value must be zero or more.");
                    }
                    else if (meta >= 32000)
                    {
                        throw new IllegalArgumentException("RecipeItemEntry: damage value must be less than 32,000.");
                    }
                }
                catch (NumberFormatException e)
                {
                    meta = -2;
                }
            }

            Object object = null;
            if (item.contains("@"))
            {
                if (item.startsWith("ore"))
                {
                    throw new IllegalArgumentException("RecipeItemEntry: does not support ore dictionary values");
                }
                else if (item.startsWith("item@"))
                {
                    String id = item.substring(5, item.length());
                    object = Item.itemRegistry.getObject(id);
                }
                else if (item.startsWith("block@"))
                {
                    String id = item.substring(6, item.length());
                    object = Block.blockRegistry.getObject(id);
                    if (object == Blocks.air)
                    {
                        return null;
                    }
                }
            }
            else
            {
                object = Item.itemRegistry.getObject(item);
                if (object == null)
                {
                    object = Block.blockRegistry.getObject(item);
                }
            }

            if (object != null)
            {
                if (meta == -2)
                {
                    if (object instanceof IJSONMetaConvert)
                    {
                        meta = ((IJSONMetaConvert) object).getMetaForValue(damage);
                        if (meta == -1)
                        {
                            return null;
                        }
                    }
                    else
                    {
                        //TODO add listener support
                        throw new IllegalArgumentException("RecipeItemEntry: item is not an instanceof IJSONMetaConvert. " +
                                "Due to this damage value could not be converted. This could either mean the system is not " +
                                "supported or the value entered could not be parsed as a number. If the last is the case fix " +
                                "the JSON. If the first is the case then ask for support");
                    }
                }

                //Create stack
                ItemStack stack;
                if (object instanceof Item)
                {
                    stack = new ItemStack((Item) object, count, meta == -1 ? 0 : meta);
                }
                else
                {
                    stack = new ItemStack((Block) object, count, meta == -1 ? 0 : meta);
                }

                //Encode NBT
                if (nbt != null && !nbt.hasNoTags())
                {
                    stack.setTagCompound(nbt);
                }
                return stack;
            }
        }
        return null;
    }
}
