package com.builtbroken.mc.prefab.gui;

import com.builtbroken.mc.lib.transform.vector.Point;
import net.minecraft.client.gui.GuiButton;

/**
 * Wrapper/Utility version of the default MC button
 * Is also used a prefab for buttons with more features
 * <p>
 * Created by robert on 4/23/2015.
 * TODO need a class name
 */
public class GuiButton2 extends GuiButton
{
    public static final Point DEFAULT_SIZE = new Point(200, 20);

    public GuiButton2(int id, Point point, String key)
    {
        this(id, point, DEFAULT_SIZE, key);
    }

    public GuiButton2(int id, int x, int y, String key)
    {
        this(id, x, y, DEFAULT_SIZE.xi(), DEFAULT_SIZE.yi(), key);
    }

    public GuiButton2(int id, Point point, Point size, String key)
    {
        super(id, point.xi(), point.yi(), size.xi(), size.yi(), key);
    }

    public GuiButton2(int id, int x, int y, int width, int height, String key)
    {
        super(id, x, y, width, height, key);
    }

    public GuiButton2 enable()
    {
        this.enabled = true;
        return this;
    }

    public GuiButton2 disable()
    {
        this.enabled = false;
        return this;
    }

    public GuiButton2 setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }
}
