package com.builtbroken.test.lib.json;

import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;

/**
 * Fake processor used to test functionality without actual processors being used
 */
public class FakeProcessor extends JsonProcessor
{
    public final String key;
    public final String loadOrder;

    public FakeProcessor(String key, String loadOrder)
    {
        this.key = key;
        this.loadOrder = loadOrder;
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
    public String getLoadOrder()
    {
        return loadOrder;
    }

    @Override
    public IJsonGenObject process(JsonElement element)
    {
        return new FakeJsonGenObject(key, element);
    }

    public static class FakeJsonGenObject implements IJsonGenObject
    {
        public final String key;
        public final JsonElement element;
        public boolean registered = false;

        public FakeJsonGenObject(String key, JsonElement element)
        {
            this.key = key;
            this.element = element;
        }

        @Override
        public void register()
        {
            registered = true;
        }

        @Override
        public String getLoader()
        {
            return "key";
        }

        @Override
        public String getMod()
        {
            return null;
        }

        @Override
        public String getContentID()
        {
            return null;
        }
    }
}