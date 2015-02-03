package com.builtbroken.mc.api.explosive;

import net.minecraft.nbt.NBTTagCompound;

/**
 * An object that contains a reference to IExplosive. Carried by explosives, grenades and missile
 * entities etc.
 *
 * @author Calclavia, Darkguardsman
 */
public interface IExplosiveContainer
{
    /** Registered explosive handler */
	public IExplosive getExplosive();
}
