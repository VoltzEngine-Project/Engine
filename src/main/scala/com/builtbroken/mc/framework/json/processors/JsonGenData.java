package com.builtbroken.mc.framework.json.processors;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.debug.error.ExceptionErrorDebug;
import com.builtbroken.mc.debug.error.IErrorDebug;
import com.builtbroken.mc.framework.json.data.JsonItemEntry;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public abstract class JsonGenData implements IJsonGenObject
{
    /** Person or group that created the object */
    @JsonProcessorData("author")
    public String author;

    @JsonProcessorData(value = {"mod", "modID"})
    public String modID;

    /** Processor that created this object */
    public final IJsonProcessor processor;

    /** Internal check to note something broke during creation but didn't cause complete failure. aka safe to continue */
    protected boolean broken = false;
    /** List of errors/warning/problems that happened during creation */
    protected List<IErrorDebug> errors;

    public JsonGenData(IJsonProcessor processor)
    {
        this.processor = processor;
    }

    @Override
    public void onCreated()
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
        return processor != null ? processor.getJsonKey() : null;
    }

    @Override
    public String getMod()
    {
        return modID;
    }

    /**
     * Used to convert string item entries
     * into output data that can be used in
     * recipes
     *
     * @param object - data to convert
     * @return
     */
    public Object convertItemEntry(Object object)
    {
        if (object instanceof JsonItemEntry)
        {
            try
            {
                return ((JsonItemEntry) object).get();
            }
            catch (IllegalArgumentException e)
            {
                Engine.logger().error("Error in recipe data " + this, e);
            }
        }
        else if (object instanceof String)
        {
            String in = (String) object;
            if (in.startsWith("tool@"))
            {
                return in;
            }
            else if (in.startsWith("ore@"))
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
        }
        return null;
    }

    /**
     * Checks if the data type can be converted. Does not
     * look at the content of the data.
     *
     * @param object - data
     * @return true if can convert
     */
    protected boolean canConvertToItem(Object object)
    {
        return object instanceof String || object instanceof JsonItemEntry;
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
        if (object instanceof JsonItemEntry)
        {
            try
            {
                return ((JsonItemEntry) object).get();
            }
            catch (IllegalArgumentException e)
            {
                Engine.logger().error("Error in recipe data " + this, e);
            }
        }
        else if (object instanceof Item)
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
                    ItemStack copy = stack.copy();
                    copy.stackSize = 1;
                    return copy;
                }
            }
        }
        return object instanceof ItemStack ? (ItemStack) object : null;
    }

    protected void addError(String title, String message, Exception e)
    {
        if (errors == null)
        {
            errors = new ArrayList();
        }
        errors.add(new ExceptionErrorDebug(title, message, e));
        broken = true;
    }
}
