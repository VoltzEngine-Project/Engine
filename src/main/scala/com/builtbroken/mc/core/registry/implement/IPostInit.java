package com.builtbroken.mc.core.registry.implement;

/**
 * Created by robert on 2/8/2015.
 */
public interface IPostInit
{
    /** Called from the mod that registered this object.
     * Use this method trigger to register recipes */
    public void onPostInit();
}
