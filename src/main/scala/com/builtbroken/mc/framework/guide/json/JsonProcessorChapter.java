package com.builtbroken.mc.framework.guide.json;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.guide.GuideBookModule;
import com.builtbroken.mc.framework.guide.parts.Chapter;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class JsonProcessorChapter extends JsonProcessor<Chapter>
{
    @Override
    protected Chapter process(final JsonElement element)
    {
        return new Chapter(this);
    }

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return GuideBookModule.JSON_GUIDE_CHAPTER;
    }

    @Override
    public String getLoadOrder()
    {
        return "after:" + GuideBookModule.JSON_GUIDE_BOOK;
    }
}
