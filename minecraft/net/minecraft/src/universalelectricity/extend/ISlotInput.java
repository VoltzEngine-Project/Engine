package net.minecraft.src.universalelectricity.extend;

import net.minecraft.src.ItemStack;

/**
 * This interface allows better interaction between tile entities with inventory by
 * letting the accessing tile entity know if a specific slot in a tile entity
 * can be accessed.
 *
 * E.g This interface will be called by the Conveyor Belts Mod to
 * see which slot can be inputed and will input an item into the specific slot.
 * @author Calclavia
 */
public interface ISlotInput
{
    /**
     * Which slots should this item belong to?
     * Return null if none can be inputed by this specific item.
     */
    public int[] getSlotInputs(ItemStack itemStack, byte side);
}
