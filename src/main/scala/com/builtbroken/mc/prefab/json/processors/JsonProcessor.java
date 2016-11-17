package com.builtbroken.mc.prefab.json.processors;

import com.builtbroken.mc.prefab.json.imp.IJsonGenObject;
import com.google.gson.JsonElement;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public abstract class JsonProcessor<D extends IJsonGenObject>
{
    /**
     * The mod that owns this processor
     *
     * @return mod as domain name
     */
    public abstract String getMod();

    /**
     * Gets the key that this processor uses
     * for most of it's actions.
     *
     * This is mainly used to help sort
     * entries before processing so items
     * load in order.
     *
     * @return key
     */
    public abstract String getJsonKey();

    /**
     * Called to process a json block section
     *
     * @return
     */
    public boolean canProcess(String key, JsonElement element)
    {
        return key.equalsIgnoreCase(getJsonKey());
    }

    public abstract D process(JsonElement element);
}
