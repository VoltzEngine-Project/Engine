package com.builtbroken.mc.framework.guide.gui;

import com.builtbroken.mc.framework.guide.GuideBookModule;
import com.builtbroken.mc.framework.guide.GuideEntry;
import com.builtbroken.mc.framework.guide.GuideEntryType;
import com.builtbroken.mc.framework.guide.parts.Book;
import com.builtbroken.mc.framework.guide.parts.Chapter;
import com.builtbroken.mc.framework.guide.parts.Page;
import com.builtbroken.mc.framework.guide.parts.Section;
import com.builtbroken.mc.prefab.gui.components.GuiLabel;
import com.builtbroken.mc.prefab.gui.screen.GuiScreenBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.function.Consumer;

/**
 * Used to display the current page being viewed
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/19/2018.
 */
public class GuiBookPage extends GuiScreenBase
{
    /** Page that is desired to be opened */
    protected final GuideEntry targetPage;
    /** Function to call when the page is closed, can be used to return to previous GUIs */
    protected final Consumer<GuiScreen> onCloseFunction;

    //Cached page data
    protected Book currentBook;
    protected Chapter currentChapter;
    protected Section currentSection;
    protected Page currentPage;

    public GuiBookPage(GuideEntry targetPage, Consumer<GuiScreen> onCloseFunction)
    {
        this.targetPage = targetPage;
        this.onCloseFunction = onCloseFunction;
    }

    @Override
    public void onGuiClosed()
    {
        if (onCloseFunction != null)
        {
            onCloseFunction.accept(this);
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        if (targetPage != null)
        {
            //Load data
            loadBook();

            //Setup titles
            add(new GuiLabel(0, 0, currentBook != null ? currentBook.name : "Book Title"));
            add(new GuiLabel(0, 20, currentChapter != null ? currentChapter.name : "Chapter Title"));
            add(new GuiLabel(0, 40, currentSection != null ? currentSection.name : "Section Title"));
            add(new GuiLabel(0, 60, currentPage != null ? currentPage.name : "Page Title"));

            //TODO home button -> takes the user back to the books GUI
            //TODO index button -> opens the index, keeps track of current page
            //TODO back button -> moves up one component (page -> section -> chapter -> book)

            if (targetPage.getType() == GuideEntryType.PAGE)
            {
                loadPageDisplay();
            }
            else if (targetPage.getType() == GuideEntryType.SECTION)
            {
                loadSectionIndex();
            }
            else if (targetPage.getType() == GuideEntryType.CHAPTER)
            {
                loadChapterIndex();
            }
            else if (targetPage.getType() == GuideEntryType.BOOK)
            {
                loadBookIndex();
            }
            else
            {
                //TODO page is invalid, either show error or send to book list
            }
        }
        else
        {
            //TODO page is invalid, either show error or send to book list
        }
    }

    /**
     * Called during init to generate the page
     */
    protected void loadPageDisplay()
    {
        if (currentPage != null)
        {
            add(new GuiLabel(0, 80, currentPage.toString()));
        }
        else
        {
            add(new GuiLabel(0, 80, "Missing data for page: " + targetPage.page));
        }
    }

    /**
     * Called during init to generate the page
     */
    protected void loadSectionIndex()
    {
        if (currentSection != null)
        {
            //TODO show description and index
            add(new GuiLabel(0, 80, currentSection.toString()));
        }
        else
        {
            add(new GuiLabel(0, 80, "Missing data for section: " + targetPage.section));
        }
    }

    /**
     * Called during init to generate the page
     */
    protected void loadChapterIndex()
    {
        if (currentChapter != null)
        {
            //TODO show description and index
            add(new GuiLabel(0, 80, currentChapter.toString()));
        }
        else
        {
            add(new GuiLabel(0, 80, "Missing data for chapter: " + targetPage.chapter));
        }
    }

    /**
     * Called during init to generate the page
     */
    protected void loadBookIndex()
    {
        if (currentSection != null)
        {
            //TODO show description and index
            add(new GuiLabel(0, 80, currentSection.toString()));
        }
        else
        {
            add(new GuiLabel(0, 80, "Missing data for book: " + targetPage.book));
        }
    }

    protected void loadBook()
    {
        if (targetPage != null)
        {
            currentBook = GuideBookModule.INSTANCE.getBook(targetPage);
            currentChapter = GuideBookModule.INSTANCE.getChapter(targetPage);
            currentSection = GuideBookModule.INSTANCE.getSection(targetPage);
            currentPage = GuideBookModule.INSTANCE.getPage(targetPage);
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    public void actionPerformed(GuiButton button)
    {

    }
}
