package com.builtbroken.mc.framework.guide.parts;

import com.builtbroken.mc.framework.guide.GuideEntry;
import com.builtbroken.mc.framework.guide.parts.imp.GuidePartContainer;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class Book extends GuidePartContainer<GuidePartContainer, Chapter>
{
    public Book(IJsonProcessor processor)
    {
        super(processor);
    }

    @Override
    public GuideEntry getGuideEntry()
    {
        return new GuideEntry(id, null, null, null);
    }

    @Override
    public Book add(Chapter chapter)
    {
        super.add(chapter);
        chapter.parent = this;
        return this;
    }
}
