package resonant.api.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/** Applied to armor(or tools) that protected the player from electrical damage. In the case of tools
 * it will only check if the player breaks something that shocks when on break.
 * 
 * @author Darkguardsman */
public interface IInsulatedArmor extends IArmorSet
{
    /** @param armorStack - armor
     * @param entity - entity wearing the armor
     * @param source - source of the damage
     * @param damage - damage being applied
     * @param voltage - voltage
     * @return damage to apply */
    public float onEletricalDamage(ItemStack armorStack, EntityLivingBase entity, Object source, long voltage, float damage);

}
