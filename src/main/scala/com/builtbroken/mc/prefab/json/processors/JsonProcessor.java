package com.builtbroken.mc.prefab.json.processors;

import com.builtbroken.mc.prefab.json.imp.IJsonGenObject;
import com.google.gson.JsonElement;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonProcessor<D extends IJsonGenObject>
{
    /**
     * Called to process a json block section
     * @return
     */
    public boolean canProcess(JsonElement element)
    {
        return true;
    }

    public D process(JsonElement element)
    {
        return null;
    }
}
