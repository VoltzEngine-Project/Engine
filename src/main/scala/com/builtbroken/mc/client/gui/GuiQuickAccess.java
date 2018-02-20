package com.builtbroken.mc.client.gui;

import com.builtbroken.mc.framework.access.global.GlobalAccessSystem;
import com.builtbroken.mc.framework.guide.GuideBookModule;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.prefab.gui.GuiButton2;
import com.builtbroken.mc.prefab.gui.screen.GuiScreenBase;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

/**
 * Provides access to other GUIs
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/19/2018.
 */
public class GuiQuickAccess extends GuiScreenBase
{
    public static final int BUTTON_PERM = 0;
    public static final int BUTTON_BOOK = 1;
    public static final int BUTTON_SETTING = 2;

    @Override
    public void initGui()
    {
        super.initGui();
        //TODO add help icon in top right
        //TODO add quick access for each category
        //TODO organize categorizes in rows (1 row per category, with quick access to its right
        //TODO add settings button at top right to change display
        //TODO maybe add a new section?
        add(new GuiButton2(BUTTON_PERM, 5, 40, 100, 20, "Permission"));
        add(new GuiButton2(BUTTON_BOOK, 5, 80, 100, 20, "Books"));
        add(new GuiButton2(BUTTON_SETTING, 5, 120, 100, 20, "Settings").disable());
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    public void actionPerformed(GuiButton button)
    {
        final int button_id = button.id;
        if (button_id == BUTTON_PERM)
        {
            GlobalAccessSystem.openGUI(null);
        }
        else if (button_id == BUTTON_BOOK)
        {
            GuideBookModule.openGUI();
        }
        else if (button_id == BUTTON_SETTING)
        {
            //TODO open settings
        }
    }

    @Override
    public void drawScreen(int mouse_x, int mouse_y, float d)
    {
        //Draw background
        this.drawDefaultBackground();

        Color a = new Color(122, 122, 122, 143);
        Color b = new Color(122, 122, 122, 143);

        //Side bar
        this.drawGradientRect(0, 15, 109 + 4, this.height, a.getRGB(), b.getRGB());
        this.drawVerticalLine(109 + 3, 14, this.height, Color.BLACK.getRGB());

        //Header
        this.drawGradientRect(0, 0, this.width, 15, a.getRGB(), b.getRGB());
        this.drawRect(0, 14, this.width, 15, Color.BLACK.getRGB());
        final String key = "gui.voltzengine:usertools.header";
        this.drawCenteredString(this.fontRendererObj, LanguageUtility.getLocal(key), this.width / 2, 3, 16777215);

        //Draw everything else
        super.drawScreen(mouse_x, mouse_y, d);
    }
}
