package com.builtbroken.mc.prefab.gui.components;

import com.builtbroken.mc.lib.transform.vector.Point;

/**
 * Created by robert on 4/23/2015.
 */
public class GuiCButton extends GuiComponent
{
    public Point size;
    public String name;

    public GuiCButton(){}

    public GuiCButton(Point pos)
    {
        super(pos);
    }

    public GuiCButton(Point pos, Point size)
    {
        this(pos);
        this.size = size;
    }
    
    public GuiCButton(Point pos, Point size, String name)
    {
        this(pos, size);
        this.name = name;
    }

}
