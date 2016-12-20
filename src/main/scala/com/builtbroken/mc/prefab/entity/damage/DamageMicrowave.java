package com.builtbroken.mc.prefab.entity.damage;

import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.prefab.AbstractDamageSource;

/**
 * Damage source for microwave radiation
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/20/2016.
 */
public class DamageMicrowave extends AbstractDamageSource
{
    /** Where the energy for the electrical damage ordinated from */
    public Location sourceOfEnergy;

    public DamageMicrowave()
    {
        this(null);
    }

    public DamageMicrowave(Location sourceOfEnergy)
    {
        super("electrocution");
        this.setDifficultyScaled();
        this.sourceOfEnergy = sourceOfEnergy;
    }

    public DamageMicrowave(Object entity, Location pos)
    {
        this(pos);
        damageSource = entity;
    }
}