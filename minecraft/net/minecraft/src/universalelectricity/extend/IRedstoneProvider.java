package net.minecraft.src.universalelectricity.extend;

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

/**
 * This should be applied on tile entities that can provide redstone power
 * @author Henry
 *
 */
public interface IRedstoneProvider
{
	
	public boolean isPoweringTo(byte side);
	
	public boolean isIndirectlyPoweringTo(byte side);
}
