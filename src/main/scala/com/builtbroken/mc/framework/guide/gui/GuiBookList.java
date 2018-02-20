package com.builtbroken.mc.framework.guide.gui;

import com.builtbroken.mc.framework.guide.GuideBookModule;
import com.builtbroken.mc.framework.guide.GuideEntry;
import com.builtbroken.mc.framework.guide.parts.Book;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.prefab.gui.GuiButton2;
import com.builtbroken.mc.prefab.gui.buttons.GuiLeftRightArrowButton;
import com.builtbroken.mc.prefab.gui.screen.GuiScreenBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Used to list all books a user has access to
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/19/2018.
 */
public class GuiBookList extends GuiScreenBase
{
    public static final int BUTTON_BACK = 0;
    /** Function to call when the page is closed, can be used to return to previous GUIs */
    protected final Consumer<GuiScreen> returnGuiHandler;

    List<GuiButton2> bookButtons = new ArrayList();

    public GuiBookList(Consumer<GuiScreen> returnGuiHandler)
    {
        this.returnGuiHandler = returnGuiHandler;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        if (returnGuiHandler != null)
        {
            add(new GuiLeftRightArrowButton(BUTTON_BACK, 2, 2, true).setOnMousePressFunction(b -> {
                returnToPrevGUI();
                return true;
            }));
        }

        bookButtons.clear();

        final int button_cols = 4;
        final int button_rows = 4;
        final int spacing_row = 20;

        int row_width = this.width - spacing_row * (button_cols + 1); //20 spacer each side, 20 space between each button
        int buttonWidth = row_width / button_cols;
        int row = 0;
        int col = 0;

        for (Book book : GuideBookModule.INSTANCE.getBooksSorted())
        {
            int x = spacing_row + row * buttonWidth + spacing_row * row;
            int y = 20 + col * 30;

            //Make button
            GuiButton2 bookButton = new GuiButton2(-1, x, y, LanguageUtility.getLocal(book.unlocalized));
            bookButton.setOnMousePressFunction(b -> {
                GuideBookModule.openGUI(new GuideEntry(book.id, null, null, null));
                return true;
            });
            bookButton.setWidth(buttonWidth);

            //Add to GUI
            bookButtons.add(add(bookButton));


            //Move forward
            col++;
            if (col >= button_cols)
            {
                col = 0;
                row++;
            }

            //Stop if we fill screen TODO replace with scroll bar
            if (row >= button_rows)
            {
                break;
            }
        }
    }


    protected void returnToPrevGUI()
    {
        if (returnGuiHandler != null)
        {
            returnGuiHandler.accept(this);
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

        //GUI title
        final String key = "gui.voltzengine:booklist.header";
        this.drawCenteredString(this.fontRendererObj, LanguageUtility.getLocal(key), this.width / 2, 3, 16777215);

        //Draw everything else
        super.drawScreen(mouse_x, mouse_y, d);
    }
}
