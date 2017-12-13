package com.builtbroken.test.json.loader;

import com.builtbroken.mc.framework.json.loading.JsonLoader;
import com.builtbroken.mc.framework.json.loading.JsonProcessorInjectionMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class TestProcessorInjection extends TestCase
{
    @Test
    public void testInit()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        assertTrue(map.jsonDataFields.containsKey("key"));
        assertNotNull(map.jsonDataFields.get("key"));

        assertTrue(map.jsonDataSetters.containsKey("key2"));
        assertNotNull(map.jsonDataSetters.get("key2"));
    }

    @Test
    public void testField()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();
        assertTrue(map.handle(object, "key", "String"));
        assertEquals("String", object.key);
    }

    @Test
    public void testMethod()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();
        assertTrue(map.handle(object, "key2", "String"));
        assertEquals("String", object.key);
    }

    @Test
    public void testJsonElement()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();
        JsonElement element = createTestElement();

        assertTrue(map.handle(object, "element", element));
        assertSame(element, object.element);

        object.element = null;
        assertTrue(map.handle(object, "element2", element));
        assertSame(element, object.element);
    }

    @Test
    public void testJsonBoolean()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        JsonPrimitive primitive = new JsonPrimitive(true);
        assertTrue(map.handle(object, "bool", primitive));
        assertTrue(object.bool);

        assertTrue(map.handle(object, "bool2", primitive));
        assertTrue(object.bool);
    }

    @Test
    public void testBoolean()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "bool", true));
        assertTrue(object.bool);

        assertTrue(map.handle(object, "bool2", true));
        assertTrue(object.bool);
    }

    @Test
    public void testJsonInteger()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "i", new JsonPrimitive(1)));
        assertEquals(1, object.i);

        assertTrue(map.handle(object, "i3", new JsonPrimitive(2)));
        assertEquals(2, object.i);

        assertTrue(map.handle(object, "i2", new JsonPrimitive(3)));
        assertEquals(3, object.i2);

        assertTrue(map.handle(object, "i4", new JsonPrimitive(4)));
        assertEquals(4, object.i2);
    }

    @Test
    public void testJsonByte()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "b", new JsonPrimitive((byte)1)));
        assertEquals(1, object.b);

        assertTrue(map.handle(object, "b2", new JsonPrimitive((byte)2)));
        assertEquals(2, object.b);
    }

    @Test
    public void testJsonShort()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "s", new JsonPrimitive((short)1)));
        assertEquals(1, object.s);

        assertTrue(map.handle(object, "s2", new JsonPrimitive((short)2)));
        assertEquals(2, object.s);
    }

    @Test
    public void testJsonLong()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "l", new JsonPrimitive(1l)));
        assertEquals(1l, object.l);

        assertTrue(map.handle(object, "l2", new JsonPrimitive(2l)));
        assertEquals(2l, object.l);
    }

    @Test
    public void testJsonFloat()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "f", new JsonPrimitive(1f)));
        assertEquals(1f, object.f);

        assertTrue(map.handle(object, "f2", new JsonPrimitive(2f)));
        assertEquals(2f, object.f);
    }

    @Test
    public void testJsonDouble()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "d", new JsonPrimitive(1.1)));
        assertEquals(1.1, object.d);

        assertTrue(map.handle(object, "d2", new JsonPrimitive(2.2)));
        assertEquals(2.2, object.d);
    }

    @Test
    public void testInteger()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "i", 1));
        assertEquals(1, object.i);

        assertTrue(map.handle(object, "i3", 2));
        assertEquals(2, object.i);

        assertTrue(map.handle(object, "i2", 3));
        assertEquals(3, object.i2);

        assertTrue(map.handle(object, "i4", 4));
        assertEquals(4, object.i2);
    }

    @Test
    public void testByte()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "b", (byte)1));
        assertEquals(1, object.b);

        assertTrue(map.handle(object, "b2", (byte)2));
        assertEquals(2, object.b);
    }

    @Test
    public void testShort()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "s", (short)1));
        assertEquals(1, object.s);

        assertTrue(map.handle(object, "s2", (short)2));
        assertEquals(2, object.s);
    }

    @Test
    public void testLong()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "l", 1l));
        assertEquals(1l, object.l);

        assertTrue(map.handle(object, "l2", 2l));
        assertEquals(2l, object.l);
    }

    @Test
    public void testFloat()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "f", 1f));
        assertEquals(1f, object.f);

        assertTrue(map.handle(object, "f2", 2f));
        assertEquals(2f, object.f);
    }

    @Test
    public void testDouble()
    {
        JsonProcessorInjectionMap map = new JsonProcessorInjectionMap(InjectionTestClass.class);
        InjectionTestClass object = new InjectionTestClass();

        assertTrue(map.handle(object, "d", 1.1));
        assertEquals(1.1, object.d);

        assertTrue(map.handle(object, "d2", 2.2));
        assertEquals(2.2, object.d);
    }

    private JsonElement createTestElement()
    {
        String testData = "{}";
        return JsonLoader.createElement(testData);
    }
}
