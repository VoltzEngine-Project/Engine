package com.builtbroken.mc.lib.json.processors;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public class JsonGenData implements IJsonGenObject
{
    /** Person or group that created the object */
    public String author;

    /** Processor that created this object */
    public final IJsonProcessor processor;

    public JsonGenData(IJsonProcessor processor)
    {
        this.processor = processor;
    }

    @Override
    public void register()
    {

    }

    @Override
    public void setAuthor(String name)
    {
        author = name;
    }

    @Override
    public String getLoader()
    {
        return processor.getJsonKey();
    }

    @Override
    public String getMod()
    {
        return null;
    }

    /**
     * Used to convert string item entries
     * into output data that can be used in
     * recipes
     *
     * @param in
     * @return
     */
    public Object convertItemEntry(String in)
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
            return findItem(in.substring(5, in.length()));
        }
        else if (in.startsWith("block@"))
        {
            return findBlock(in.substring(6, in.length()));
        }
        else if (in.contains(":"))
        {
            Object out = findBlock(in);
            if (out == null)
            {
                out = findItem(in);
            }
            return out;
        }
        else if (OreDictionary.doesOreNameExist(in))
        {
            return in;
        }
        else
        {
            //TODO search everything and spam errors telling people to not use generic names
            //TODO add short hand look up for common items (cobble -> minecraft:cobblestone)
            Engine.logger().error("Could not match value of [" + in + "] to any data set for items, blocks, or ore names. Recipe -> " + this);
        }
        return null;
    }

    protected Object findBlock(String blockName)
    {
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

    protected Object findItem(String itemName)
    {
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


    /**
     * Called to convert the object to a usable stack
     *
     * @param object - object
     * @return stack or null if can't convert
     */
    public ItemStack toStack(Object object)
    {
        //Means the object has not been converted to usable data
        if (object instanceof String && (((String) object).contains("@") || ((String) object).contains(":")))
        {
            return toStack(convertItemEntry((String) object));
        }

        //Convert to itemstack
        if (object instanceof Item)
        {
            return new ItemStack((Item) object);
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
}
