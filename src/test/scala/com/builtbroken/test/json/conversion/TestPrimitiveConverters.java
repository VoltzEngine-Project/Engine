package com.builtbroken.test.json.conversion;

import com.builtbroken.mc.framework.json.loading.JsonLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import junit.framework.TestCase;

import java.util.function.Consumer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/26/2017.
 */
public class TestPrimitiveConverters extends TestCase
{
    public void testByte()
    {
        JsonPrimitive primitive = new JsonPrimitive((byte) 1);

        Object o = JsonLoader.convertElement("byte", primitive);
        assertTrue(o instanceof Byte);
        assertEquals((byte) 1, o);

        testJunkJson("Expected error for ", e -> JsonLoader.convertElement("byte", e), new JsonObject(), new JsonArray());
    }


    public void testShort()
    {
        JsonPrimitive primitive = new JsonPrimitive((short) 1);

        Object o = JsonLoader.convertElement("short", primitive);
        assertTrue(o instanceof Short);
        assertEquals((short) 1, o);

        testJunkJson("Expected error for ", e -> JsonLoader.convertElement("short", e), new JsonObject(), new JsonArray());
    }

    public void testInt()
    {
        JsonPrimitive primitive = new JsonPrimitive(1);

        Object o = JsonLoader.convertElement("int", primitive);
        assertTrue(o instanceof Integer);
        assertEquals(1, o);

        o = JsonLoader.convertElement("integer", primitive);
        assertTrue(o instanceof Integer);
        assertEquals(1, o);

        testJunkJson("Expected error for ", e -> JsonLoader.convertElement("int", e), new JsonObject(), new JsonArray());
    }

    public void testLong()
    {
        JsonPrimitive primitive = new JsonPrimitive(1L);

        Object o = JsonLoader.convertElement("long", primitive);
        assertTrue(o instanceof Long);
        assertEquals(1L, o);

        testJunkJson("Expected error for ", e -> JsonLoader.convertElement("long", e), new JsonObject(), new JsonArray());
    }

    public void testFloat()
    {
        JsonPrimitive primitive = new JsonPrimitive(1F);

        Object o = JsonLoader.convertElement("float", primitive);
        assertTrue(o instanceof Float);
        assertEquals(1F, o);

        testJunkJson("Expected error for ", e -> JsonLoader.convertElement("float", e), new JsonObject(), new JsonArray());
    }

    public void testDouble()
    {
        JsonPrimitive primitive = new JsonPrimitive(1.0);

        Object o = JsonLoader.convertElement("double", primitive);
        assertTrue(o instanceof Double);
        assertEquals(1.0, o);

        testJunkJson("Expected error for ", e -> JsonLoader.convertElement("double", e), new JsonObject(), new JsonArray());
    }

    protected void testJunkJson(String error, Consumer<JsonElement> action, JsonElement... junk)
    {
        for (JsonElement object : junk)
        {
            try
            {
                action.accept(object);
                fail(error + object);
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                //Desired result
            }
        }
    }
}
