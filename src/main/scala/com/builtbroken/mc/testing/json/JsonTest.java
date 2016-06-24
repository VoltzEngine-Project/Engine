package com.builtbroken.mc.testing.json;

import com.builtbroken.mc.content.VeContent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonTest extends TestCase
{
    public void testJson() throws IOException
    {
        URL url =  VeContent.class.getClassLoader().getResource("content/vec/MetalOre.json");
        InputStream stream = url.openStream();
        JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(stream)));
        JsonElement element = Streams.parse(jsonReader);
        JsonObject object = element.getAsJsonObject();
        System.out.println(object.getAsJsonPrimitive("block"));
    }
}
