package resonant.lib.multiblock.synthetic;

import net.minecraft.entity.player.EntityPlayer;

/**
 * A general interface to be implemented by anything that needs it.
 *
 * @author Calclavia
 */
@Deprecated
public interface IBlockActivate
{
	/**
	 * Called when activated
	 */
	public boolean onActivated(EntityPlayer entityPlayer);
}