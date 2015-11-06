package com.builtbroken.mc.prefab.entity.damage;

import com.builtbroken.mc.prefab.AbstractDamageSource;

/**
 * Damage taken from heat energy.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/6/2015.
 */
public class DamageThermal extends AbstractDamageSource
{
    /** Was the damage caused by an increase in temperature of the receiver. */
    public final boolean increase;

    public DamageThermal(boolean increase)
    {
        super("thermal");
        this.increase = increase;
    }

    public DamageThermal(boolean increase, Object source)
    {
        super("thermal", source);
        this.increase = increase;
    }
}
