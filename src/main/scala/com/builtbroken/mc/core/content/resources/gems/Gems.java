package com.builtbroken.mc.core.content.resources.gems;

import com.builtbroken.mc.lib.helper.LanguageUtility;

/**
 * Enum of all gems
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/28/2016.
 */
@Deprecated //TODO move to JSON
public enum Gems
{
    /** Placeholder/Mystery Gem */
    UNKNOWN("null"),
    AMAZONITE("amazonite"),
    QUARTZ("quartz"),
    QUARTZ_SMOKEY("quartz.smoky"),
    ONYX_BLACK("onyx.black"),
    ONYX_RED("onyx.red"),
    GARNET_GREEN("garnet.green"),
    GARNET_ORANGE("garnet.orange"),
    GARNET_RED("garnet.red"),
    GARNET_YELLOW("garnet.yellow"),
    EMERALD("emerald");

    String oreName;

    String name;

    Gems(String name)
    {
        this.name = name;
    }


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
