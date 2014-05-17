package resonant.api.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

/** Used to mark armor as a set to better handle how it works.
 * 
 * @author Darkguardsman */
public interface IArmorSet
{
    /** Basically the slot on the entity of 0-3 */
    public int getArmorType();

    /** Checks if the armor is part of the same set
     * 
     * @param armorStack - the armor item
     * @param compareStack - stack comparing if its part of the set
     * @return true if its part of the set */
    public boolean isPartOfSet(ItemStack armorStack, ItemStack compareStack);

    /** Are all armor peaces needed in order for the protection to work. This changes how damage is
     * applied. If all parts are needed then damage is averaged. If one part is needed then damage
     * is reduced per peace of armor.
     * 
     * @param armorStack - the armor item
     * @param entity - entity wearing the armor
     * @param source - source of the damage
     * @param data - array of extra data, depends on what is doing the damage. It should always
     * start with a string of what the data is going to contain. An example is "Electrical", Voltage
     * @return true if all parts are required */
    public boolean areAllPartsNeeded(ItemStack armorStack, EntityLivingBase entity, DamageSource source, Object... data);
}
