package com.builtbroken.mc.core.content.resources.gems;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.builtbroken.mc.core.content.resources.gems.Gems.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/28/2016.
 */
public enum GemTypes
{
    //http://www.tradeshop.com/gems/howcut.html
    //http://www.wikihow.com/Cut-Gems

    /** Still attached to stone */
    ORE("ore", "GemOre"),
    /** Bit rough and dirty */
    RAW("raw", "GemRaw"),
    /** Cleaned up but not cut */
    UNCUT("uncut", "GemUncut" , EMERALD),
    /** Generalized cut */
    CUT("cut", "Gem"),
    /** Nice and shiny */
    POLISHED("polished", "PolishedGem");

    /** Material types to ignore when generating the item */
    public final List<Gems> ignoreMaterials;
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

    GemTypes(String name, Gems... mats)
    {
        this(name, name, name, mats);
    }

    GemTypes(String name, String icon, Gems... mats)
    {
        this(name, name, icon, mats);
    }

    GemTypes(String name, String oreDict, String icon, Gems... mats)
    {
        this.name = name;
        this.oreDict = oreDict;
        this.iconName = icon;
        ignoreMaterials = new ArrayList();
        if (mats != null)
        {
            for (Gems mat : mats)
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

    public ItemStack stack(Gems mat)
    {
        return stack(mat, 1);
    }

    public ItemStack stack(Gems mat, int stackSize)
    {
        if (mat == UNKNOWN)
        {
            return null;
        }

        if (this == UNCUT)
        {
            if (mat == EMERALD)
            {
                return new ItemStack(Items.emerald, stackSize, 0);
            }
            else if (mat == QUARTZ)
            {
                return new ItemStack(Items.quartz, stackSize, 0);
            }
        }
        return new ItemStack(item, stackSize, mat.ordinal());
    }
}
