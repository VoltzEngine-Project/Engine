package com.builtbroken.mc.lib.helper;

import net.minecraft.potion.Potion;

/** Utility to handle anything related to Potion.class */
public class PotionUtility
{
    /** Limit based on the max size the NBT save allows, too set it higher the save must be ASMed */
	private static final int EXTEND_LIMIT = 256;

    /** Resizes the potion array to its max limit allowing for more potion ids
     * to be created. */
	public static void resizePotionArray()
	{
        if(Potion.potionTypes.length < EXTEND_LIMIT)
        {
            Potion[] potions = new Potion[EXTEND_LIMIT];

            for (int i = 0; i < Potion.potionTypes.length; i++)
            {
                potions[i] = Potion.potionTypes[i];
            }
            //1.7.10 potion array -> "field_76425_a" in srg_name;
            ReflectionUtility.setMCFieldWithCatch(Potion.class, null, "potionTypes", potions);
        }
	}
}
