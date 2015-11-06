package com.builtbroken.mc.prefab.entity.damage;

import com.builtbroken.mc.prefab.AbstractDamageSource;

/**
 * Damage source where the enitty takes damage threw blood loss
 * Created by robert on 1/5/2015.
 */
public class DamageBleeding extends AbstractDamageSource
{
    public DamageBleeding()
    {
        super("Bleeding");
        setDamageBypassesArmor();
        setDamageIsAbsolute();
    }
}
