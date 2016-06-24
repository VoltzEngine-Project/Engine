package com.builtbroken.mc.prefab.json;

import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.prefab.json.data.JsonData;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonBlockLoader extends AbstractLoadable
{
    List<JsonData> dataFiles = new ArrayList();

    @Override
    public void preInit()
    {
        //TODO find mods that need shit loaded - MetaInf file -> Directories -> Sift
    }

    @Override
    public void init()
    {
        String string = "{\"block\":\"tree\"}";
        StringReader reader = new StringReader(string);
        JsonReader jsonReader = new JsonReader(reader);
        JsonElement element = Streams.parse(jsonReader);
        System.out.println(element.toString());
    }

    @Override
    public void postInit()
    {

    }
}
