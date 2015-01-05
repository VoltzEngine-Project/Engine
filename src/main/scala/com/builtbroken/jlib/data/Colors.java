package com.builtbroken.jlib.data;

import java.awt.*;

/**
 * Simple enum of pre-defined colors
 */
public enum Colors
{
	BLACK("\u00a70", "black", new int[] { 0, 0, 0 }),
	DARK_BLUE("\u00a71", "darkBlue", new int[] { 0, 0, 170 }),
	DARK_GREEN("\u00a72", "darkGreen", new int[] { 0, 170, 0 }),
	DARK_AQUA("\u00a73", "darkAqua", new int[] { 0, 170, 170 }),
	DARK_RED("\u00a74", "darkRed", new int[] { 170, 0, 0 }),
	PURPLE("\u00a75", "purple", new int[] { 170, 0, 170 }),
	ORANGE("\u00a76", "orange", new int[] { 255, 170, 0 }),
	GREY("\u00a77", "grey", new int[] { 170, 170, 170 }),
	DARK_GREY("\u00a78", "darkGrey", new int[] { 85, 85, 85 }),
	INDIGO("\u00a79", "indigo", new int[] { 85, 85, 255 }),
	BRIGHT_GREEN("\u00a7a", "brightGreen", new int[] { 85, 255, 85 }),
	AQUA("\u00a7b", "aqua", new int[] { 85, 255, 255 }),
	RED("\u00a7c", "red", new int[] { 255, 85, 85 }),
	PINK("\u00a7d", "pink", new int[] { 255, 85, 255 }),
	YELLOW("\u00a7e", "yellow", new int[] { 255, 255, 85 }),
	WHITE("\u00a7f", "white", new int[] { 255, 255, 255 });

	/**
	 * The color code that will be displayed
	 */
	public final String code;

    //TODO replace with Color object
	public final int[] rgbCode;

	/**
	 * A friendly name of the color.
	 */
	public String unlocalizedName;

	private Colors(String s, String n, int[] rgb)
	{
		code = s;
		unlocalizedName = n;
		rgbCode = rgb;
	}

    //TODO replace with rgb getters that call to a color object
	public float getColor(int index)
	{
		return rgbCode[index] / 255F;
	}

	public Color toColor()
	{
		return new Color(rgbCode[0], rgbCode[1], rgbCode[2]);
	}

	@Override
	public String toString()
	{
		return code;
	}
}
