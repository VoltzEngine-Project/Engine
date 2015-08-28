package com.builtbroken.mc.core.content.resources;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.builtbroken.mc.core.content.resources.GenMaterial.*;

/**
 * List of known items to generate per material
 * Created by robert on 11/23/2014.
 */
public enum DefinedGenItems
{
    DUST("dust"),
    RUBBLE("ruble"),
    INGOT("ingot", IRON, GOLD),
    PLATE("plate", URANIUM),
    ROD("rod", URANIUM),
    GEAR("gear", URANIUM),
    AX_HEAD("head_ax", URANIUM),
    SHOVEL_HEAD("head_spade", URANIUM),
    SWORD_BLADE("head_sword", URANIUM),
    PICK_HEAD("head_pick", URANIUM),
    HOE_HEAD("head_hoe", URANIUM);

    /** Material types to ignore when generating the item */
    public final List<GenMaterial> ignoreMaterials;
    /** Name of icon file */
    public final String iconName;

    DefinedGenItems(String name, GenMaterial... mats)
    {
        this.iconName = name;
        ignoreMaterials = new ArrayList();
        if (mats != null)
        {
            for (GenMaterial mat : mats)
            {
                ignoreMaterials.add(mat);
            }
        }
    }
}
