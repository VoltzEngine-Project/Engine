package com.builtbroken.mc.framework.json.processors.explosive;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.explosive.handler.ExplosiveData;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/10/2017.
 */
public class JsonProcessorExplosive extends JsonProcessor<ExplosiveData>
{
    public JsonProcessorExplosive()
    {
        super(ExplosiveData.class);
    }

    @Override
    public ExplosiveData process(JsonElement element)
    {
        return new ExplosiveData(this);
    }

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "explosive";
    }

    @Override
    public String getLoadOrder()
    {
        return "after:item";
    }
}
