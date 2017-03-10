package com.builtbroken.mc.lib.json.recipe.smelting;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.block.BlockJson;
import com.builtbroken.mc.lib.json.block.meta.MetaData;
import com.builtbroken.mc.lib.json.imp.IJsonBlockSubProcessor;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Loads smelting recipes from a json for {@link net.minecraft.item.crafting.FurnaceRecipes}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonSmeltingProcessor extends JsonProcessor<JsonSmeltingRecipeData> implements IJsonBlockSubProcessor
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "furnaceRecipe";
    }

    @Override
    public String getLoadOrder()
    {
        return "after:item";
    }

    @Override
    public void process(JsonElement element, List<IJsonGenObject> objects)
    {
        JsonSmeltingRecipeData data = process(null, element);
        if (data != null)
        {
            objects.add(data);
        }
    }

    public JsonSmeltingRecipeData process(final Object out, final JsonElement element)
    {
        final JsonObject recipeData = element.getAsJsonObject();

        ensureValuesExist(recipeData, "input");

        Object output = out;
        if (output == null)
        {
            ensureValuesExist(recipeData, "output");
            output = recipeData.getAsJsonPrimitive("output").getAsString();
        }
        return null;
    }

    @Override
    public void process(BlockJson block, JsonElement element, List<IJsonGenObject> objectList)
    {
        process(block, element);
    }

    @Override
    public void process(MetaData data, BlockJson block, JsonElement element, List<IJsonGenObject> objectList)
    {
        process(new ItemStack(block, 1, data.index), element);
    }
}
