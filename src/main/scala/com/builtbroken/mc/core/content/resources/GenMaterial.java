package com.builtbroken.mc.core.content.resources;

import net.minecraftforge.fluids.Fluid;

import java.awt.*;

/**
 * Generic set of ore types that are commonly found in most mods, and MC itself
 *
 * @author Darkguardsman
 */
public enum GenMaterial
{
    UNKNOWN(new Color(164, 5, 137)),
    //http://www.colorcombos.com/colors/C87533
    COPPER(new Color(222, 136, 52)),
    TIN(new Color(197, 197, 197)),
    BRONZE(new Color(151, 90, 10)),
    IRON(new Color(145, 145, 145)),
    STEEL(new Color(93, 93, 93)),
    //https://en.wikipedia.org/wiki/Silver_%28color%29
    SILVER(new Color(222, 217, 209)),
    GOLD(new Color(224, 183, 39)),
    LEAD(new Color(84, 132, 128)),
    ZINC(new Color(158, 186, 185)),
    NICKEL(new Color(173, 186, 181)),
    ALUMINIUM(new Color(151, 164, 159)),
    MAGNESIUM(new Color(133, 146, 141)),
    URANIUM(new Color(140, 196, 114)),
    BRASS(new Color(127, 118, 62)),
    STONE(new Color(93, 102, 97)),
    WOOD(new Color(97, 77, 5)),
    DIAMOND(new Color(20, 110, 100));


    /** Color to use for render the texture */
    public final Color color;

    /** Molten fluid for this material, does not apply to all types */
    public Fluid moltenFluid;

    GenMaterial(Color color)
    {
        this.color = color;
    }
}
