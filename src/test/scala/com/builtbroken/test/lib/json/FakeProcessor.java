package com.builtbroken.test.lib.json;

import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonElement;

/**
 * Fake processor used to test functionality without actual processors being used
 */
public class FakeProcessor extends JsonProcessor
{
    public final String key;

    public FakeProcessor(String key)
    {
        this.key = key;
    }

    @Override
    public String getMod()
    {
        return "mod";
    }

    @Override
    public String getJsonKey()
    {
        return key;
    }

    @Override
    public IJsonGenObject process(JsonElement element)
    {
        return null;
    }
}