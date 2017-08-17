package com.builtbroken.mc.framework.recipe.cast;

import com.builtbroken.mc.core.References;

/**
 * Created by Dark on 6/23/2015.
 */
public enum CastingData
{
    INGOT(References.INGOT_VOLUME),
    NUGGET(References.INGOT_VOLUME / 16),
    BLOCK(References.INGOT_VOLUME * 9);

    public final int volume;

    CastingData(int volume)
    {
        this.volume = volume;
    }

    public String internalName()
    {
        return name().toLowerCase();
    }
}
