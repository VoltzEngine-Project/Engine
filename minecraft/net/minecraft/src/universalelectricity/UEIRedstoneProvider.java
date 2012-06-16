package net.minecraft.src.universalelectricity;

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

/**
 * This should be applied on tile entities that can provide redstone power
 * @author Henry
 *
 */
public interface UEIRedstoneProvider
{
	
	public boolean isPoweringTo(IBlockAccess par1IBlockAccess, int x, int y, int z, int side);
	
	public boolean isIndirectlyPoweringTo(World par1World, int x, int y, int z, int side);
}
