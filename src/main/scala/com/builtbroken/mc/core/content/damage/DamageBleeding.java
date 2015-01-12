package com.builtbroken.mc.core.content.damage;

import com.builtbroken.mc.prefab.AbstractDamageSource;

/**
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
