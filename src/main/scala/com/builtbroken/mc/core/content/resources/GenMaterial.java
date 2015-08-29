package com.builtbroken.mc.core.content.resources;

import java.awt.*;

/**
 * Generic set of ore types that are commonly found in most mods, and MC itself
 *
 * @author Darkguardsman
 */
public enum GenMaterial
{
    UNKNOWN,
    //http://www.colorcombos.com/colors/C87533
	COPPER(new Color(200, 117, 51)),
	TIN(new Color(192, 192, 192)),
    BRONZE(new Color(205, 127, 50)),
	IRON,
    STEEL,
    //https://en.wikipedia.org/wiki/Silver_%28color%29
	SILVER(new Color(192, 192, 192)),
	GOLD,
    LEAD,
    ZINC,
    NICKEL,
    ALUMINIUM,
    MAGNESIUM,
    URANIUM,
    BRASS;


    /** Color to use for render the texture */
    public Color color;

    GenMaterial(){}

    GenMaterial(Color color)
    {
        this.color = color;
    }
}
