package resonant.engine;

import net.minecraft.entity.player.EntityPlayer;
import resonant.lib.prefab.AbstractProxy;

/**
 * The Resonant Engine common proxy
 *
 * @author Calclavia
 */
public class CommonProxy extends AbstractProxy
{
	public boolean isPaused()
	{
		return false;
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}
}
