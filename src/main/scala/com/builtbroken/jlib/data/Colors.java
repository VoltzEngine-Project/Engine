package com.builtbroken.jlib.data;

import java.awt.*;

/**
 * Simple enum of pre-defined colors to keep things simple and consistent
 */
public enum Colors
{
	BLACK("\u00a70", Color.BLACK),
	DARK_BLUE("\u00a71", new Color( 0, 0, 170 )),
	DARK_GREEN("\u00a72", new Color( 0, 170, 0 )),
	DARK_AQUA("\u00a73", new Color(  0, 170, 170 )),
	DARK_RED("\u00a74", new Color(  170, 0, 0 )),
	PURPLE("\u00a75", new Color(  170, 0, 170 )),
    CORRUPTION_PURPLE("\u00a75", new Color(  71, 57, 124 )),
	ORANGE("\u00a76", new Color(  255, 170, 0 )),
	GREY("\u00a77", new Color(  170, 170, 170 )),
	DARK_GREY("\u00a78", Color.DARK_GRAY),
	INDIGO("\u00a79", new Color(  85, 85, 255 )),
	BRIGHT_GREEN("\u00a7a", new Color(  85, 255, 85 )),
	AQUA("\u00a7b", new Color(  85, 255, 255 )),
	RED("\u00a7c", Color.RED),
	PINK("\u00a7d", Color.PINK),
	YELLOW("\u00a7e",Color.YELLOW),
	WHITE("\u00a7f", Color.WHITE);

	/**
	 * The color code that will be displayed
	 */
	public final String code;

    //TODO replace with Color object
	public final Color color;

	Colors(String s, Color color)
	{
		code = s;
		this.color = color;
	}

    public Color color()
    {
        return color;
    }

	@Override
	public String toString()
	{
		return code;
	}

    public int toInt()
    {
        return getIntFromColor(color());
    }

    public static int getIntFromColor(Color color)
    {
        return getIntFromColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static int getIntFromColor(int Red, int Green, int Blue)
    {
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}
