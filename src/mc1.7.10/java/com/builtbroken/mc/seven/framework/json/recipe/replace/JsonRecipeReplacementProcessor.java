package com.builtbroken.mc.seven.framework.json.recipe.replace;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.framework.mod.loadable.AbstractLoadable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
public class JsonRecipeReplacementProcessor extends AbstractLoadable implements IJsonProcessor
{
    List<JsonRecipeReplacementData> replacementData = new ArrayList();

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "recipeRemoval";
    }

    @Override
    public String getLoadOrder()
    {
        return "after:" + References.JSON_ORENAME_KEY;
    }

    @Override
    public boolean canProcess(String key, JsonElement element)
    {
        return key.equalsIgnoreCase(getJsonKey());
    }

    @Override
    public boolean process(JsonElement element, List<IJsonGenObject> entries)
    {
        JsonObject jsonData = element.getAsJsonObject();

        //Ensure the basics exist
        JsonProcessor.ensureValuesExist(jsonData, "type", "item", "craftingType");

        String type = jsonData.getAsJsonPrimitive("type").getAsString();
        String craftingType = jsonData.getAsJsonPrimitive("craftingType").getAsString();
        String item = jsonData.getAsJsonPrimitive("item").getAsString();

        JsonRecipeReplacementData replacementDataEntry = new JsonRecipeReplacementData(this, item, craftingType, true);
        //Remove and replace call
        if ("replace".equalsIgnoreCase(type))
        {
            for (Map.Entry<String, JsonElement> entry : jsonData.entrySet())
            {
                //Ignore fields we use for the replacement data, only grab extra fields
                if (!entry.getKey().equalsIgnoreCase("type") && !entry.getKey().equalsIgnoreCase("item") && !entry.getKey().equalsIgnoreCase("craftingType"))
                {
                    replacementDataEntry.subProcessingData.put(entry.getKey(), entry.getValue());
                }
            }
        }
        else if (!"remove".equalsIgnoreCase(type))
        {
            throw new RuntimeException("Unknown replacement type '" + type + "'");
        }

        replacementData.add(replacementDataEntry);
        return true;
    }

    @Override
    public void loadComplete()
    {
        //Init replacement data
        Iterator it = replacementData.iterator();
        while (it.hasNext())
        {
            JsonRecipeReplacementData data = (JsonRecipeReplacementData) it.next();
            if (data.output == null)
            {
                data.output = data.convertItemEntry(data.outputValue);
            }
            if (data.output == null)
            {
                Engine.logger().error("JsonRecipeReplacementProcessor: Failed to locate item entry for '" + data.outputValue + "' ignoring value.");
                it.remove();
            }
        }

        //Loop recipes and remove entries
        it = CraftingManager.getInstance().getRecipeList().iterator();
        while (it.hasNext())
        {
            Object r = it.next();
            if (r instanceof IRecipe)
            {
                final ItemStack result = ((IRecipe) r).getRecipeOutput();
                if (result != null)
                {
                    //Loop replacement data checking for match
                    for (JsonRecipeReplacementData data : replacementData)
                    {
                        if ("grid".equalsIgnoreCase(data.craftingType) && data.doesMatchForReplacement(result))
                        {
                            Engine.logger().info("JsonRecipeReplacementProcessor: Removed recipe -> " + r);
                            //Remove and exit loop
                            it.remove();
                            break;
                        }
                    }
                }
                //Error to note broken recipe might exist
                else
                {
                    Engine.logger().error("JsonRecipeReplacementProcessor: Potential broken recipe with no output -> " + r);
                }
            }
        }

        //Loop to finalize data
        for (JsonRecipeReplacementData data : replacementData)
        {
            if (!data.subProcessingData.isEmpty())
            {
                final List<IJsonGenObject> objects = new ArrayList();
                for (Map.Entry<String, JsonElement> entry : data.subProcessingData.entrySet())
                {
                    JsonContentLoader.INSTANCE.process(entry.getKey(), entry.getValue(), objects);
                }
                JsonContentLoader.INSTANCE.handlePostCalls(objects);
            }
        }
    }
}
