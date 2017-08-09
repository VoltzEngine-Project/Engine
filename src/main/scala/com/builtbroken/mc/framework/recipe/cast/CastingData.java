package com.builtbroken.mc.framework.recipe.cast;

import com.builtbroken.mc.core.Engine;

/**
 * Created by Dark on 6/23/2015.
 */
public enum CastingData
{
    INGOT(Engine.INGOT_VOLUME),
    NUGGET(Engine.INGOT_VOLUME / 16),
    BLOCK(Engine.INGOT_VOLUME * 9);

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
