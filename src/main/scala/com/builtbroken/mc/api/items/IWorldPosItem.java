package com.builtbroken.mc.api.items;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.lib.transform.vector.Location;
import net.minecraft.dispenser.ILocation;
import net.minecraft.item.ItemStack;

/** Simple way to store a location inside an Item's NBT
 *
 * Designed to be used with {@link com.builtbroken.mc.api.items.IPassCodeItem}
 *      as a dual authentication system for linking machines
 *
 * Created by robert on 4/15/2015.
 */
public interface IWorldPosItem
{
    /**
     * Retrieves the location from the NBT,
     * Creates a new object each method call
     * @return Location(World, x, y, z)
     */
    public IWorldPosition getLocation(ItemStack stack);

    /**
     * Sets the location data in the Item's NBT
     * @param loc
     */
    public void setLocation(ItemStack stack, IWorldPosition loc);
}
