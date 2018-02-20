package com.builtbroken.mc.framework.guide;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.guide.gui.GuiBookList;
import com.builtbroken.mc.framework.guide.gui.GuiBookPage;
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
import com.builtbroken.mc.lib.data.StringComparator;
import com.builtbroken.mc.prefab.gui.event.RestorePrevGui;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.*;

/**
 * Handles all guide book data management
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class GuideBookModule implements ILoadableProxy
{
    /** JSON key for the guide book */
    public static final String JSON_GUIDE_BOOK = "guide_book";
    /** JSON key for the guide chapter */
    public static final String JSON_GUIDE_CHAPTER = "guide_chapter";
    /** JSON key for the guide section */
    public static final String JSON_GUIDE_SECTION = "guide_section";
    /** JSON key for the guide page */
    public static final String JSON_GUIDE_PAGE = "guide_page";

    /** Primary guide book module, used by the GUIs for accessing books */
    public static final GuideBookModule INSTANCE = new GuideBookModule();

    /** Allows client to ignore duplicate entries that would normally crash the game */
    public static boolean ignoreDuplicateEntries = false;

    /** Map of book id to book instance */
    protected HashMap<String, Book> books = new HashMap();
    /** Allow mapping old book entries to new ones */
    protected HashMap<String, String> missingMappings = new HashMap();

    /** List of sorted books by name for display in the GUI */
    protected List<Book> sortedBookList = new ArrayList();
    protected boolean listNeedsSorted = true;

    /** JSON processor for handling book entries */
    public JsonProcessorBook jsonProcessorBook;
    /** JSON processor for handling chapter entries */
    public JsonProcessorChapter jsonProcessorChapter;
    /** JSON processor for handling section entries */
    public JsonProcessorSection jsonProcessorSection;
    /** JSON processor for handling page entries */
    public JsonProcessorPage jsonProcessorPage;

    @Override
    public void loadJsonContentHandlers()
    {
        //Register processors
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
                    listNeedsSorted = true;
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
                    else if (!ignoreDuplicateEntries)
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

    public Book getBook(GuideEntry entry)
    {
        return getBook(entry.book);
    }

    /**
     * Gets a book by its key
     *
     * @param id - key, forced to lower case
     * @return book if found
     */
    public Book getBook(String id)
    {
        return id != null ? books.get(id.toLowerCase()) : null;
    }

    /**
     * Gets a list of books, do not modify
     *
     * @return
     */
    public Collection<Book> getBooks()
    {
        return books.values();
    }

    /**
     * Gets a list of books that have been sorted.
     * <p>
     * Will do the actual sorting any time the list
     * has been marked as needing resorted. Keep this
     * in mind when seeing spikes in method time.
     *
     * @return list of books, sorted by key & char value
     */
    public List<Book> getBooksSorted()
    {
        if (listNeedsSorted)
        {
            //Reset trigger
            listNeedsSorted = false;

            //Clear list, both to reset values and allow re-adding new values
            sortedBookList.clear();

            //Get list of book keys
            List<String> names = new ArrayList();
            names.addAll(books.keySet());

            //Sort keys, will sort by name using chars over standard sort using length
            Collections.sort(names, new StringComparator());
        }
        return sortedBookList;
    }

    @Override
    public boolean shouldLoad()
    {
        return true; //TODO implement config to disable guide book
    }

    /**
     * Called to open the Book List GUI
     *
     */
    @SideOnly(Side.CLIENT)
    public static void openGUI()
    {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiBookList)) //TODO check previous GUI to prevent bugs (e.g. prevent opening on death screen)
        {
            //Close previous
            if (Minecraft.getMinecraft().currentScreen != null)
            {
                Minecraft.getMinecraft().currentScreen.onGuiClosed();
            }

            //Create and set profile to load
            GuiBookList gui = new GuiBookList();
            //TODO cache previous open GUI to restore that GUI

            //Open
            Minecraft.getMinecraft().displayGuiScreen(gui);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void openGUI(GuideEntry entry)
    {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiBookList)) //TODO check previous GUI to prevent bugs (e.g. prevent opening on death screen)
        {
            final GuiScreen prev_gui = Minecraft.getMinecraft().currentScreen;
            //Close previous
            if (prev_gui != null)
            {
                prev_gui.onGuiClosed();
            }

            //Create and set profile to load
            GuiBookPage gui = new GuiBookPage(entry, new RestorePrevGui(prev_gui));
            //TODO cache previous open GUI to restore that GUI

            //Open
            Minecraft.getMinecraft().displayGuiScreen(gui);
        }
    }
}
