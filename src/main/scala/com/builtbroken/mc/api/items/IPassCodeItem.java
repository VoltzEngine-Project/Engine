package com.builtbroken.mc.api.items;

import net.minecraft.item.ItemStack;

/** Very simple interface used to short, or transport a pass code for
 *  some object. Most likely the object the code is used for is a machine
 *  and the same item caries location data for the machine. The idea behind
 *  this is to prevent location data from being the only factor in linking
 *  devices. As this can lead to users hacking that data with a machines XYZ.
 *
 *  Designed to be used with {@link com.builtbroken.mc.api.items.IWorldPosItem}
 *          as a dual authentication system for linking machines
 *
 * Created by robert on 4/15/2015.
 */
public interface IPassCodeItem
{
    short getCode(ItemStack stack);

    void setCode(ItemStack stack, short code);
}
