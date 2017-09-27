package com.builtbroken.test.json;

import com.builtbroken.mc.framework.json.struct.JsonForLoop;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/7/2017.
 */
public class TestForLoop extends TestCase
{
    @Test
    public void testForLoop()
    {
        //Generate JSON
        JsonObject forLoopJSON = buildLoop("number", 0, 10);
        JsonObject dataJSON = new JsonObject();
        dataJSON.add("someVar", new JsonPrimitive("value.%number%"));
        forLoopJSON.add("data", dataJSON);

        List<JsonObject> elements = new ArrayList();
        JsonForLoop.generateDataForLoop(forLoopJSON, elements, new HashMap(), 0);

        assertEquals(11, elements.size());
        int i = 0;
        for (JsonObject o : elements)
        {
            assertTrue(o.has("someVar"));
            assertEquals("value." + i++, o.getAsJsonPrimitive("someVar").getAsString());
        }
    }

    @Test
    public void testForLoopNested()
    {
        //First for loop
        JsonObject forLoopJSON2 = buildLoop("i", 0, 4);

        //Nested loop
        JsonObject forLoopJSON = buildLoop("j", 0, 4);

        //Data
        JsonObject dataJSON = new JsonObject();
        dataJSON.add("someVar", new JsonPrimitive("value.%i%.%j%"));
        forLoopJSON.add("data", dataJSON);

        forLoopJSON2.add("for", forLoopJSON);

        List<JsonObject> elements = new ArrayList();
        JsonForLoop.generateDataForLoop(forLoopJSON2, elements, new HashMap(), 0);

        //We should have 25 entries
        assertEquals(25, elements.size());

        //All entries should have a value, not containing injection points
        for (JsonObject o : elements)
        {
            assertTrue(o.has("someVar"));
            assertTrue(!o.get("someVar").getAsString().contains("%"));
        }

        //We should have exactly values
        for (int i = 0; i <= 4; i++)
        {
            for (int j = 0; j <= 4; j++)
            {
                String match = "value." + i + "." + j;
                Iterator<JsonObject> it = elements.iterator();
                while (it.hasNext())
                {
                    JsonObject object = it.next();
                    if (object.get("someVar").getAsString().equals(match))
                    {
                        it.remove();
                        break;
                    }
                }
            }
        }
        assertEquals(0, elements.size());
    }

    @Test
    public void testForLoopNested2()
    {
        //First for loop
        JsonObject forLoopA = buildLoop("a", 0, 4);

        //Nested loop
        JsonObject forLoopB = buildLoop("b", 0, 4);

        //Nested loop
        JsonObject forLoopC = buildLoop("c", 0, 4);

        //Nested loop
        JsonObject forLoopD = buildLoop("d", 0, 4);

        //Data
        JsonObject dataJSON = new JsonObject();
        dataJSON.add("someVar", new JsonPrimitive("value.%a%.%b%.%c%.%d%"));
        forLoopD.add("data", dataJSON);

        forLoopC.add("for", forLoopD);
        forLoopB.add("for", forLoopC);
        forLoopA.add("for", forLoopB);

        List<JsonObject> elements = new ArrayList();
        JsonForLoop.generateDataForLoop(forLoopA, elements, new HashMap(), 0);

        //We should have 625 entries
        assertEquals(625, elements.size());

        //All entries should have a value, not containing injection points
        for (JsonObject o : elements)
        {
            assertTrue(o.has("someVar"));
            assertTrue(!o.get("someVar").getAsString().contains("%"));
        }

        //We should have exactly values
        for (int a = 0; a <= 4; a++)
        {
            for (int b = 0; b <= 4; b++)
            {
                for (int c = 0; c <= 4; c++)
                {
                    for (int d = 0; d <= 4; d++)
                    {
                        String match = "value." + a + "." + b + "." + c + "." + d;
                        Iterator<JsonObject> it = elements.iterator();
                        while (it.hasNext())
                        {
                            JsonObject object = it.next();
                            if (object.get("someVar").getAsString().equals(match))
                            {
                                it.remove();
                                break;
                            }
                        }
                    }
                }
            }
        }
        assertEquals(0, elements.size());
    }

