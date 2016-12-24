package com.builtbroken.mc.prefab.gui;

import com.builtbroken.mc.client.SharedAssets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * Enum of 18*18 icons as part of VE's main GUI texture sheet
 */
public enum EnumGuiIconSheet
{
    /** Empty Gui Slot */
    NONE(0, 0),
    /** Battery icon */
    BATTERY(1, 0),
    /** Liquid Fluid icon */
    LIQUID(2, 0),
    /** Gas Fluid icon */
    GAS(3, 0),
    /** Arrow */
    ARR_UP(4, 0),
    /** Arrow */
    ARR_DOWN(5, 0),
    /** Arrow */
    ARR_LEFT(6, 0),
    /** Arrow */
    ARR_RIGHT(7, 0),
    /** Arrow */
    ARR_UP_RIGHT(8, 0),
    /** Arrow */
    ARR_UP_LEFT(9, 0),
    /** Arrow */
    ARR_DOWN_LEFT(10, 0),
    /** Arrow */
    ARR_DOWN_RIGHT(11, 0),
    /** Square~ish cancel icon */
    SQUARE_CANCEL(12, 0),
    /** Circle shaped cancel icon */
    CIRCLE_CANCEL(13, 0),
    STATUS_ON(13, 1),
    STATUS_OFF(13, 2),
    STATUS_CONNECTION_LOST(13, 3);

    public final int x;
    public final int y;
    public final int width;
    public final int height;

    EnumGuiIconSheet(int row, int col)
    {
        this(col * 18, row * 18, 18, 18);
    }

    EnumGuiIconSheet(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Gui gui, int x, int y)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(SharedAssets.GUI_COMPONENTS);
        gui.drawTexturedModalRect(x, y, this.x, this.y, width, height);
    }
}