package com.builtbroken.mc.prefab.entity.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

/**
 * Enum of commonly used damage sources
 * Created by robert on 1/5/2015.
 */
public enum DamageSources
{
    //TODO populate enum with MC and other damage sources
    //TODO use a pass threw interface to create new damage sources for entities/tiles
    ELECTRIC(new DamageElectrical()),
    BLEEDING(new DamageBleeding()),
    THERMAL_INCREASE(new DamageThermal(true), new IEntityDamageSource()
    {
        @Override
        public DamageSource getSourceForEntity(Entity entity)
        {
            return new DamageThermal(true, entity);
        }
    }),
    THERMAL_DECREASE(new DamageThermal(false), new IEntityDamageSource()
    {
        @Override
        public DamageSource getSourceForEntity(Entity entity)
        {
            return new DamageThermal(false, entity);
        }
    }),
    /** Radiation source from a microwave */
    RAD_MICROWAVE(new DamageMicrowave()),
    /** Radiation source from a conventional radio ative sources */
    RAD_NUCLEAR(null),
    /** Radiation source */
    RAD_X_RAY(null),
    /** Radiation source */
    RAD_GAMMA(null),
    /** Radiation source, sunlight.... should only be used if source is stupidly high */
    RAD_UV(null);

    private DamageSource source;
    private IEntityDamageSource entitySource;

    DamageSources(DamageSource source)
    {
        this.source = source;
    }

    DamageSources(DamageSource source, IEntityDamageSource sourceEntity)
    {
        this(source);
        this.entitySource = sourceEntity;
    }


    public DamageSource getSource(Object cause)
    {
        if (cause instanceof Entity)
        {
            return entitySource.getSourceForEntity((Entity) cause);
        }
        return source;
    }

    public DamageSource getSource()
    {
        return source;
    }

    public interface IEntityDamageSource
    {
        DamageSource getSourceForEntity(Entity entity);
    }
}
