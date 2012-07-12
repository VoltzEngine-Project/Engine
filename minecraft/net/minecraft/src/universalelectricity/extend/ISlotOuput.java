package net.minecraft.src.universalelectricity.extend;

import net.minecraft.src.ItemStack;

/**
 * This interface allows better interaction between tile entities with inventory by
 * letting the accessing tile entity know if a specific slot in a tile entity
 * can be accessed.
 * 
 * E.g This interface will be called by the Conveyor Belts Mod to
 * see which slot can be outputed and will output an item into the specific slot.
 * @author Calclavia
 */
public interface ISlotOuput
{
	/**
	 * Which slots of this tile entity can output it's item?
	 */
	public int[] getSlotOutputs();
}
