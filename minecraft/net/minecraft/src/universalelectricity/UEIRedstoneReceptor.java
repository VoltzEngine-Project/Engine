package net.minecraft.src.universalelectricity;

import net.minecraft.src.universalelectricity.components.TileEntityBatteryBox;

/**
 * OPTIONAL
 * This interface should be applied onto all tile entities that needs to receive redstone power.
 * Look at {@link TileEntityBatteryBox} for reference.
 * @author Calclavia
 *
 */
public interface UEIRedstoneReceptor
{
	/**
	 * Called when the block is powered on by redstone
	 */
	public void onPowerOn();
	
	/**
	 * Called when the block is powered off by redstone
	 */
	public void onPowerOff();
}
