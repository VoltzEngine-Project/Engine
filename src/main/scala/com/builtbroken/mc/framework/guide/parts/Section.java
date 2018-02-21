package com.builtbroken.mc.framework.guide.parts;

import com.builtbroken.mc.framework.guide.GuideEntry;
import com.builtbroken.mc.framework.guide.parts.imp.GuidePartContainer;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class Section extends GuidePartContainer<Chapter, Page>
{
    public Section(IJsonProcessor processor)
    {
        super(processor);
    }

    @Override
    public GuideEntry getGuideEntry()
    {
        return parent != null ? parent.getGuideEntry().getWithSection(id) : new GuideEntry(null, null, id, null);
    }

    @Override
    public Section add(Page page)
    {
        super.add(page);
        page.parent = this;
        return this;
    }
}
