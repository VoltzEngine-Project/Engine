package com.builtbroken.mc.prefab.entity.damage;

import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.prefab.AbstractDamageSource;

/**
 * Electrical based damage source
 *
 * @author Darkguardsman
 */
public class DamageElectrical extends AbstractDamageSource
{
    //TODO replace code calclavia removed, add Factory to setup damage type correctly
    //TODO add armor handling so metal armors cause more not less damage
    //TODO add power armor support to charge and damage the armor
    //TODO damage electric items in the player's inventory
    //TODO destory some weak items
    //TODO catch player on fire

    /** Where the energy for the electrical damage ordinated from */
    public Location sourceOfEnergy;

    public DamageElectrical()
    {
        this(null);
    }

    public DamageElectrical(Location sourceOfEnergy)
    {
        this(null, sourceOfEnergy);
    }

    public DamageElectrical(Object source, Location sourceOfEnergy)
    {
        super("electrocution", source);
        this.setDifficultyScaled();
        this.sourceOfEnergy = sourceOfEnergy;
    }
}