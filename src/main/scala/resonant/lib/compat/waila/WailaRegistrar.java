package resonant.lib.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import resonant.lib.prefab.tile.TileElectrical;

/** @since 21/03/14
 * @author tgame14 */
public class WailaRegistrar
{
    public static void wailaCallBack(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new WailaEnergyData(), TileElectrical.class);

    }
}