    @Test
    public void testForLoopEach()
    {
        JsonObject forLoop = new JsonObject();

        //values
        JsonArray values = new JsonArray();
        values.add(buildLoopValue("tree", "1", "bee", "4"));
        values.add(buildLoopValue("tree", "2", "bee", "5"));
        values.add(buildLoopValue("tree", "3", "bee", "6"));
        forLoop.add("values", values);

        //Data
        JsonObject dataJSON = new JsonObject();
        dataJSON.add("someVar", new JsonPrimitive("value.%tree%.%bee%"));
        forLoop.add("data", dataJSON);

        List<JsonObject> elements = new ArrayList();
        JsonForLoop.generateDataForEachLoop(forLoop, elements, new HashMap(), 0);

        //We should have 3 entries
        assertEquals(3, elements.size());

        //All entries should have a value, not containing injection points
        for (JsonObject o : elements)
        {
            assertTrue(o.has("someVar"));
            assertTrue(!o.get("someVar").getAsString().contains("%"));
        }

        Iterator<JsonObject> it = elements.iterator();
        while (it.hasNext())
        {
            JsonObject object = it.next();
            String value = object.get("someVar").getAsString();
            if (value.equals("value.1.4"))
            {
                it.remove();
            }
            else if (value.equals("value.2.5"))
            {
                it.remove();
            }
            else if (value.equals("value.3.6"))
            {
                it.remove();
            }
        }
        assertEquals(0, elements.size());
    }

    @Test
    public void testForLoopEachNested()
    {
        JsonObject forLoop2 = new JsonObject();

        //values
        JsonArray values2 = new JsonArray();
        values2.add(buildLoopValue("tree", "1", "bee", "4"));
        values2.add(buildLoopValue("tree", "2", "bee", "5"));
        values2.add(buildLoopValue("tree", "3", "bee", "6"));

        forLoop2.add("values", values2);

        JsonObject forLoop = new JsonObject();

        //values
        JsonArray values = new JsonArray();
        values.add(buildLoopValue("burger", "green", "fries", "small"));
        values.add(buildLoopValue("burger", "red", "fries", "medium"));
        values.add(buildLoopValue("burger", "blue", "fries", "large"));
        forLoop.add("values", values);

        //Data
        JsonObject dataJSON = new JsonObject();
        dataJSON.add("someVar", new JsonPrimitive("value.%tree%.%bee%.%burger%.%fries%"));
        forLoop.add("data", dataJSON);

        forLoop2.add("forEach", forLoop);

        List<JsonObject> elements = new ArrayList();
        JsonForLoop.generateDataForEachLoop(forLoop2, elements, new HashMap(), 0);

        //We should have 3 entries
        assertEquals(9, elements.size());

        //All entries should have a value, not containing injection points
        for (JsonObject o : elements)
        {
            assertTrue(o.has("someVar"));
            assertTrue(!o.get("someVar").getAsString().contains("%"));
        }

        assertEquals("value.1.4.green.small", elements.get(0).get("someVar").getAsString());
        assertEquals("value.1.4.red.medium", elements.get(1).get("someVar").getAsString());
        assertEquals("value.1.4.blue.large", elements.get(2).get("someVar").getAsString());
        assertEquals("value.2.5.green.small", elements.get(3).get("someVar").getAsString());
        assertEquals("value.2.5.red.medium", elements.get(4).get("someVar").getAsString());
        assertEquals("value.2.5.blue.large", elements.get(5).get("someVar").getAsString());
        assertEquals("value.3.6.green.small", elements.get(6).get("someVar").getAsString());
        assertEquals("value.3.6.red.medium", elements.get(7).get("someVar").getAsString());
        assertEquals("value.3.6.blue.large", elements.get(8).get("someVar").getAsString());
    }

