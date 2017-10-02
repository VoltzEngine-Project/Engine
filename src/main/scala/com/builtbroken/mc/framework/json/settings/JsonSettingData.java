package com.builtbroken.mc.framework.json.settings;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public abstract class JsonSettingData extends JsonGenData
{
    public final String key;

    @JsonProcessorData("config")
    public boolean doConfig = false;

    @JsonProcessorData("configKey")
    public String configKey;

    @JsonProcessorData("description")
    public String description;

    public JsonSettingData(IJsonProcessor processor, String key)
    {
        super(processor);
        this.key = key;
    }

    @Override
    public void onCreated()
    {
        Engine.addSetting(this);
    }

    @Override
    public String getContentID()
    {
        return "setting." + key;
    }

    public String getUniqueID()
    {
        return key;
    }

    public byte getByte()
    {
        return 0;
    }

    public short getShort()
    {
        return 0;
    }

    public int getInt()
    {
        return 0;
    }

    public long getLong()
    {
        return 0;
    }

    public float getFloat()
    {
        return 0;
    }

    public double getDouble()
    {
        return 0;
    }

    public String getString()
    {
        return null;
    }

    public boolean getBoolean()
    {
        return false;
    }
}
