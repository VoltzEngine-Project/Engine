package net.minecraft.src.universalelectricity.extend;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

/**
 * This interface should be applied to any BLOCK that can be used by the UE wrench
 *
 */
public interface IWrenchable
{
	/**
	 * Called when the player right click the wrench of this block.
	 * Make sure you have the blockActivated set to return false or it will open
	 * your block's GUI (if it has one) rather than using the wrench.
	 */
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer EntityPlayer);

    /**
	 * Called when the player right click the wrench of this block while sneaking.
	 * Make sure you have the blockActivated set to return false or it will open
	 * your block's GUI (if it has one) rather than using the wrench.
	 */
    public boolean onSneakUseWrench(World world, int x, int y, int z, EntityPlayer EntityPlayer);

}
