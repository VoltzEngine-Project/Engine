package com.builtbroken.test.lib.json;

import com.builtbroken.mc.lib.json.JsonContentLoader;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import org.junit.runner.RunWith;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/17/2016.
 */
@RunWith(VoltzTestRunner.class)
public class TestJsonLoader extends AbstractTest
{
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
        final List<JsonContentLoader.JsonEntry> entryList = new ArrayList();

        JsonContentLoader.loadJson("someFile", reader, entryList);

        assertEquals(1, entryList.size());
        assertEquals("block", entryList.get(0).jsonKey);
        assertEquals("someFile", entryList.get(0).fileReadFrom);
        assertEquals("mod", entryList.get(0).author);
    }

    /**
     * Tests {@link JsonContentLoader#sortSortingValues(List)}
     */
    public void testSorting()
    {
        List<String> list = new ArrayList();
        list.add("ammo@after:ammoType");
        list.add("block");
        list.add("ammoType@after:item");
        list.add("item");
        list.add("tile");
        list.add("clip@after:ammoType");
        list.add("gun@after:ammoType");

        Map<String, Integer> map = JsonContentLoader.sortSortingValues(list);
        assertEquals(0, (int)map.get("block"));
        assertEquals(1, (int)map.get("item"));
        assertEquals(2, (int)map.get("ammoType"));
        assertEquals(3, (int)map.get("ammo"));
        assertEquals(4, (int)map.get("gun"));
        assertEquals(5, (int)map.get("clip"));
        assertEquals(6, (int)map.get("tile"));
    }

    //Data is stored at the bottom due to size
    public static final String TEST_OBJECT_ONE = "{\n" +
            "  \"author\": {\n" +
            "    \"name\": \"mod\"\n" +
            "  },\n" +
            "\"block\": {\n" +
            "    \"name\": \"block\"\n" +
            "  }\n" +
            "}";
}
