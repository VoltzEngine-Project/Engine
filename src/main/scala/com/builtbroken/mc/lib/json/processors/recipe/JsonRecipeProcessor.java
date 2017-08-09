package com.builtbroken.mc.lib.json.processors.recipe;

import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.meta.MetaData;
import com.builtbroken.mc.lib.json.conversion.JsonConverterNBT;
import com.builtbroken.mc.lib.json.exceptions.JsonFormatException;
import com.builtbroken.mc.lib.json.imp.IJsonBlockSubProcessor;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Prefab for any processor that uses item/block based recipes
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public abstract class JsonRecipeProcessor<D extends IJsonGenObject> extends JsonProcessor<D> implements IJsonBlockSubProcessor
{
    @Override
    public boolean process(JsonElement element, List<IJsonGenObject> objects)
    {
        try
        {
            D data = process(null, element);
            if (data != null)
            {
                objects.add(data);
            }
        }
        catch (JsonFormatException e)
        {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Called to process a recipe
     *
     * @param out     - optional, output item - if provided will not require output from recipe json
     * @param element - data containing the recipe
     * @return recipe data
     */
    public abstract D process(final Object out, final JsonElement element) throws JsonFormatException;

    @Override
    public void process(BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {
        try
        {
            D object = process(block, element);
            if (object != null)
            {
                objectList.add(object);
            }
        }
        catch (JsonFormatException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process(MetaData data, BlockBase block, JsonElement element, List<IJsonGenObject> objectList)
    {
        try
        {
            D object = process(new ItemStack(block, 1, data.index), element);
            if (object != null)
            {
                objectList.add(object);
            }
        }
        catch (JsonFormatException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Object getItemFromJson(JsonElement element) throws JsonFormatException
    {
        if (element.isJsonObject())
        {
            return fromJson(element.getAsJsonObject());
        }
        else if (element.isJsonPrimitive())
        {
            return element.getAsString();
        }
        throw new JsonFormatException("Could not convert json element into item entry >> '" + element + "'");
    }

    public RecipeItemEntry fromJson(JsonObject itemStackObject) throws JsonFormatException
    {
        //Convert and check types
        ensureValuesExist(itemStackObject, "item", "meta");

        //Create entry
        RecipeItemEntry entry = new RecipeItemEntry();

        //Get required data
        entry.item = itemStackObject.get("item").getAsString();
        entry.damage = itemStackObject.get("meta").getAsString();

        //Load optional stacksize
        if (itemStackObject.has("count"))
        {
            entry.count = itemStackObject.getAsJsonPrimitive("count").getAsInt();
            if (entry.count < 0)
            {
                throw new JsonFormatException("Recipe output count must be above zero");
            }
            else if (entry.count > 64)
            {
                throw new JsonFormatException("Recipe output count must be below 64 as this is the max stacksize for this version of Minecraft.");
            }
        }

        //Load optional item data
        if (itemStackObject.has("nbt"))
        {
            entry.nbt = JsonConverterNBT.handle(itemStackObject.get("nbt"));
        }
        return entry;
    }

}
