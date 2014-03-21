package calclavia.lib.compat.waila;

import calclavia.lib.modproxy.ICompatProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInterModComms;

/**
 * @since 21/03/14
 * @author tgame14
 */
public class Waila implements ICompatProxy
{
    @Override
    public void preInit ()
    {
        // nothing
    }

    @Override
    public void init ()
    {
        FMLInterModComms.sendMessage("Waila", "register", "calclavia.lib.compat.waila.WailaRegistrar.WailaCallBack");
    }

    @Override
    public void postInit ()
    {
        // nothing
    }

    @Override
    public String modId ()
    {
        return "Waila";
    }
}
