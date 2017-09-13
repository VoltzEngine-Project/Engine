package com.builtbroken.mc.framework.json.loading;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.conversion.*;
import com.builtbroken.mc.framework.json.conversion.data.JsonConverterEnergyBufferData;
import com.builtbroken.mc.framework.json.conversion.data.JsonConverterEnergyChargeData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for working with json files
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class JsonLoader
{
    /** Map of types to json data converts */
    public static final HashMap<String, JsonConverter> conversionHandlers = new HashMap();

    static
    {
        addConverter(new JsonConverterPos());
        addConverter(new JsonConverterNBT());
        addConverter(new JsonConverterStringArray());
        addConverter(new JsonConverterCube());
        addConverter(new JsonConverterItem());
        addConverter(new JsonConverterEnergyBufferData());
        addConverter(new JsonConverterEnergyChargeData());
    }

    public static void addConverter(JsonConverter converter)
    {
        String key = converter.key.toLowerCase();
        if (conversionHandlers.containsKey(key))
        {
            Engine.logger().error("Overriding converter '" + key + "' with " + converter + ", previous value " + conversionHandlers.get(key));
        }
        conversionHandlers.put(key, converter);
    }

    /**
     * Loads a json file from the resource path
     *
     * @param resource - resource location
     * @return json file as a json element object
     * @throws IOException
     */
    public static void loadJsonFileFromResources(URL resource, HashMap<String, List<JsonEntry>> entries) throws Exception
    {
        if (resource != null)
        {
            loadJson(resource.getFile(), new InputStreamReader(resource.openStream()), entries);
        }
    }

    /**
     * Loads a json file from the resource path
     *
     * @param file - file to read from
     * @return json file as a json element object
     * @throws IOException
     */
    public static void loadJsonFile(File file, HashMap<String, List<JsonEntry>> entries) throws IOException
    {
        if (file.exists() && file.isFile())
        {
            FileReader stream = new FileReader(file);
            loadJson(file.getName(), new BufferedReader(stream), entries);
            stream.close();
        }
    }

    /**
     * Loads a json file from a reader
     *
     * @param fileName - file the reader loaded from, used only for error logs
     * @param reader   - reader with the data
     * @param entries  - place to put json entries into
     */
    public static void loadJson(String fileName, Reader reader, HashMap<String, List<JsonEntry>> entries)
    {
        try
        {
            JsonReader jsonReader = new JsonReader(reader);
            JsonElement element = Streams.parse(jsonReader);
            loadJsonElement(fileName, element, entries);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to parse file '" + fileName + "'", e);
        }
    }

    /**
     * Creates an json element from a string
     *
     * @param data - string data, needs to be formatted correctly,
     *             e.g. { "content" : { "more":"content"} }
     * @return json element
     * @throws JsonSyntaxException - if string is not formatted correctly
     */
    public static JsonElement createElement(String data)
    {
        JsonReader jsonReader = new JsonReader(new StringReader(data));
        return Streams.parse(jsonReader);
    }

    /**
     * Loads the data from the element passed in and creates {@link JsonEntry} for processing
     * later on.
     *
     * @param file    - file the element was read from
     * @param element - the element to process
     * @param entries - list to populate with new entries
     */
    public static List<JsonEntry> loadJsonElement(String file, JsonElement element, HashMap<String, List<JsonEntry>> entries)
    {
        if (element.isJsonObject())
        {
            final JsonObject object = element.getAsJsonObject();

            //Handle load conditions
            if (object.has("loadCondition"))
            {
                final String condition = object.getAsJsonPrimitive("loadCondition").getAsString(); //TODO implement array version

                //Dev mod load condition
                if (condition.equalsIgnoreCase("devMode") && !Engine.runningAsDev)
                {
                    return new ArrayList();
                }
                //Mod load condition
                else if (condition.startsWith("mod:"))
                {
                    final String modName = condition.substring(4, condition.length());
                    final List<ModContainer> mods = Loader.instance().getActiveModList();
                    boolean found = false;
                    for (ModContainer mod : mods)
                    {
                        if (mod.getModId().equalsIgnoreCase(modName))
                        {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                    {
                        Engine.logger().info("JsonLoader: File '" + file + "' contains load condition for mod '" + modName + "' but mod is not loaded. This file will be ignored for JSON loading.");
                        return new ArrayList();
                    }
                }
            }

            //Handle author data
            String author = null;
            String helpSite = null;
            if (object.has("author"))
            {
                final JsonObject authorData = object.get("author").getAsJsonObject();
                author = authorData.get("name").getAsString();
                if (authorData.has("site"))
                {
                    helpSite = authorData.get("site").getAsString();
                }
            }

            //Load entries
            List<JsonEntry> processedEntries = new ArrayList();
            for (Map.Entry<String, JsonElement> entry : object.entrySet())
            {
                if (!ignoreLoadingEntry(entry.getKey()))
                {
                    String key = entry.getKey();
                    if (key.contains(":"))
                    {
                        key = key.split(":")[0];
                    }
                    JsonEntry jsonEntry = new JsonEntry(key, file, entry.getValue());
                    jsonEntry.author = author;
                    jsonEntry.authorHelpSite = helpSite;
                    List<JsonEntry> list = entries.get(jsonEntry.jsonKey);
                    if (list == null)
                    {
                        list = new ArrayList();
                    }
                    list.add(jsonEntry);
                    entries.put(jsonEntry.jsonKey, list);
                    processedEntries.add(jsonEntry);
                }
            }
            return processedEntries;
        }
        return null;
    }

    //Json keys to ignore when processing
    private static boolean ignoreLoadingEntry(String key)
    {
        return key.equalsIgnoreCase("author") // Ignore author as its already handled
                || key.startsWith("_") // Ignore comments
                || key.equalsIgnoreCase("loadCondition")  // Ignore load condition as its already handled
                || key.equalsIgnoreCase("editorData");  // Ignore data stored by editor
    }
}
