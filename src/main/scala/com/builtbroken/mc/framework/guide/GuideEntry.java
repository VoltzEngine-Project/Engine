package com.builtbroken.mc.framework.guide;

/**
 * Acts as an entry to a page in the guide book.
 * <p>
 * Can be used to reference higher parts than just the page.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/16/2018.
 */
public class GuideEntry
{
    public final String book;
    public final String chapter;
    public final String section;
    public final String page;

    private String id;

    public GuideEntry(String book, String chapter, String section, String page)
    {
        this.book = book;
        this.chapter = chapter;
        this.section = section;
        this.page = page;
    }

    /**
     * Unique String ID of the entry
     *
     * @return
     */
    public String id()
    {
        if (id == null)
        {
            id = book;
            if (chapter != null)
            {
                id += "." + chapter;
                if (section != null)
                {
                    id += "." + section;
                    if (page != null)
                    {
                        id += "." + page;
                    }
                }
            }
        }
        return id;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object.getClass() == GuideEntry.class)
        {
            return ((GuideEntry) object).id() == id();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return id();
    }
}
