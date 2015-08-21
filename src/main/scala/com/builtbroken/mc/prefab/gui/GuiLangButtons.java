package com.builtbroken.mc.prefab.gui;

import com.builtbroken.mc.lib.helper.LanguageUtility;
import net.minecraft.client.gui.GuiButton;

/** Version of MC's button that auto translates and positions using a lang file
 * Created by robert on 4/23/2015.
 */
public class GuiLangButtons extends GuiButton
{
    String key;

    public GuiLangButtons(int id, int x, int y, String key)
    {
        this(id, x, y, 200, 20, key);
    }

    public GuiLangButtons(int id, int x, int y, int w, int h, String key)
    {
        super(id, x, y, LanguageUtility.getLangSetting(key +".width", w), h, LanguageUtility.getLocalName(key));
        this.key = key;
    }

    public void addXOffset(int a)
    {
        xPosition += LanguageUtility.getLangSetting(key +".x", a);
    }
}
