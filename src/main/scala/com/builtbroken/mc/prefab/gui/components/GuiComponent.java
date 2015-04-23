package com.builtbroken.mc.prefab.gui.components;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.lib.transform.vector.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * Created by robert on 4/23/2015.
 */
public class GuiComponent extends Gui
{
    public Point position;
    protected boolean enabled = true;
    protected boolean visible = true;

    public GuiComponent(){}

    public GuiComponent(int x, int y)
    {
        position = new Point(x, y);
    }

    public GuiComponent(Point position)
    {
        this.position = position;
    }

    /**
     * Draws the component into the GUI
     * @param mc - Instance of the game
     * @param mouse_x - x pos of the mouse
     * @param mouse_y - y pos of the mouse
     */
    public void draw(Minecraft mc, int mouse_x, int mouse_y)
    {
        drawRect(position.xi(), position.yi(), position.xi() + 1, position.yi() + 1, Colors.BLACK.toInt());
    }

    public boolean enabled()
    {
        return enabled;
    }

    public void disable()
    {
        enabled = false;
    }

    public void enable()
    {
        enabled = true;
    }

    public boolean visible()
    {
        return visible;
    }

    public void show()
    {
        this.visible = true;
    }

    public void hide()
    {
        this.visible = false;
    }
}
