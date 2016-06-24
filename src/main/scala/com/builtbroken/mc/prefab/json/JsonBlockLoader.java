package com.builtbroken.mc.prefab.json;

import com.builtbroken.mc.content.VeContent;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.prefab.json.processors.JsonBlockProcessor;
import com.builtbroken.mc.prefab.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonBlockLoader extends AbstractLoadable
{
    List<String> resources = new ArrayList();
    List<JsonProcessor> processors = new ArrayList();

    @Override
    public void preInit()
    {
        //TODO find mods that need shit loaded - MetaInf file -> Directories -> Sift

        //Load processors
        processors.add(new JsonBlockProcessor());
    }

    @Override
    public void init()
    {
        for (String resource : resources)
        {
            try
            {
                JsonElement element = load(resource);
                for (JsonProcessor processor : processors)
                {
                    if (processor.canProcess(element))
                    {
                        Object data = processor.process(element);
                        break;
                    }
                }
            }
            catch (IOException e)
            {
                //Crash as the file may be important
                throw new RuntimeException("Failed to load resource " + resource, e);
            }
        }
    }

    /**
     * Loads a json file from the resource path
     *
     * @param resource - resource location
     * @return json file as a json element object
     * @throws IOException
     */
    public static JsonElement load(String resource) throws IOException
    {
        URL url = VeContent.class.getClassLoader().getResource(resource);
        InputStream stream = url.openStream();
        JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(stream)));
        JsonElement element = Streams.parse(jsonReader);
        stream.close();
        return element;
    }

    @Override
    public void postInit()
    {

    }
}
