package com.builtbroken.test.lib.json;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.JsonContentLoader;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/17/2016.
 */
@RunWith(VoltzTestRunner.class)
public class TestJsonLoader extends AbstractTest
{
    public static final String TEST_OBJECT_ONE = "{\n" +
            "  \"author\": {\n" +
            "    \"name\": \"mod\"\n" +
            "  },\n" +
            "\"block\": {\n" +
            "    \"name\": \"block\"\n" +
            "  }\n" +
            "}";

    /**
     * Tests the constructor of the method
     */
    public void testConstructor()
    {
        JsonContentLoader loader = new JsonContentLoader();
        assertTrue(loader.extensionsToLoad.contains("json"));
        assertNotNull(loader.blockProcessor);
    }

    /**
     * Tests the init methods and is
     * basicly a full run down of the system
     */
    public void testInitMethods()
    {
        JsonContentLoader loader = new JsonContentLoader();
        loader.ignoreFileLoading = true;

        //Call preInit and setup data that it needs
        References.BBM_CONFIG_FOLDER = new File("file");
        loader.preInit();
        References.BBM_CONFIG_FOLDER = null;
        //Test everything that runs in the pre int method
        assertEquals("json", loader.externalContentFolder.getName());
        assertEquals("file", loader.externalContentFolder.getParentFile().getName());

        //Check number of processors loaded
        assertEquals(6, loader.processors.size());

        //Check block is loaded, and its sub processors are loaded
        assertSame(loader.blockProcessor, loader.processors.get("block"));
        assertEquals(4, loader.blockProcessor.subProcessors.size());
        assertSame(loader.craftingRecipeProcessor, loader.blockProcessor.subProcessors.get("craftingGridRecipe"));
        assertSame(loader.furnaceRecipeProcessor, loader.blockProcessor.subProcessors.get("furnaceRecipe"));
        assertSame(loader.worldOreGenProcessor, loader.blockProcessor.subProcessors.get("worldGenerator"));

        //Check that item is loaded
        assertSame(loader.itemProcessor, loader.processors.get("item"));

        //Check that crafting is loaded
        assertSame(loader.craftingRecipeProcessor, loader.processors.get("craftingGridRecipe"));

        //Check that furnace is loaded
        assertSame(loader.furnaceRecipeProcessor, loader.processors.get("furnaceRecipe"));

        //Call init and setup data it needs
        loader.add(new FakeProcessor("ammo", "after:ammoType"));
        loader.add(new FakeProcessor("clip", "after:ammoType"));
        loader.add(new FakeProcessor("ammoType", null));

        for (int i = 0; i < 13; i++)
        {
            JsonContentLoader.loadJsonElement("file" + i, createTestElement("ammo", "ammo" + i), loader.jsonEntries);
        }
        for (int i = 0; i < 5; i++)
        {
            JsonContentLoader.loadJsonElement("file" + (13 + i), createTestElement("ammoType", "ammoType" + i), loader.jsonEntries);
        }
        for (int i = 0; i < 3; i++)
        {
            JsonContentLoader.loadJsonElement("file" + (13 + 5 + i), createTestElement("clip", "clip" + i), loader.jsonEntries);
        }

        loader.init();
        //TODO test that all files loaded correctly
        assertEquals(21, loader.generatedObjects.size());
        assertEquals(0, loader.jsonEntries.size());
        assertEquals(0, loader.externalFiles.size());
        assertEquals(0, loader.classPathResources.size());
        assertEquals(0, loader.externalJarFiles.size());

        //Post init does nothing but is still called
        loader.postInit();
    }

    public void testFileSearch()
    {

    }

    public void testResourceSearch()
    {

    }

    public void testFileLoad()
    {

    }

    public void testResourceLoad()
    {

    }

    public void testJsonElementLoad()
    {

    }

    public void testJsonLoad()
    {
        final StringReader reader = new StringReader(TEST_OBJECT_ONE);
        final HashMap<String, List<JsonContentLoader.JsonEntry>> entryList = new HashMap();

        JsonContentLoader.loadJson("someFile", reader, entryList);

        assertEquals(1, entryList.size());
        assertEquals("block", entryList.get("block").get(0).jsonKey);
        assertEquals("someFile", entryList.get("block").get(0).fileReadFrom);
        assertEquals("mod", entryList.get("block").get(0).author);
    }

    /**
     * Tests {@link JsonContentLoader#sortSortingValues(List)}
     */
    public void testSortingMap()
    {
        List<String> list = new ArrayList();
        list.add("ammo@after:ammoType");
        list.add("block");
        list.add("ammoType@after:item");
        list.add("item");
        list.add("tile");
        list.add("clip@after:ammoType");
        list.add("gun@after:ammoType");

        List<String> list2 = JsonContentLoader.sortSortingValues(list);
        assertEquals("block", list2.get(0));
        assertEquals("item", list2.get(1));
        assertEquals("ammoType", list2.get(2));
        assertEquals("ammo", list2.get(3));
        assertEquals("gun", list2.get(4));
        assertEquals("clip", list2.get(5));
        assertEquals("tile", list2.get(6));
    }


    /**
     * Generates a json element from a string of data
     *
     * @param data - valid json data
     * @return element containing processed data
     */
    private JsonElement createElement(String data)
    {
        JsonReader jsonReader = new JsonReader(new StringReader(data));
        return Streams.parse(jsonReader);
    }

    /**
     * Creates a test element that contains a single value
     * held in the value of "name" inside of a key value
     *
     * @param key  - key that matches the processor that will
     *             accept this element
     * @param name - what you will test for
     * @return generated json element
     */
    private JsonElement createTestElement(String key, String name)
    {
        String testData = "{\n" +
                "  \"author\": {\n" +
                "    \"name\": \"mod\"\n" +
                "  },\n" +
                "\"" + key + "\": {\n" +
                "    \"name\": \"" + name + "\"\n" +
                "  }\n" +
                "}";
        return createElement(testData);
    }
}
