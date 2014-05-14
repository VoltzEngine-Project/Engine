package resonant.lib.prefab.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import resonant.api.armor.IInsulatedArmor;
import resonant.lib.prefab.CustomDamageSource;
import resonant.lib.prefab.potion.CustomPotionEffect;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.electricity.IElectricalNetwork;

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
        handleElectrocution(entity, source, network, 1);
    }

    /** Handles wire or machine based electrocution
     * 
     * @param entity - entity to electrocute
     * @param source - source of the damage, can be null *
     * @param network - network to calculate damage from, and drain energy from
     * @param percent - percent of damage to apply 1 = 100% */
    public static void handleElectrocution(Entity entity, Object source, IElectricalNetwork network, float percent)
    {
        if (network != null && network.getRequest() > 0 && network.getBuffer() > 0 && network.getVoltage() != 0)
        {
            electrocuteEntity(entity, source, network.getVoltage(), percent);
            network.setBuffer(Math.max(0, network.getBuffer() - network.getVoltage() * 10));
        }
    }

    /** Electrocutes an entity, takes into account several different conditions when applying damage
     * 
     * @param entity - entity to electrocute
     * @param voltage - voltage level being used
     * @return damage applied to the entity */
    public static float electrocuteEntity(Entity entity, Object source, long voltage, float percent)
    {
        if (entity instanceof EntityLivingBase)
        {
            /* Damage is percent based of default voltage, this prevents insta-killing entities */
            float damage = Math.abs(voltage / UniversalElectricity.DEFAULT_VOLTAGE) * percent;
            DamageSource sourceDamage = new ElectricalDamage(source);
            damage = handleArmorReduction(entity, sourceDamage, voltage, damage);

            /* No damage no method calls */
            if (damage > 0)
            {
                entity.attackEntityFrom(sourceDamage, Math.min(damage, 15));
                if (entity instanceof EntityLivingBase && damage >= 2)
                {
                    ((EntityLivingBase) entity).addPotionEffect(new CustomPotionEffect(Potion.confusion.id, (int) ((voltage / 120) * 20), 1));
                    ((EntityLivingBase) entity).addPotionEffect(new CustomPotionEffect(Potion.weakness.id, (int) ((voltage / 120) * 20), 1));
                    ((EntityLivingBase) entity).addPotionEffect(new CustomPotionEffect(Potion.digSlowdown.id, (int) ((voltage / 120) * 20), 1));
                }
            }

            return damage >= 0 ? damage : 0;
        }
        return 0;
    }

    //TODO turn this into an armor handler
    public static float handleArmorReduction(Entity entity, DamageSource source, long voltage, float damage)
    {

        //TODO add support for normal mobs
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer entityPlayer = (EntityPlayer) entity;
            /** Average damage when the player wears a full armor set */
            float averageDamage = 0;
            /** Damage to be applied */
            float appliedDamage = damage;
            /** Is the character wearing a full suit of the same armor */
            boolean fullSuit = true;
            boolean hadArmor = false;

            /* Check for full suit of protected armor */
            for (int armorSlot = 0; armorSlot < entityPlayer.inventory.armorInventory.length; armorSlot++)
            {
                if (entityPlayer.inventory.armorInventory[armorSlot] != null)
                {
                    if (entityPlayer.inventory.armorInventory[armorSlot].getItem() instanceof IInsulatedArmor)
                    {
                        IInsulatedArmor insulatedArmor = (IInsulatedArmor) entityPlayer.inventory.armorInventory[armorSlot].getItem();
                        float d = insulatedArmor.onEletricalDamage(entityPlayer.inventory.armorInventory[armorSlot], (EntityLivingBase) entity, source, voltage, damage);
                        hadArmor = true;
                        if (insulatedArmor.areAllPartsNeeded(entityPlayer.inventory.armorInventory[armorSlot], (EntityLivingBase) entity, source, "Electrical", voltage))
                        {
                            averageDamage += d;
                            if (armorSlot > 0)
                            {
                                if (!insulatedArmor.isPartOfSet(entityPlayer.inventory.armorInventory[armorSlot], entityPlayer.inventory.armorInventory[armorSlot - 1]))
                                    fullSuit = false;
                            }
                        }
                        else
                        {
                            appliedDamage = d;
                            fullSuit = false;
                        }
                    }
                    else
                    {
                        fullSuit = false;
                    }
                }
            }
            if (fullSuit && hadArmor)
            {
                return averageDamage > 0 ? averageDamage / 4 : 0;
            }
            else
            {
                return appliedDamage;
            }
        }
        return damage;
    }

}