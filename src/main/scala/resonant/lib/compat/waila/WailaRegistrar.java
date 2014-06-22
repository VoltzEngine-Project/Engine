package resonant.lib.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import resonant.lib.prefab.tile.TileElectrical;

/**
 * @author tgame14
 * @since 21/03/14
 */
public class WailaRegistrar
{
	public static void wailaCallBack(IWailaRegistrar registrar)
	{
		registrar.registerBodyProvider(new WailaEnergyData(), TileElectrical.class);

	}
}
