package com.builtbroken.test.json.conversion;

import com.builtbroken.mc.framework.json.loading.JsonLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import junit.framework.TestCase;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/26/2017.
 */
public class TestHashMapConverter extends TestCase
{
    public void testHashMap()
    {
        JsonArray array = new JsonArray();

        JsonObject object = new JsonObject();
        object.add("key", new JsonPrimitive(1));
        object.add("value", new JsonPrimitive(2));
        array.add(object);

        object = new JsonObject();
        object.add("key", new JsonPrimitive(3));
        object.add("value", new JsonPrimitive(4));
        array.add(object);

        object = new JsonObject();
        object.add("key", new JsonPrimitive(5));
        object.add("value", new JsonPrimitive(6));
        array.add(object);

        HashMap map = (HashMap) JsonLoader.convertElement("HashMap", array, "key", "int", "value", "int");

        assertNotNull(map);
        assertEquals(3, map.size());

        assertEquals(2, map.get(1));
        assertEquals(4, map.get(3));
        assertEquals(6, map.get(5));
    }
}
