package com.builtbroken.mc.lib.helper;

import com.builtbroken.mc.core.Engine;
import net.minecraft.potion.Potion;

/** Utility to handle anything related to Potion.class */
public class PotionUtility
{
    /** Limit based on the max size the NBT save allows, too set it higher the save must be ASMed */
    private static final int EXTEND_LIMIT = 256;

    private static int nextPotionID = 32;

    /**
     * Resizes the potion array to its max limit allowing for more potion ids
     * to be created.
     */
    public static void resizePotionArray()
    {
        if (Potion.potionTypes.length < EXTEND_LIMIT)
        {
            Potion[] potions = new Potion[EXTEND_LIMIT];

            for (int i = 0; i < Potion.potionTypes.length; i++)
            {
                potions[i] = Potion.potionTypes[i];
            }
            //1.7.10 potion array -> "field_76425_a" in srg_name;
            ReflectionUtility.setMCFieldWithCatch(Potion.class, null, "potionTypes", "field_76425_a", potions);
        }
    }

    public static int getPotionID(String potionName)
    {
        if (nextPotionID >= Potion.potionTypes.length)
        {
            throw new RuntimeException("There are not enough potion IDs to assign another ID");
        }
        if (Potion.potionTypes[nextPotionID] != null)
        {
            while (nextPotionID < Potion.potionTypes.length && Potion.potionTypes[nextPotionID] != null)
            {
                nextPotionID++;
            }
        }
        if (!Engine.loaderInstance.getConfig().hasCategory("potionIDs"))
        {
            Engine.loaderInstance.getConfig().addCustomCategoryComment("potionIDs", "This category is used by several mods to set potion IDs with minimal conflict. If a conflict happens adjust the settings below to remove the conflict. Keep in mind changing the ID may cause a different potion to be loaded from save files.");
        }
        return Engine.loaderInstance.getConfig().getInt(potionName, "potionIDs", nextPotionID, 0, Potion.potionTypes.length - 1, "ID used to save the potion");
    }
}
