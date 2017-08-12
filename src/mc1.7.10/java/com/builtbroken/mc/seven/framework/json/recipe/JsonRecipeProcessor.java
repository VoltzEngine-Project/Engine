package com.builtbroken.mc.seven.framework.json.recipe;

import com.builtbroken.mc.framework.json.exceptions.JsonFormatException;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import com.builtbroken.mc.seven.framework.block.IJsonBlockSubProcessor;
import com.builtbroken.mc.seven.framework.block.meta.MetaData;
import com.google.gson.JsonElement;
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
}
