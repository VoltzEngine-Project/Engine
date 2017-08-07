package com.builtbroken.mc.lib.json.loading;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.json.conversion.*;
import com.builtbroken.mc.lib.json.conversion.data.JsonConverterEnergyBufferData;
import com.builtbroken.mc.lib.json.conversion.data.JsonConverterEnergyChargeData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

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
    public static void loadJsonElement(String file, JsonElement element, HashMap<String, List<JsonEntry>> entries)
    {
        if (element.isJsonObject())
        {
            JsonObject object = element.getAsJsonObject();
            String author = null;
            String helpSite = null;
            if (object.has("author"))
            {
                JsonObject authorData = object.get("author").getAsJsonObject();
                author = authorData.get("name").getAsString();
                if (authorData.has("site"))
                {
                    helpSite = authorData.get("site").getAsString();
                }
            }
            for (Map.Entry<String, JsonElement> entry : object.entrySet())
            {
                if (!entry.getKey().equalsIgnoreCase("author"))
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
                }
            }
        }
    }
}
