package com.builtbroken.mc.framework.guide.gui;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.jlib.lang.EnglishLetters;
import com.builtbroken.mc.framework.guide.GuideBookModule;
import com.builtbroken.mc.framework.guide.GuideEntry;
import com.builtbroken.mc.framework.guide.GuideEntryType;
import com.builtbroken.mc.framework.guide.parts.Book;
import com.builtbroken.mc.framework.guide.parts.Chapter;
import com.builtbroken.mc.framework.guide.parts.Page;
import com.builtbroken.mc.framework.guide.parts.Section;
import com.builtbroken.mc.lib.helper.MathUtility;
import com.builtbroken.mc.prefab.gui.buttons.GuiLeftRightArrowButton;
import com.builtbroken.mc.prefab.gui.components.GuiLabel;
import com.builtbroken.mc.prefab.gui.screen.GuiScreenBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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

            loadPageDisplay();
            if (targetPage.getType() == GuideEntryType.PAGE)
            {
                //loadPageDisplay();
            }
            else if (targetPage.getType() == GuideEntryType.SECTION)
            {
                //loadSectionIndex();
            }
            else if (targetPage.getType() == GuideEntryType.CHAPTER)
            {
                //loadChapterIndex();
            }
            else if (targetPage.getType() == GuideEntryType.BOOK)
            {
                //loadBookIndex();
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
            //add(new GuiLabel(width / 2, 80, currentPage.toString()));
        }
        else
        {
            //add(new GuiLabel(width / 2, 80, "Missing data for page: " + targetPage.page));
        }

        try
        {
            int sideSize = (int) (width * .1) + 10;
            int lineSplit = width - (sideSize * 2);


            int currentLine = 0;
            for (int i = 0; i < 4; i++)
            {
                //String textBlock = makeRandomText(200); //TODO replace with real data, as text block component
                List<String> lines = new ArrayList();//splitLines(textBlock, lineSplit);
                for(int c = 0; c < 6; c++)
                {
                    lines.add(c + ": " + makeRandomText(200));
                }

                for (String line : lines)
                {
                    //Create
                    GuiLabel label = new GuiLabel(sideSize, 20 + currentLine * 10, line);
                    label.setWidth(lineSplit);

                    //Add
                    add(label);

                    //Increment
                    currentLine++;
                }
                currentLine++;
            }
        }
        catch (Exception e)
        {
            add(new GuiLabel(20, width / 2, Colors.RED.code + "Error: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public String makeRandomText(int count)
    {
        String textBlock = "";
        for (int i = 0; i < count; i++)
        {
            if (MathUtility.rand.nextFloat() > 0.1)
            {
                textBlock += EnglishLetters.values()[MathUtility.rand.nextInt(EnglishLetters.values().length - 1)];
            }
            else
            {
                textBlock += " ";
            }
        }
        return textBlock;
    }

    public List<String> splitLines(final String textBlock, final int pixelWidth)
    {
        //Copy to not modify original
        String copy = textBlock;
        //Collect lines for display
        List<String> lines = new ArrayList();

        int index = -1;
        int index_lastSpace = -1;
        int pixels = 0;
        while (!copy.isEmpty() && ++index < copy.length())
        {
            //Exit case
            if(index + 1 == copy.length())
            {
                lines.add(copy);
                return lines;
            }

            //Get char
            final char c = copy.charAt(index);

            //Note if its a space
            if (Character.isSpaceChar(c))
            {
                index_lastSpace = index;
            }

            //Increase width tracker for line
            pixels += fontRendererObj.getCharWidth(c);


            //Split if oversize
            if (pixels > pixelWidth)
            {
                final int splitPoint = index_lastSpace == -1 ? index : index_lastSpace;
                //Clip out sub string
                final String sub = copy.substring(0, splitPoint);
                lines.add(sub);

                //Set copy to current point
                copy = copy.substring(splitPoint + 1, copy.length());

                //Reset
                index_lastSpace = -1;
                index = -1;
                pixels = 0;
            }
        }

        return lines;
    }

    /**
     * Called during init to generate the page
     */
    protected void loadSectionIndex()
    {
        if (currentSection != null)
        {
            //TODO show description and index
            add(new GuiLabel(width / 2, 80, currentSection.toString()));
        }
        else
        {
            add(new GuiLabel(width / 2, 80, "Missing data for section: " + targetPage.section));
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
            add(new GuiLabel(width / 2, 80, currentChapter.toString()));
        }
        else
        {
            add(new GuiLabel(width / 2, 80, "Missing data for chapter: " + targetPage.chapter));
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
            add(new GuiLabel(width / 2, 80, currentSection.toString()));
        }
        else
        {
            add(new GuiLabel(width / 2, 80, "Missing data for book: " + targetPage.book));
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
        final int headerSize = 14;
        final int sideSize = (int) (width * .1);
        //Draw background
        this.drawDefaultBackground();

        //Colors
        Color color_a = new Color(122, 122, 122, 143);
        Color color_b = new Color(122, 122, 122, 143);

        //Left Side bar
        this.drawGradientRect(0, headerSize + 1, sideSize + 4, this.height, color_a.getRGB(), color_b.getRGB());
        this.drawVerticalLine(sideSize + 3, headerSize, this.height, Color.BLACK.getRGB());

        //Right Side bar
        this.drawGradientRect(width - sideSize - 3, headerSize + 1, width, this.height, color_a.getRGB(), color_b.getRGB());
        this.drawVerticalLine(width - sideSize - 3, headerSize, this.height, Color.BLACK.getRGB());

        //Header
        this.drawGradientRect(0, 0, this.width, headerSize + 1, color_a.getRGB(), color_b.getRGB());
        this.drawRect(0, headerSize, this.width, 15, Color.BLACK.getRGB());

        String header = currentBook != null ? "Book: " + currentBook.unlocalized : "Book Title";
        header += " >> " + (currentChapter != null ? currentChapter.unlocalized : "Chapter Title");
        header += " >> " + (currentSection != null ? currentSection.unlocalized : "Section Title");
        header += " >> " + (currentPage != null ? currentPage.name : "Page Title");
        this.drawString(this.fontRendererObj, header, 50, 3, 16777215);

        //Middle
        color_a = new Color(45, 45, 45, 143);
        color_b = new Color(122, 122, 122, 143);

        this.drawGradientRect(sideSize + 4, headerSize + 1, width - sideSize - 3, this.height, color_a.getRGB(), color_b.getRGB());

        //Draw everything else
        super.drawScreen(mouse_x, mouse_y, d);
    }
}
