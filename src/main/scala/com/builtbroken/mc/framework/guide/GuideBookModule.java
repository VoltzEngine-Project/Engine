package com.builtbroken.mc.framework.guide;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.guide.json.JsonProcessorBook;
import com.builtbroken.mc.framework.guide.json.JsonProcessorChapter;
import com.builtbroken.mc.framework.guide.json.JsonProcessorPage;
import com.builtbroken.mc.framework.guide.json.JsonProcessorSection;
import com.builtbroken.mc.framework.guide.parts.Book;
import com.builtbroken.mc.framework.guide.parts.Chapter;
import com.builtbroken.mc.framework.guide.parts.Page;
import com.builtbroken.mc.framework.guide.parts.Section;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.mod.loadable.ILoadableProxy;

import java.util.Collection;
import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class GuideBookModule implements ILoadableProxy
{
    public static final String JSON_GUIDE_BOOK = "guide_book";
    public static final String JSON_GUIDE_CHAPTER = "guide_chapter";
    public static final String JSON_GUIDE_SECTION = "guide_section";
    public static final String JSON_GUIDE_PAGE = "guide_page";

    public static final GuideBookModule INSTANCE = new GuideBookModule();

    public static boolean ignoreDuplicateEntires = false;

    protected HashMap<String, Book> books = new HashMap();
    /** Allow mapping old book entries to new ones */
    protected HashMap<String, String> missingMappings = new HashMap();

    public JsonProcessorBook jsonProcessorBook;
    public JsonProcessorChapter jsonProcessorChapter;
    public JsonProcessorSection jsonProcessorSection;
    public JsonProcessorPage jsonProcessorPage;

    @Override
    public void loadJsonContentHandlers()
    {
        JsonContentLoader.INSTANCE.add(jsonProcessorBook = new JsonProcessorBook());
        JsonContentLoader.INSTANCE.add(jsonProcessorChapter = new JsonProcessorChapter());
        JsonContentLoader.INSTANCE.add(jsonProcessorSection = new JsonProcessorSection());
        JsonContentLoader.INSTANCE.add(jsonProcessorPage = new JsonProcessorPage());


    }

    @Override
    public void preInit()
    {
        //Test data to confirm pages are working
        if (Engine.runningAsDev)
        {
            Book book = new Book(jsonProcessorBook).init("test", "Test Data");

            Chapter chapter1 = book.add(new Chapter(jsonProcessorChapter).init("chapter1", "Chapter 1"));
            Section section1A = chapter1.add(new Section(jsonProcessorSection).init("section1", "Section 1"));
            section1A.add(new Page(jsonProcessorPage).init("page1", "Page 1"));
            section1A.add(new Page(jsonProcessorPage).init("page2", "Page 2"));
            section1A.add(new Page(jsonProcessorPage).init("page3", "Page 3"));

            Chapter chapter2 = book.add(new Chapter(jsonProcessorChapter).init("chapter1", "Chapter 1"));
            Section section2A = chapter2.add(new Section(jsonProcessorSection).init("section1", "Section 1"));
            section2A.add(new Page(jsonProcessorPage).init("pageA", "Page A"));
            section2A.add(new Page(jsonProcessorPage).init("pageB", "Page B"));
            section2A.add(new Page(jsonProcessorPage).init("pageC", "Page C"));

            addBook(book);
        }
    }

    /**
     * Called to add a new chapter
     *
     * @param chapter
     */
    public void addBook(Book chapter)
    {
        if (chapter != null)
        {
            if (chapter.id != null && !chapter.id.trim().isEmpty())
            {
                String id = chapter.id.toLowerCase();
                if (!books.containsKey(id) || books.get(id) == null)
                {
                    this.books.put(id, chapter);
                }
                else
                {
                    Engine.logger().error("GuideBookModule >> Attempted to register duplicate book... this will causing missing pages in guide.", new RuntimeException("Duplicate guide book entry. Previous: " + getBook(id) + "  New: " + chapter));
                    if (chapter.getClass() == books.get(id).getClass())
                    {
                        if (!chapter.getPartIDs().isEmpty())
                        {
                            chapter.getPartMap().values().forEach(v -> books.get(id).add(v));
                        }
                    }
                    else if (!ignoreDuplicateEntires)
                    {
                        throw new RuntimeException("Duplicate guide book chapter entry. Previous: " + getBook(id) + "  New: " + chapter);
                    }
                }
            }
            else
            {
                Engine.logger().error("GuideBookModule >> Something tried to register a book with a null or empty id", new RuntimeException("Error registering chapter"));
            }
        }
        else
        {
            Engine.logger().error("GuideBookModule >> Something tried to register a 'null' book", new RuntimeException("Error registering chapter"));
        }
    }

    public Page getPage(GuideEntry entry)
    {
        Section section = getSection(entry);
        if (section != null)
        {
            return section.get(entry.page);
        }
        return null;
    }

    public Section getSection(GuideEntry entry)
    {
        Chapter chapter = getChapter(entry);
        if (chapter != null)
        {
            return chapter.get(entry.section);
        }
        return null;
    }

    public Chapter getChapter(GuideEntry entry)
    {
        Book book = getBook(entry.book);
        if (book != null)
        {
            return book.get(entry.chapter);
        }
        return null;
    }

    public Book getBook(String id)
    {
        return id != null ? books.get(id.toLowerCase()) : null;
    }

    public Collection<Book> getChapters()
    {
        return books.values();
    }

    @Override
    public boolean shouldLoad()
    {
        return true; //TODO implement config to disable guide book
    }
}
