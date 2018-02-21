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

    /**
     * Called to get the type of the entry.
     * <p>
     * Type is used to identify how deep in the book
     * the entry is referencing. As well how much data
     * the entry actually contains.
     * <p>
     * Ex. if entry only contains entry for book and section
     * then type would be section. As the entry can only navigate
     * to the section index.
     *
     * @return type, based on data
     */
    public GuideEntryType getType()
    {
        if (book != null)
        {
            if (chapter != null)
            {
                if (section != null)
                {
                    if (page != null)
                    {
                        return GuideEntryType.PAGE;
                    }
                    return GuideEntryType.SECTION;
                }
                return GuideEntryType.CHAPTER;
            }
            return GuideEntryType.BOOK;
        }
        return GuideEntryType.INVALID;
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

    public GuideEntry getWithChapter(String chapter)
    {
        return new GuideEntry(book, chapter, null, null);
    }

    public GuideEntry getWithSection(String section)
    {
        return new GuideEntry(book, chapter, section, null);
    }

    public GuideEntry getWithPage(String page)
    {
        return new GuideEntry(book, chapter, section, page);
    }
}
