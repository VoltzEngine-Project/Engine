package com.builtbroken.mc.core.registry.implement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Called after the object has been registered threw the ModManager
 * Created by robert on 2/6/2015.
 */
public interface IRegistryInit
{
    /**
     * Called right after the block has been registered
     */
    default void onRegistered()
    {

    }

    /**
     * Called client side after the block has been registered,
     * use this to register any renders
     */
    @SideOnly(Side.CLIENT)
    default void onClientRegistered()
    {

    }
}
