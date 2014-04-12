package calclavia.lib.compat.waila;

import calclavia.lib.prefab.tile.TileElectrical;
import mcp.mobius.waila.api.IWailaRegistrar;

/**
 * @since 21/03/14
 * @author tgame14
 */
public class WailaRegistrar
{
    public static void WailaCallBack (IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new WailaTileElectricalData(), TileElectrical.class);

    }
}
