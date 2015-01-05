package com.builtbroken.mc.api.explosive;

/**
 * An object that contains a reference to IExplosive. Carried by explosives, grenades and missile
 * entities etc.
 *
 * @author Calclavia
 */
public interface IExplosiveContainer
{
	public IExplosive getExplosive();
}
