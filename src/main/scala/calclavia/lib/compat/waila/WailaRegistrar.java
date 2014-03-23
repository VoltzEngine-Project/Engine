package calclavia.lib.compat.waila;

import calclavia.lib.content.module.prefab.TileElectrical;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

/**
 * @since 21/03/14
 * @author tgame14
 */
public class WailaRegistrar
{
    public static void WailaCallBack (IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider((IWailaDataProvider) new TileElectrical(), TileElectrical.class);
    }
}
