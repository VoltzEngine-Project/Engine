package com.builtbroken.mc.prefab.damage;

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
