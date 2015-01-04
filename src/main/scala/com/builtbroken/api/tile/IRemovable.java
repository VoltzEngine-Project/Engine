package com.builtbroken.api.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/** Automated implementation for allowing a tile to be picked up by the player.
 * This works in combination with an event based system inside of RE. In which
 * depending on the sub interface the tile can be picked up and placed inside
 * the player's inventory.
 *
 * @author Darkguardsman
 */
public abstract interface IRemovable
{
	/** Items to place in the player's inventory, or drop if the player has no space left */
	public List<ItemStack> getRemovedItems(EntityPlayer entity);

	/** Drops the tile on sneak right click with a registered wrench */
	public static interface ISneakWrenchable extends IRemovable{}

    /** Drops the tile on right click with a registered wrench */
	public static interface IWrenchable extends IRemovable{}

    /** Drops the tile on sneak right click with an empty hand */
	public static interface ISneakPickup extends IRemovable{}

    /** Drops the tile on right click with an empty hand */
	public static interface IPickup extends IRemovable{}

	/** Custom version of IRemovable that lets the tile decide if it can be picked up */
	public static interface ICustomRemoval extends IRemovable
	{
		/**
		 * Can the tile be removed by the player. Use this to force tool interaction for removal.
		 */
		public boolean canBeRemoved(EntityPlayer entity);
	}
}
