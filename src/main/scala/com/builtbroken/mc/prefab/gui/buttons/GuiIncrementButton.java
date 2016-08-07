package com.builtbroken.mc.prefab.gui.buttons;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/7/2016.
 */
public class GuiIncrementButton extends GuiImageButton
{
    public GuiIncrementButton(int id, int x, int y, boolean plus)
    {
        super(id, x, y, 9, 9, 54, 198 + (!plus ? 9 : 0));
    }
}