    @Test
    public void testForLoopNested3()
    {
        JsonObject forLoop2 = buildLoop("i", 0, 2);
        JsonObject forLoop = new JsonObject();

        //values
        JsonArray values = new JsonArray();
        values.add(buildLoopValue("burger", "green", "fries", "small"));
        values.add(buildLoopValue("burger", "red", "fries", "medium"));
        values.add(buildLoopValue("burger", "blue", "fries", "large"));
        forLoop.add("values", values);

        //Data
        JsonObject dataJSON = new JsonObject();
        dataJSON.add("someVar", new JsonPrimitive("value.%i%.%burger%.%fries%"));
        forLoop.add("data", dataJSON);

        forLoop2.add("forEach", forLoop);

        List<JsonObject> elements = new ArrayList();
        JsonForLoop.generateDataForLoop(forLoop2, elements, new HashMap(), 0);

        //We should have 3 entries
        assertEquals(9, elements.size());

        //All entries should have a value, not containing injection points
        for (JsonObject o : elements)
        {
            assertTrue(o.has("someVar"));
            assertTrue(!o.get("someVar").getAsString().contains("%"));
        }

        assertEquals("value.0.green.small", elements.get(0).get("someVar").getAsString());
        assertEquals("value.0.red.medium", elements.get(1).get("someVar").getAsString());
        assertEquals("value.0.blue.large", elements.get(2).get("someVar").getAsString());
        assertEquals("value.1.green.small", elements.get(3).get("someVar").getAsString());
        assertEquals("value.1.red.medium", elements.get(4).get("someVar").getAsString());
        assertEquals("value.1.blue.large", elements.get(5).get("someVar").getAsString());
        assertEquals("value.2.green.small", elements.get(6).get("someVar").getAsString());
        assertEquals("value.2.red.medium", elements.get(7).get("someVar").getAsString());
        assertEquals("value.2.blue.large", elements.get(8).get("someVar").getAsString());
    }

    @Test
    public void testForLoopNested4()
    {
        JsonObject forLoop = new JsonObject();

        //values
        JsonArray values = new JsonArray();
        values.add(buildLoopValue("burger", "green", "fries", "small"));
        values.add(buildLoopValue("burger", "red", "fries", "medium"));
        values.add(buildLoopValue("burger", "blue", "fries", "large"));
        forLoop.add("values", values);

        JsonObject forLoop2 = buildLoop("i", 0, 2);

        //Data
        JsonObject dataJSON = new JsonObject();
        dataJSON.add("someVar", new JsonPrimitive("value.%burger%.%fries%.%i%"));
        forLoop2.add("data", dataJSON);

        forLoop.add("for", forLoop2);

        List<JsonObject> elements = new ArrayList();
        JsonForLoop.generateDataForEachLoop(forLoop, elements, new HashMap(), 0);

        //We should have 3 entries
        assertEquals(9, elements.size());

        //All entries should have a value, not containing injection points
        for (JsonObject o : elements)
        {
            assertTrue(o.has("someVar"));
            assertTrue(!o.get("someVar").getAsString().contains("%"));
        }

        assertEquals("value.green.small.0", elements.get(0).get("someVar").getAsString());
        assertEquals("value.green.small.1", elements.get(1).get("someVar").getAsString());
        assertEquals("value.green.small.2", elements.get(2).get("someVar").getAsString());
        assertEquals("value.red.medium.0", elements.get(3).get("someVar").getAsString());
        assertEquals("value.red.medium.1", elements.get(4).get("someVar").getAsString());
        assertEquals("value.red.medium.2", elements.get(5).get("someVar").getAsString());
        assertEquals("value.blue.large.0", elements.get(6).get("someVar").getAsString());
        assertEquals("value.blue.large.1", elements.get(7).get("someVar").getAsString());
        assertEquals("value.blue.large.2", elements.get(8).get("someVar").getAsString());
    }

    private JsonObject buildLoopValue(String... strings)
    {
        assertTrue(strings.length % 2 == 0);

        JsonObject object = new JsonObject();
        for (int i = 0; i < strings.length; i += 2)
        {
            object.add(strings[i], new JsonPrimitive(strings[i + 1]));
        }

        return object;
    }

    private JsonObject buildLoop(String id, int start, int end)
    {
        JsonObject forLoopJSON2 = new JsonObject();
        forLoopJSON2.add("start", new JsonPrimitive(start));
        forLoopJSON2.add("end", new JsonPrimitive(end));
        forLoopJSON2.add("id", new JsonPrimitive(id));
        return forLoopJSON2;
    }
}
