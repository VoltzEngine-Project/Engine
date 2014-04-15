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
        handleElectrocution(entity, null, network);
    }

    /** Handles wire or machine based electrocution
     * 
     * @param entity - entity to electrocute
     * @param source - source of the damage, can be null *
     * @param network - network to calculate damage from, and drain energy from */
    public static void handleElectrocution(Entity entity, Object source, IElectricalNetwork network)
    {
        handleElectrocution(entity, source, network);
    }

    /** Handles wire or machine based electrocution
     * 
     * @param entity - entity to electrocute
     * @param source - source of the damage, can be null *
     * @param network - network to calculate damage from, and drain energy from
     * @param percent - percent of damage to apply 1 = 100% */
    public static void handleElectrocution(Entity entity, Object source, IElectricalNetwork network, float percent)
    {
        if (network != null && network.getRequest() > 0 && network.getVoltage() != 0)
        {
            electrocuteEntity(entity, source, Math.abs(network.getVoltage()), percent);
            network.setBuffer((long) Math.max(0, network.getBuffer() - network.getVoltage() * 10));
        }
    }

    /** Electrocutes an entity, takes into account several different conditions when applying damage
     * 
     * @param entity - entity to electrocute
     * @param voltage - voltage level being used
     * @return damage applied to the entity */
    public static float electrocuteEntity(Entity entity, Object source, long voltage, float percent)
    {
        float damage = 0;
        /* Damage is percent based of default voltage, this prevents insta-killing entities */
        damage = voltage / UniversalElectricity.DEFAULT_VOLTAGE;

        /* Apply percentage from the source */
        damage *= percent;

        if (damage > 0)
            entity.attackEntityFrom(CustomDamageSource.electrocution, Math.min(damage, 10));
        return damage;
    }
}
