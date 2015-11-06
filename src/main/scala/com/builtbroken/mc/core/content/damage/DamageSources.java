package com.builtbroken.mc.core.content.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

/** Enum of commonly used damage sources
 * Created by robert on 1/5/2015.
 */
@Deprecated
public enum DamageSources
{
    //TODO populate enum with MC and other damage sources
    //TODO use a pass threw interface to create new damage sources for entities/tiles
    ELECTRIC(new DamageElectrical()),
    BLEEDING(new DamageBleeding());

    private DamageSource source;

    DamageSources(DamageSource source)
    {
        this.source = source;
    }

    public DamageSource getSource(Object cause)
    {
        //TODO filter and return source based on cause
        return source;
    }

    public DamageSource getSource()
    {
        return source;
    }

    public interface EntitySource
    {
        DamageSource getDamageSource(Entity entity);
    }
}
