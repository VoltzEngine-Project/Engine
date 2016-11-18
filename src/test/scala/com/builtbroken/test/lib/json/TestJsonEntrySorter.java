package com.builtbroken.test.lib.json;

import com.builtbroken.mc.lib.json.JsonContentLoader;
import com.builtbroken.mc.lib.json.JsonContentLoader.JsonEntry;
import com.builtbroken.mc.lib.json.JsonContentLoader.JsonEntryComparator;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/17/2016.
 */
@RunWith(VoltzTestRunner.class)
public class TestJsonEntrySorter extends AbstractTest
{
    public void testSorting()
    {
        final List<String> list = new ArrayList();
        list.add("ammo@after:ammoType");
        list.add("block");
        list.add("ammoType@after:item");
        list.add("item");
        list.add("tile");
        list.add("clip@after:ammoType");
        list.add("gun@after:ammoType");

        final Map<String, Integer> map = JsonContentLoader.sortSortingValues(list);
        final JsonEntryComparator comparator = new JsonEntryComparator(map);

        final List<JsonEntry> jsonEntries = new ArrayList();
        final JsonEntry entry1 = new JsonEntry("ammo", "file1", null);
        final JsonEntry entry2 = new JsonEntry("ammo", "file1", null);
        final JsonEntry entry3 = new JsonEntry("ammo", "file1", null);
        final JsonEntry entry4 = new JsonEntry("ammo", "file1", null);
        final JsonEntry entry5 = new JsonEntry("ammo", "file1", null);
        final JsonEntry entry6 = new JsonEntry("ammo", "file1", null);
        final JsonEntry entry7 = new JsonEntry("ammoType", "file3", null);
        final JsonEntry entry8 = new JsonEntry("clip", "file4", null);
        final JsonEntry entry9 = new JsonEntry("item", "file2", null);
        final JsonEntry entry10 = new JsonEntry("tile", "file5", null);

        jsonEntries.add(entry1);
        jsonEntries.add(entry2);
        jsonEntries.add(entry3);
        jsonEntries.add(entry4);
        jsonEntries.add(entry5);
        jsonEntries.add(entry6);
        jsonEntries.add(entry7);
        jsonEntries.add(entry8);
        jsonEntries.add(entry9);
        jsonEntries.add(entry10);

        //Overkill but need to make sure results are always good
        for(int i = 0; i < 12; i++)
        {
            Collections.shuffle(jsonEntries);
            Collections.shuffle(jsonEntries);
            Collections.shuffle(jsonEntries);
            Collections.sort(jsonEntries, comparator);

            assertSame(entry9, jsonEntries.get(0));
            assertSame(entry7, jsonEntries.get(1));
            //Ammo in the middle
            assertSame(entry8, jsonEntries.get(8));
            assertSame(entry10, jsonEntries.get(9));
        }
    }


}
