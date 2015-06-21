package com.builtbroken.mc.api.explosive;

/**
 * An object that contains a reference to IExplosive. Carried by explosives, grenades and missile
 * entities etc.
 *
 * @author Calclavia, Darkguardsman
 */
public interface IExplosive
{
    /** Registered explosive handler */
    IExplosiveHandler getExplosive();
}
