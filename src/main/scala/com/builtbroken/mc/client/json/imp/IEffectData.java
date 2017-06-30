package com.builtbroken.mc.client.json.imp;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/12/2017.
 */
public interface IEffectData
{
    default void trigger(World world, double x, double y, double z, double mx, double my, double mz, boolean endPoint)
    {
        trigger(world, x, y, z, mx, my, mz, endPoint, getNbt());
    }

    default void trigger(World world, double x, double y, double z, double mx, double my, double mz, boolean endPoint, NBTTagCompound nbt)
    {

    }

    default NBTTagCompound getNbt()
    {
        return new NBTTagCompound();
    }
}
