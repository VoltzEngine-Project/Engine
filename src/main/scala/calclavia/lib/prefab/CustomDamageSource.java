package calclavia.lib.prefab;

import calclavia.lib.prefab.damage.ElectricalDamage;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.electricity.IElectricalNetwork;

/** Extend this class to create more custom damage sources.
 * 
 * @author Calclavia */
public class CustomDamageSource extends DamageSource
{
    /** Use this damage source for all types of electrical attacks. */
    public static ElectricalDamage electrocution = new ElectricalDamage(null);

    public CustomDamageSource(String damageType)
    {
        super(damageType);
    }

    @Override
    public DamageSource setDamageBypassesArmor()
    {
        return super.setDamageBypassesArmor();
    }

    @Override
    public DamageSource setDamageAllowedInCreativeMode()
    {
        return super.setDamageAllowedInCreativeMode();
    }

    @Override
    public DamageSource setFireDamage()
    {
        return super.setFireDamage();
    }

    
}
