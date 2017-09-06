package com.builtbroken.mc.framework.json.settings.data;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.settings.JsonSettingData;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class JsonSettingInteger extends JsonSettingData
{
    public int value;

    @JsonProcessorData(value = "min", type = "int")
    public int min;

    @JsonProcessorData(value = "max", type = "int")
    public int max;

    public JsonSettingInteger(IJsonProcessor processor, String key, int value)
    {
        super(processor, key);
        this.value = value;
    }

    @Override
    public String getContentID()
    {
        return "setting." + key;
    }

    @Override
    public int getInt()
    {
        return value;
    }

    @Override
    public long getLong()
    {
        return value;
    }

    @Override
    public float getFloat()
    {
        return value;
    }

    @Override
    public double getDouble()
    {
        return value;
    }

    @Override
    public String getString()
    {
        return "" + value;
    }

    @Override
    public String toString()
    {
        return "JsonSettingInteger[" + key + " = " + value + "]@" + hashCode();
    }
}
