package com.builtbroken.mc.lib.world.radar;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/5/2016.
 */
public class RadarEntity extends RadarObject<Entity>
{
    public RadarEntity(Entity referent)
    {
        super(referent);
    }

    @Override
    public World world()
    {
        if (reference != null && reference.get() != null)
        {
            return reference.get().worldObj;
        }
        return null;
    }

    @Override
    public double x()
    {
        if (reference != null && reference.get() != null)
        {
            return reference.get().posX;
        }
        return 0;
    }

    @Override
    public double y()
    {
        if (reference != null && reference.get() != null)
        {
            return reference.get().posX;
        }
        return 0;
    }

    @Override
    public double z()
    {
        if (reference != null && reference.get() != null)
        {
            return reference.get().posZ;
        }
        return 0;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Entity && reference.get() != null)
        {
            return object == reference.get() || ((Entity) object).getEntityId() == reference.get().getEntityId();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        if (reference.get() != null)
        {
            return reference.get().hashCode();
        }
        return super.hashCode();
    }
}
