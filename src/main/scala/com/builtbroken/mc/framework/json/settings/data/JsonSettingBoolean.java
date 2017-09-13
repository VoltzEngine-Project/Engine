package com.builtbroken.mc.framework.json.settings.data;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.settings.JsonSettingData;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/13/2017.
 */
public class JsonSettingBoolean extends JsonSettingData
{
    public boolean value;

    public JsonSettingBoolean(IJsonProcessor processor, String key, boolean value)
    {
        super(processor, key);
        this.value = value;
    }

    @Override
    public boolean getBoolean()
    {
        return value;
    }
}
