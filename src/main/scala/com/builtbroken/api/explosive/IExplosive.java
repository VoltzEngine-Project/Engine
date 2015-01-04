package com.builtbroken.api.explosive;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import com.builtbroken.api.event.TriggerCause;
import com.builtbroken.lib.world.edit.IWorldChangeAction;

import java.util.List;

/**
 * Applied to any explosive handler that created a blast. Should only create
 * one explosive instance per subtype of blast to avoid creating extra ids.
 * Use trigger to express how the blast will be created rather than creating
 * a new explosive instance per different blast.
 */
public interface IExplosive
{

	/** Attempt to trigger the explosive at the location for the trigger cause.
     * Most of the time this will be call once so avoid chance logic as it maybe not
     * get called again.
	 *
	 * @param world  The world in which the explosion takes place.
	 * @param x      The X-Coord
	 * @param y      The Y-Coord
	 * @param z      The Z-Coord
	 * @param triggerCause - object that describes what caused the explosion to try
     * @param size  - size of the explosive, will be used to set radius using ex. (size * min_radius)
     * @return instanceof IWorldChangeAction that tells what blocks and entities are to be effected
	 */
	public IWorldChangeAction createBlastForTrigger(World world, double x, double y, double z, TriggerCause triggerCause, int size, NBTTagCompound tag);

    /** Gets estimated range info for the given trigger and size
     *
     * @param stack - item that contains the explosive
     * @param lines - list to add info to display for the item tooltip
     * @return min and max pair
     */
    public void addInfoToItem(ItemStack stack, List<String> lines);

    /**
     * Called when the explosive is registered
     * @param id - name the explosive was registered with
     * @param modID - mod the explosive was registered by
     */
    public void onRegistered(String id, String modID);

    /** Gets the key to use for translating the name */
    public String getTranslationKey();

    /** Gets the id this was registered with */
    public String getID();

}
