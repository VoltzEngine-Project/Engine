package com.builtbroken.mc.core.content.resources;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.builtbroken.mc.core.content.resources.GenMaterial.*;

/**
 * List of known items to generate per material
 * Created by robert on 11/23/2014.
 */
public enum DefinedGenItems
{
    /** Mostly pure macerated metal */
    DUST("dust"),
    /** Impure macerated metal containing unknown other elements, most likely rock */
    DUST_IMPURE("dustImpure", WOOD, STONE, BRASS, BRONZE, STEEL),
    /** Crushed ore */
    RUBBLE("rubble", BRASS, BRONZE, STEEL),
    /** Solid bar of metal */
    INGOT("ingot", IRON, GOLD, STONE, WOOD, URANIUM),
    /** Solid 1m^2 shape of metal */
    PLATE("plate", URANIUM),
    /** Solid Cylinder of metal */
    ROD("rod", URANIUM),
    /** Circle shape with groves or teeth on the edge */
    GEAR("gear", URANIUM),
    /** Head to a small Ax */
    AX_HEAD("head_ax", URANIUM),
    /** Head to a small spade */
    SHOVEL_HEAD("head_spade", URANIUM),
    /** Blade to a standard sword */
    SWORD_BLADE("head_sword", URANIUM),
    /** Head to a miner's pickax */
    PICK_HEAD("head_pick", URANIUM),
    /** Head to a farming hoe */
    HOE_HEAD("head_hoe", URANIUM),
    /** Used to attach parts to each other. */
    SCREW("screw", URANIUM),
    /** Cable */
    WIRE("wire", URANIUM, DIAMOND),
    /** Small piece of metal */
    NUGGET("nugget", URANIUM, DIAMOND, GOLD, WOOD, STONE);

    /** Material types to ignore when generating the item */
    public final List<GenMaterial> ignoreMaterials;
    /** Name of icon file */
    public final String iconName;
    /** Name to use for localization */
    public final String name;
    /** Name to use for ore dict */
    public final String oreDict;
    /** Called inside the Engine.class to trigger loading of the item */
    private boolean requested = false;
    /** Item representing this generated item */
    public Item item;

    DefinedGenItems(String name, GenMaterial... mats)
    {
        this(name, name, name, mats);
    }

    DefinedGenItems(String name, String icon, GenMaterial... mats)
    {
        this(name, name, icon, mats);
    }

    DefinedGenItems(String name, String oreDict, String icon, GenMaterial... mats)
    {
        this.name = name;
        this.oreDict = oreDict;
        this.iconName = icon;
        ignoreMaterials = new ArrayList();
        if (mats != null)
        {
            for (GenMaterial mat : mats)
            {
                ignoreMaterials.add(mat);
            }
        }
    }

    public void requestToLoad()
    {
        this.requested = true;
    }

    public boolean isRequested()
    {
        return this.requested;
    }

    public ItemStack stack(GenMaterial mat)
    {
        return stack(mat, 1);
    }

    public ItemStack stack(GenMaterial mat, int stackSize)
    {
        if (mat == UNKNOWN)
        {
            return null;
        }

        if (this == INGOT)
        {
            if (mat == IRON)
            {
                return new ItemStack(Items.iron_ingot, stackSize, 0);
            }
            else if (mat == GOLD)
            {
                return new ItemStack(Items.gold_ingot, stackSize, 0);
            }
        }
        return new ItemStack(item, stackSize, mat.ordinal());
    }
}
