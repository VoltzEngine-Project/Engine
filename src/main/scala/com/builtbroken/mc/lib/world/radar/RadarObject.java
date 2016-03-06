package com.builtbroken.mc.lib.world.radar;

import com.builtbroken.mc.api.IWorldPosition;
import net.minecraft.world.ChunkCoordIntPair;

import java.lang.ref.WeakReference;

/**
 * Special type of weak reference used to track radar objects. This prevents the radar system from holding on to
 * references that should be unloaded from the map.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/5/2016.
 */
public abstract class RadarObject<J> implements IWorldPosition
{
    protected final WeakReference<J> reference;

    public RadarObject(J referent)
    {
        this.reference = new WeakReference<J>(referent);
    }

    /**
     * Is the radar object valid?
     *
     * @return true if the object is valid, normally a null check
     */
    public boolean isValid()
    {
        return reference != null && reference.get() != null;
    }

    public ChunkCoordIntPair getChunkCoordIntPair()
    {
        return  new ChunkCoordIntPair((int)x() >> 4, (int)z() >> 4);
    }
}
