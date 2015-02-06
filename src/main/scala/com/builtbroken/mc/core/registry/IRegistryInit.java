package com.builtbroken.mc.core.registry;

/** Called after the object has been registered threw the ModManager
 * Created by robert on 2/6/2015.
 */
public interface IRegistryInit
{
    /**
     * Called right after the block has been registered
     */
    public void onRegistered();
}
