package com.builtbroken.mc.framework.json.processors.item.processor;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.item.ItemBase;
import com.builtbroken.mc.framework.item.logic.ItemNode;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.loading.JsonProcessorInjectionMap;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.builtbroken.mc.framework.json.processors.block.JsonBlockProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Loads basic item data from a processor
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonItemProcessor extends JsonProcessor<ItemBase>
{
    public static final String KEY = "item";

    protected final JsonProcessorInjectionMap itemPropDataHandler;

    public JsonItemProcessor()
    {
        super();
        itemPropDataHandler = new JsonProcessorInjectionMap(ItemNode.class);
    }

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return KEY;
    }

    @Override
    public String getLoadOrder()
    {
        return "after:" + JsonBlockProcessor.KEY;
    }

    @Override
    public boolean process(JsonElement element, List<IJsonGenObject> objectList)
    {
        debugPrinter.start("ItemProcessor", "Processing entry", Engine.runningAsDev);
        JsonObject itemData = element.getAsJsonObject();
        ensureValuesExist(itemData, "name");

        ItemBase item;
        if (itemData.has("itemClass"))
        {
            String className = itemData.get("itemClass").getAsString();
            debugPrinter.log("ItemClass: " + className);
            try
            {
                Class clazz = Class.forName(className);
                item = (ItemBase) clazz.newInstance();
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException("Failed to find item class for '" + className + "'", e);
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException("Failed to create item for class '" + className + "'", e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException("Failed to access item constructor for class '" + className + "'", e);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Unexpected error creating node from '" + className + "'", e);
            }
        }
        else if (itemData.has("nodeClass"))
        {
            String className = itemData.get("nodeClass").getAsString();
            debugPrinter.log("NodeClass: " + className);
            try
            {

                Class clazz = Class.forName(className);
                ItemNode node = (ItemNode) clazz.newInstance();
                item = new ItemBase(node);
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException("Failed to find class for '" + className + "' in order to construct item node", e);
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException("Failed to create constructor for class '" + className + "'", e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException("Failed to access node constructor for class '" + className + "'", e);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Unexpected error creating node from '" + className + "'", e);
            }
        }
        else
        {
            ensureValuesExist(itemData, "id", "mod");
            String id = itemData.getAsJsonPrimitive("id").getAsString();
            String mod = itemData.getAsJsonPrimitive("mod").getAsString();
            String name = itemData.getAsJsonPrimitive("name").getAsString();
            item = new ItemBase(id, mod, name);

            debugPrinter.log("Name: " + name);
            debugPrinter.log("Mod: " + mod);
            debugPrinter.log("ID: " + id);
        }

        //TODO implement subtypes

        //Handles loading node data for item
        for (Map.Entry<String, JsonElement> entry : itemData.entrySet())
        {
            if (itemPropDataHandler.handle(item.node, entry.getKey().toLowerCase(), entry.getValue()))
            {
                if (Engine.runningAsDev)
                {
                    debugPrinter.log("Injected Key: " + entry.getKey());
                }
            }
        }

        objectList.add(item);
        debugPrinter.end("Done...");
        return true;
    }
}
