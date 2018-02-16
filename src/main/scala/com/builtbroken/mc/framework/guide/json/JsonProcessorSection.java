package com.builtbroken.mc.framework.guide.json;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.guide.GuideBookModule;
import com.builtbroken.mc.framework.guide.parts.Section;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class JsonProcessorSection extends JsonProcessor<Section>
{
    @Override
    protected Section process(final JsonElement element)
    {
        return new Section(this);
    }

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return GuideBookModule.JSON_GUIDE_SECTION;
    }

    @Override
    public String getLoadOrder()
    {
        return "after:" + GuideBookModule.JSON_GUIDE_CHAPTER;
    }
}
