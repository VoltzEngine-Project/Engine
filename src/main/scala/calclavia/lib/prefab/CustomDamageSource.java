package calclavia.lib.prefab;

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
    public static final CustomDamageSource electrocution = ((CustomDamageSource) new CustomDamageSource("electrocution").setDamageBypassesArmor());

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

    /** Handles wire or machine based electrocution
     * 
     * @param entity - entity to electrocute
     * @param network - network to calculate damage from, and drain energy from */
    public static void handleElectrocution(Entity entity, IElectricalNetwork network)
    {
        if (network.getRequest() > 0)
            network.setBuffer((long) Math.max(0, network.getBuffer() - network.getVoltage() * 10));
    }

    /** Electrocutes an entity, takes into account several different conditions when applying damage
     * 
     * @param entity - entity to electrocute
     * @param voltage - voltage level being used
     * @return damage applied to the entity */
    public static float electrocuteEntity(Entity entity, long voltage)
    {
        float damage = 0;
        damage = voltage / UniversalElectricity.DEFAULT_VOLTAGE;
        entity.attackEntityFrom(CustomDamageSource.electrocution, Math.min(damage, 10));

        return damage;
    }
}
