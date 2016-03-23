package com.builtbroken.mc.prefab.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class TextField extends GuiTextField
{
    public TextField(FontRenderer fontRender, int xPos, int yPos, int width, int height)
    {
        super(fontRender, xPos, yPos, width, height);
    }

    public TextField setLength(int length)
    {
        setMaxStringLength(length);
        return this;
    }
}
