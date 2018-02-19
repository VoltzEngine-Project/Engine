package com.builtbroken.mc.framework.guide.gui;

import com.builtbroken.mc.framework.guide.GuideEntry;
import com.builtbroken.mc.framework.guide.GuideEntryType;
import com.builtbroken.mc.framework.guide.parts.Book;
import com.builtbroken.mc.framework.guide.parts.Chapter;
import com.builtbroken.mc.framework.guide.parts.Page;
import com.builtbroken.mc.framework.guide.parts.Section;
import com.builtbroken.mc.prefab.gui.components.GuiLabel;
import com.builtbroken.mc.prefab.gui.screen.GuiScreenBase;
import net.minecraft.client.gui.GuiButton;

/**
 * Used to display the current page being viewed
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/19/2018.
 */
public class FramePage extends GuiScreenBase
{
    GuideEntry targetPage;

    Book currentBook;
    Chapter currentChapter;
    Section currentSection;
    Page currentPage;


    @Override
    public void initGui()
    {
        super.initGui();
        if(targetPage != null)
        {
            //Load data
            loadBook();

            //Setup titles
            add(new GuiLabel(0, 0, currentBook != null ? currentBook.name : "Book Title"));
            add(new GuiLabel(0, 0, currentChapter != null ? currentChapter.name : "Chapter Title"));
            add(new GuiLabel(0, 0, currentSection != null ? currentSection.name : "Section Title"));
            add(new GuiLabel(0, 0, currentPage != null ? currentPage.name : "Page Title"));

            //TODO home button -> takes the user back to the books GUI
            //TODO index button -> opens the index, keeps track of current page
            //TODO back button -> moves up one component (page -> section -> chapter -> book)

            if(targetPage.getType() == GuideEntryType.PAGE)
            {
                loadPage();
            }
            else if(targetPage.getType() == GuideEntryType.SECTION)
            {
                //TODO show description and index
            }
            else if(targetPage.getType() == GuideEntryType.CHAPTER)
            {
                //TODO show description and index
            }
            else if(targetPage.getType() == GuideEntryType.BOOK)
            {
                //TODO show book index
            }
        }
    }

    /**
     * Called during init to generate the page
     */
    protected void loadPage()
    {
        if(currentPage != null)
        {
            
        }
        else
        {
            //TODO display error (Missing page)
        }
    }

    protected void loadBook()
    {
        if(targetPage != null)
        {

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
