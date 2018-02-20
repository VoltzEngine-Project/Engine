package com.builtbroken.mc.framework.guide.gui;

import com.builtbroken.mc.framework.guide.GuideBookModule;
import com.builtbroken.mc.framework.guide.GuideEntry;
import com.builtbroken.mc.framework.guide.GuideEntryType;
import com.builtbroken.mc.framework.guide.parts.Book;
import com.builtbroken.mc.framework.guide.parts.Chapter;
import com.builtbroken.mc.framework.guide.parts.Page;
import com.builtbroken.mc.framework.guide.parts.Section;
import com.builtbroken.mc.prefab.gui.buttons.GuiLeftRightArrowButton;
import com.builtbroken.mc.prefab.gui.components.GuiLabel;
import com.builtbroken.mc.prefab.gui.screen.GuiScreenBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Used to display the current page being viewed
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/19/2018.
 */
public class GuiBookPage extends GuiScreenBase
{
    public static final int BUTTON_BACK = 0;

    /** Page that is desired to be opened */
    protected final GuideEntry targetPage;
    /** Function to call when the page is closed, can be used to return to previous GUIs */
    protected final Consumer<GuiScreen> returnGuiHandler;

    //Cached page data
    protected Book currentBook;
    protected Chapter currentChapter;
    protected Section currentSection;
    protected Page currentPage;

    public GuiBookPage(GuideEntry targetPage, Consumer<GuiScreen> returnGuiHandler)
    {
        this.targetPage = targetPage;
        this.returnGuiHandler = returnGuiHandler;
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
            add(new GuiLabel(0, 0, currentBook != null ? currentBook.unlocalized : "Book Title"));
            add(new GuiLabel(0, 20, currentChapter != null ? currentChapter.unlocalized : "Chapter Title"));
            add(new GuiLabel(0, 40, currentSection != null ? currentSection.unlocalized : "Section Title"));
            add(new GuiLabel(0, 60, currentPage != null ? currentPage.name : "Page Title"));

            if (returnGuiHandler != null)
            {
                add(new GuiLeftRightArrowButton(BUTTON_BACK, 2, 2, true).setOnMousePressFunction(b -> {
                    returnToPrevGUI();
                    return true;
                }));
            }

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

    protected void returnToPrevGUI()
    {
        if (returnGuiHandler != null)
        {
            returnGuiHandler.accept(this);
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

    @Override
    public void drawScreen(int mouse_x, int mouse_y, float d)
    {
        //Draw background
        this.drawDefaultBackground();

        //Colors
        final Color color_a = new Color(122, 122, 122, 143);
        final Color color_b = new Color(122, 122, 122, 143);

        //Side bar
        this.drawGradientRect(0, 15, 109 + 4, this.height, color_a.getRGB(), color_b.getRGB());
        this.drawVerticalLine(109 + 3, 14, this.height, Color.BLACK.getRGB());

        //Header
        this.drawGradientRect(0, 0, this.width, 15, color_a.getRGB(), color_b.getRGB());
        this.drawRect(0, 14, this.width, 15, Color.BLACK.getRGB());

        //Draw everything else
        super.drawScreen(mouse_x, mouse_y, d);
    }
}
