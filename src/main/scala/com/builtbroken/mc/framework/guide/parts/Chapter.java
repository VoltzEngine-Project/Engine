package com.builtbroken.mc.framework.guide.parts;

import com.builtbroken.mc.framework.guide.GuideEntry;
import com.builtbroken.mc.framework.guide.parts.imp.GuidePartContainer;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;

/**
 * Single chapter in the guide book
 * <p>
 * Normal a chapter would be focused on a single item or concept. However, it doesn't matter so long as the content
 * helps the user.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class Chapter extends GuidePartContainer<Book, Section>
{
    public Chapter(IJsonProcessor processor)
    {
        super(processor);
    }

    @Override
    public GuideEntry getGuideEntry()
    {
        return parent != null ? parent.getGuideEntry().getWithChapter(id) : new GuideEntry(null, id, null, null);
    }

    @Override
    public Chapter add(Section section)
    {
        super.add(section);
        section.parent = this;
        return this;
    }
}
