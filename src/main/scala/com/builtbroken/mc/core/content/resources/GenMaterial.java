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
	COPPER,
	TIN,
    BRONZE,
	IRON,
    STEEL,
	SILVER,
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
