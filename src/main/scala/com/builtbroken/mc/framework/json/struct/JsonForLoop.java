package com.builtbroken.mc.framework.json.struct;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.*;
import java.util.function.Consumer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class JsonForLoop
{

    public static final int MAX_FOR_LOOPS = 100;

    /**
     * Used to check if an object contains a loop
     *
     * @param currentJsonObject
     * @return true if a loop tag exists
     */
    public static boolean hasLoops(JsonObject currentJsonObject)
    {
        if (currentJsonObject != null)
        {
            for (Map.Entry<String, JsonElement> elementEntry : currentJsonObject.entrySet())
            {
                String key = elementEntry.getKey();
                if (key.contains(":"))
                {
                    String[] split = key.split(":");
                    key = split[0];
                }
                if (key.equalsIgnoreCase("for"))
                {
                    return true;
                }
                else if (key.equalsIgnoreCase("forEach"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Called to handle any loops found in an object.
     * <p>
     * Allows for more than 1 loop and nested loops
     *
     * @param currentJsonObject - object being processed containing template and loop data
     * @param action            - process to run on generated objects
     * @return true if something was processed
     */
    public static boolean handleLoops(JsonObject currentJsonObject, Consumer<JsonObject> action)
    {
        boolean handled = false;
        for (Map.Entry<String, JsonElement> elementEntry : currentJsonObject.entrySet())
        {
            String key = elementEntry.getKey();
            String subKey = "";
            if (key.contains(":"))
            {
                String[] split = key.split(":");
                if (split.length > 2)
                {
                    throw new IllegalArgumentException("JsonForLoop: Loop entry name for '" + key + "' should only contain one ':'");
                }
                key = split[0];
                subKey = split[1];
            }
            if (key.equalsIgnoreCase("for"))
            {
                generateDataForLoop(currentJsonObject, action);
                handled = true;
            }
            else if (key.equalsIgnoreCase("forEach"))
            {
                generateDataForEachLoop(currentJsonObject, action);
                handled = true;
            }
        }
        return handled;
    }


    /**
     * Handles JSON for for loops for generate JsonObject data. Is a recursive call that allows
     * for nest loops.
     *
     * @param currentJsonObject - object being read
     * @param action            - process
     * @return list of objects generated
     */
    public static void generateDataForLoop(JsonObject currentJsonObject, Consumer<JsonObject> action)
    {
        //Validate
        Objects.requireNonNull(action);

        //Generate data
        List<JsonObject> elements = new ArrayList();
        JsonForLoop.generateDataForLoop(currentJsonObject, elements);

        //Process
        for (JsonObject object : elements)
        {
            action.accept(object);
        }
    }

    /**
     * Handles JSON for for loops for generate JsonObject data.
     * <p>
     * Is a recursive call that allows for nest loops.
     *
     * @param currentJsonObject - object being read
     * @param states            - generate data, populated by method call
     * @return list of objects generated
     */
    public static void generateDataForLoop(JsonObject currentJsonObject, List<JsonObject> states)
    {
        generateDataForLoop(currentJsonObject, states, new HashMap(), 0);
    }

    /**
     * Handles JSON for for loops for generate JsonObject data.
     * <p>
     * Is a recursive call that allows for nest loops.
     *
     * @param currentJsonObject - object being read
     * @param injectionKeys     - inject keys to insert into template
     * @return list of objects generated
     */
    public static void generateDataForLoop(JsonObject currentJsonObject, List<JsonObject> states, HashMap<String, String> injectionKeys, int depth)
    {
        //Ensure values
        if (!currentJsonObject.has("start") || !currentJsonObject.has("end"))
        {
            throw new RuntimeException("For loop expects 'start' and 'end' values in JSON object structure");
        }

        JsonPrimitive startData = currentJsonObject.getAsJsonPrimitive("start");
        JsonPrimitive endData = currentJsonObject.getAsJsonPrimitive("end");

        //Get values
        int start = 0;
        int end = 0;

        if (startData.isNumber())
        {
            start = startData.getAsInt();
        }
        else
        {
            String s = startData.getAsString();
            s = injectValues(s, injectionKeys);
            start = Integer.parseInt(s);
        }

        if (endData.isNumber())
        {
            end = endData.getAsInt();
        }
        else
        {
            String s = endData.getAsString();
            s = injectValues(s, injectionKeys);
            end = Integer.parseInt(s);
        }


        String id = "number";
        if (currentJsonObject.has("id"))
        {
            id = currentJsonObject.getAsJsonPrimitive("id").getAsString();
        }

        if (depth != 0 && "number".equalsIgnoreCase(id))
        {
            throw new RuntimeException("Nested for loops must each have there own unique id so not to overlap with default");
        }


        //Validate data
        if (start >= end)
        {
            throw new IllegalArgumentException("Start can not be greater than or equal to end for a for loop.");
        }

        //Loop entries
        for (int i = start; i <= end; i++)
        {
            injectionKeys.put(id, "" + i);
            handle(currentJsonObject, states, injectionKeys, depth);
        }

        //Remove ID to prevent issues
        injectionKeys.remove(id);
    }

    /**
     * Handles generating and processing data for a for each loop
     *
     * @param currentJsonObject - loop
     * @param action            - process
     */
    public static void generateDataForEachLoop(JsonObject currentJsonObject, Consumer<JsonObject> action)
    {
        //Validate
        Objects.requireNonNull(action);

        //Generate data
        List<JsonObject> elements = new ArrayList();
        JsonForLoop.generateDataForEachLoop(currentJsonObject, elements);

        //Process
        for (JsonObject object : elements)
        {
            action.accept(object);
        }
    }

    /**
     * Handles generating and processing data for a for each loop
     *
     * @param currentJsonObject - loop
     * @param states            - generated data, will be populated by method call
     */
    public static void generateDataForEachLoop(JsonObject currentJsonObject, List<JsonObject> states)
    {
        generateDataForEachLoop(currentJsonObject, states);
    }

    /**
     * Handles generating and processing data for a for each loop.
     * <p>
     * This is a recursive method and has a depth limit of a 100 to prevent crashing.
     *
     * @param currentJsonObject - loop
     * @param states            - generated data, will be populated by method call
     * @param injectionKeys     - keys -> value map, used for replacing %key% in template data, populated by method each iteration
     */
    public static void generateDataForEachLoop(JsonObject currentJsonObject, List<JsonObject> states, HashMap<String, String> injectionKeys, int depth)
    {
        if (!currentJsonObject.has("values") || !currentJsonObject.get("values").isJsonArray())
        {
            throw new RuntimeException("ForEach must define a JSON array of values to loop over");
        }

        JsonArray values = currentJsonObject.getAsJsonArray("values");

        for (JsonElement e : values)
        {
            if (e.isJsonObject())
            {
                JsonObject o = e.getAsJsonObject();
                HashMap<String, String> newKeys = new HashMap();
                for (Map.Entry<String, JsonElement> entry : o.entrySet())
                {
                    if (entry.getValue().isJsonPrimitive())
                    {
                        newKeys.put(entry.getKey(), entry.getValue().getAsString());
                    }
                    else
                    {
                        throw new RuntimeException("ForEach values keys must be JSON primitives (string, int, double, float, etc)");
                    }
                }

                injectionKeys.putAll(newKeys);
                handle(currentJsonObject, states, injectionKeys, depth);
                for (String key : newKeys.keySet())
                {
                    injectionKeys.remove(key);
                }
            }
            else
            {
                throw new RuntimeException("ForEach values must be define as a JSON object containing key:value");
            }
        }
    }

    /**
     * Internal handling for each entry in a loop. Allows fo nested loops and injection of sub objects
     *
     * @param currentJsonObject
     * @param states
     * @param injectionKeys
     * @param depth
     */
    public static void handle(JsonObject currentJsonObject, List<JsonObject> states, HashMap<String, String> injectionKeys, int depth)
    {
        JsonObject template;
        //Recursive loops
        if (currentJsonObject.has("for"))
        {
            if (depth >= MAX_FOR_LOOPS)
            {
                throw new RuntimeException("Too many for loops while building json element data, the limit is set to " + MAX_FOR_LOOPS + " to prevent infinite loop.");
            }
            template = currentJsonObject.getAsJsonObject("for");

            JsonObject object = buildObjectFromTemplate(template, injectionKeys, false);
            generateDataForLoop(object, states, injectionKeys, depth + 1);

        }
        //Recursive loops
        else if (currentJsonObject.has("forEach"))
        {
            template = currentJsonObject.getAsJsonObject("forEach");
            JsonObject object = buildObjectFromTemplate(template, injectionKeys, false);
            generateDataForEachLoop(object, states, injectionKeys, depth + 1);
        }
        else if (currentJsonObject.has("data"))
        {
            template = currentJsonObject.getAsJsonObject("data");
            states.add(buildObjectFromTemplate(template, injectionKeys, true));
        }
        //Legacy code
        else if (currentJsonObject.has("state"))
        {
            template = currentJsonObject.getAsJsonObject("state");
            states.add(buildObjectFromTemplate(template, injectionKeys, true));
        }
        else
        {
            throw new IllegalArgumentException("ForLoop could not understate layout, expected 'for', 'forEach', or 'data' entries in JSON object");
        }
    }

    //TODO move to shared JSON helper
    public static JsonObject buildObjectFromTemplate(JsonObject template, HashMap<String, String> injectionKeys, boolean doNested)
    {
        JsonObject state = new JsonObject();

        //Copy template and rename values as needed
        for (Map.Entry<String, JsonElement> jsonEntry : template.entrySet())
        {
            state.add(jsonEntry.getKey(), buildElementFromTemplate(jsonEntry.getValue(), injectionKeys, doNested));
        }
        return state;
    }

    public static JsonElement buildElementFromTemplate(JsonElement value, HashMap<String, String> injectionKeys, boolean doNested)
    {
        if (value instanceof JsonPrimitive)
        {
            if (((JsonPrimitive) value).isString())
            {
                return new JsonPrimitive(injectValues(value.getAsString(), injectionKeys));
            }
            else if (((JsonPrimitive) value).isNumber())
            {
                return new JsonPrimitive(value.getAsNumber());
            }
            else if (((JsonPrimitive) value).isBoolean())
            {
                return new JsonPrimitive(value.getAsBoolean());
            }
        }
        else if (value instanceof JsonObject)
        {
            return buildObjectFromTemplate(value.getAsJsonObject(), injectionKeys, doNested);
        }
        else if (value instanceof JsonArray)
        {
            JsonArray clone = new JsonArray();
            JsonArray array = value.getAsJsonArray();
            for (JsonElement element : array)
            {
                clone.add(buildElementFromTemplate(element, injectionKeys, doNested));
            }
        }
        return value;
    }

    //TODO move to shared JSON helper
    public static String injectValues(final String string, final HashMap<String, String> injectionKeys)
    {
        String s = string;
        for (Map.Entry<String, String> entry : injectionKeys.entrySet())
        {
            final String value = entry.getValue();
            s = s.replace("%" + entry.getKey() + "%", value);
        }
        return s;
    }
}
