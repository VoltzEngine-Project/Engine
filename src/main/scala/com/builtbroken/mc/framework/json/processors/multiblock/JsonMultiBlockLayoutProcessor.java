package com.builtbroken.mc.framework.json.processors.multiblock;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.multiblock.EnumMultiblock;
import com.builtbroken.mc.framework.multiblock.structure.MultiBlockLayout;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.framework.json.conversion.JsonConverterPos;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.Map;

/**
 * Prefab for any processor that uses item/block based recipes
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public class JsonMultiBlockLayoutProcessor extends JsonProcessor<MultiBlockLayout>
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "multiblock";
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }

    @Override
    public MultiBlockLayout process(JsonElement data)
    {
        //Get object
        JsonObject multiBlockData = data.getAsJsonObject();

        //Validate data
        ensureValuesExist(multiBlockData, "key", "tiles");

        //Get registry key
        String key = multiBlockData.get("key").getAsString().toLowerCase();

        //Create object
        MultiBlockLayout layout = new MultiBlockLayout(this, key);

        //Process array
        JsonArray array = multiBlockData.get("tiles").getAsJsonArray();
        for (JsonElement element : array)
        {
            if (element.isJsonObject())
            {
                JsonObject object = element.getAsJsonObject();

                if (object.has("pos"))
                {
                    createPos(object, layout);
                }
                else if (object.has("for"))
                {
                    doForLoop(object.getAsJsonObject("for"), layout, new HashMap());
                }
                else
                {
                    throw new IllegalArgumentException("Unknown entry in multiblock array.");
                }
            }
        }
        return layout;
    }

    protected void doForLoop(JsonObject object, MultiBlockLayout layout, HashMap<String, Integer> map)
    {
        //Validate minimal requirements
        ensureValuesExist(object, "start", "end");

        //Get input
        int start = object.getAsJsonPrimitive("start").getAsInt();
        int end = object.getAsJsonPrimitive("end").getAsInt();
        String key = object.has("id") ? object.getAsJsonPrimitive("id").getAsString() : "number";

        //validate input
        if (start >= end)
        {
            throw new IllegalArgumentException("Start can not be greater than or equal to end for a for loop.");
        }

        //Do loop
        for (int i = start; i <= end; i++)
        {
            //Index position
            map.put(key, i);

            //Multiblock code
            if (object.has("multiblock"))
            {
                //Get template
                JsonObject template = object.getAsJsonObject("multiblock");

                //Copy template
                JsonObject state = new JsonObject();
                replaceValues(template, state, map);

                //Create value from copy
                createPos(state, layout);
            }
            else if (object.has("for"))
            {
                if (!object.has("id"))
                {
                    throw new IllegalArgumentException("Repeat for loops require ID be provide for each loop to iterate correctly.");
                }
                doForLoop(object.getAsJsonObject("for"), layout, map);
            }
            else
            {
                throw new IllegalArgumentException("Loop was provided without data to create.");
            }
        }
    }

    protected void replaceValues(JsonObject template, JsonObject state, HashMap<String, Integer> map)
    {
        //Copy template and rename values as needed
        for (Map.Entry<String, JsonElement> entry : template.entrySet())
        {
            if (entry.getValue() instanceof JsonPrimitive && ((JsonPrimitive) entry.getValue()).isString())
            {
                String s = entry.getValue().getAsString();
                if (s.contains("%"))
                {
                    for (Map.Entry<String, Integer> e : map.entrySet())
                    {
                        s = s.replace("%" + e.getKey() + "%", "" + e.getValue());
                    }
                }
                state.add(entry.getKey(), new JsonPrimitive(s));
            }
            else if (entry.getValue() instanceof JsonObject)
            {
                JsonObject object = new JsonObject();
                replaceValues(entry.getValue().getAsJsonObject(), object, map);
                state.add(entry.getKey(), object);
            }
            //TODO handle arrays and other types
            else
            {
                state.add(entry.getKey(), entry.getValue());
            }
        }
    }

    protected void createPos(JsonObject object, MultiBlockLayout layout)
    {
        ensureValuesExist(object, "pos");
        Pos pos = JsonConverterPos.fromJson(object.get("pos"));
        String tile = EnumMultiblock.TILE.getTileName();
        String tileData = "";
        if (object.has("data"))
        {
            tileData = object.get("data").getAsString();
        }
        if (object.has("tile"))
        {
            tile = object.get("tile").getAsString(); //TODO ensure matches enum
        }
        layout.addTile(pos, tile + "#" + tileData);
    }
}
