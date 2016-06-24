package com.builtbroken.mc.testing.json;

import com.builtbroken.mc.content.VeContent;
import com.builtbroken.mc.prefab.json.BlockJsonMeta;
import com.builtbroken.mc.prefab.json.processors.JsonBlockProcessor;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
@RunWith(VoltzTestRunner.class)
public class JsonTest extends AbstractTest
{
    public void testJson() throws IOException
    {
        URL url = VeContent.class.getClassLoader().getResource("content/vec/MetalOre.json");
        InputStream stream = url.openStream();
        JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(stream)));
        JsonElement element = Streams.parse(jsonReader);
        JsonObject object = element.getAsJsonObject();
    }

    public void testBlockProcessor() throws IOException
    {
        URL url = VeContent.class.getClassLoader().getResource("content/vec/MetalOre.json");
        InputStream stream = url.openStream();
        JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(stream)));
        JsonElement element = Streams.parse(jsonReader);

        JsonBlockProcessor processor = new JsonBlockProcessor();
        assertTrue(processor.canProcess(element));

        Object obj = processor.process(element);
        assertNotNull(obj);
        assertTrue(obj instanceof BlockJsonMeta);
    }
}
