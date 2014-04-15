package calclavia.lib.prefab.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.electricity.IElectricalNetwork;
import calclavia.lib.prefab.CustomDamageSource;

/** Electrical based damage source
 * 
 * @author Darkguardsman */
public class ElectricalDamage extends CustomDamageSource
{

    public ElectricalDamage(Object source)
    {
        super("electrocution");
        this.setDamageBypassesArmor();
        this.setDifficultyScaled();
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
            electrocuteEntity(entity, source, network.getVoltage(), percent);
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
        if (entity instanceof EntityLiving)
        {
            /* Damage is percent based of default voltage, this prevents insta-killing entities */
            float damage = Math.abs(voltage) / UniversalElectricity.DEFAULT_VOLTAGE;

            /* Apply percentage from the source */
            damage *= percent;

            /* TODO apply potion effects */

            /* TODO do armor checks, leather does 60% reduction in damage, MPS armor shields block damage, metal armor x5 damage */

            /* No damage no method call */
            if (damage > 0)
                entity.attackEntityFrom(CustomDamageSource.electrocution, Math.min(damage, 15));
            return damage;
        }
        return -1;
    }

}