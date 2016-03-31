package com.builtbroken.mc.prefab.entity.selector;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2016.
 */
public class EntityDistanceSelector implements IEntitySelector
{
    public final boolean damageable;
    public final double distance;
    public final IPos3D center;

    public EntityDistanceSelector(IPos3D center, double distance, boolean damageable)
    {
        this.center = center;
        this.damageable = damageable;
        this.distance = distance;
    }

    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        if (!entity.isDead && (!damageable || !entity.isEntityInvulnerable()))
        {
            return entity.getDistance(center.x(), center.y(), center.z()) <= distance;
        }
        return false;
    }
}
