package com.builtbroken.mc.api.tile;

import net.minecraft.item.ItemStack;

/** Designed to be used with a linking tool to grab a tile's location and a pass code to
 * allowing linking to another machine. Basically an auth system.
 *
 * Created by robert on 4/15/2015.
 */
public interface IPassCode
{
    short getCode();
}
