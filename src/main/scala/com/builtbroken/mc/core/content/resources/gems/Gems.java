package com.builtbroken.mc.core.content.resources.gems;

import com.builtbroken.mc.lib.helper.LanguageUtility;

/**
 * Enum of all gems
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/28/2016.
 */
public enum Gems
{
    /** Placeholder/Mystery Gem */
    UNKNOWN,
    AMAZONITE,
    QUARTZ,
    QUARTZ_SMOKEY,
    ONYX_BLACK,
    ONYX_RED,
    GARNET_GREEN,
    GARNET_ORANGE,
    GARNET_RED,
    GARNET_YELLOW,
    EMERALD;

    String oreName;


    public String getOreName()
    {
        if (oreName == null)
        {
            String[] split = name().split("_");

            oreName = LanguageUtility.capitalizeFirst(split[0].toLowerCase());
            if (split.length > 0)
            {
                for (int i = 1; i < split.length; i++)
                {
                    oreName += LanguageUtility.capitalizeFirst(split[i].toLowerCase());
                }
            }
        }
        return oreName;
    }
}