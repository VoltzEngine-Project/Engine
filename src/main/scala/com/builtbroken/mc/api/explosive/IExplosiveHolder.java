package com.builtbroken.mc.api.explosive;

import net.minecraft.nbt.NBTTagCompound;

/** Anything that can contain an explosive in a way that the explosive can be inserted/set
 * Created by robert on 2/1/2015.
 */
public interface IExplosiveHolder extends IExplosiveContainer
{
    /**
     * Sets what explosive the container uses
     * @param ex - registered explosive handler
     * @param nbt - data used to trigger the explosive
     * @return true if it was set, false if it was rejected
     */
    public boolean setExplosive(IExplosive ex, NBTTagCompound nbt);
}
