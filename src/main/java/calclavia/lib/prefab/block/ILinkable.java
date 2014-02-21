package calclavia.lib.prefab.block;

import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.api.vector.VectorWorld;

/**
 * @author Calclavia
 * 
 */
@Deprecated
public interface ILinkable
{
	/**
	 * 
	 * @param player
	 * @param vector
	 * @return True to clear the link.
	 */
	public boolean onLink(EntityPlayer player, VectorWorld vector);
}
