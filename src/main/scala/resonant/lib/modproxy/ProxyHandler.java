package resonant.lib.modproxy;

import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.Loader;

/** the Object that handles the submods of the mod
 * 
 * to have the submodules work, You must register them in this class, Adding support for a submodule
 * includes only aquiring its class and throwing it in the registerModules method, this is handled
 * as such to allow turning these modules off by configuration, and disable them if the parent mod
 * is not loaded (Integration modules with other mods)
 * 
 * Replace @Mod annotation with this system and it allows better handling in the end of it
 * 
 * @since 23/02/14
 * @author tgame14 */
public class ProxyHandler
{
    private List<ICompatProxy> compatModulesList;
    private LoadPhase phase;

    /** initiate in Mod constructor */
    public ProxyHandler()
    {
        this.compatModulesList = new LinkedList<ICompatProxy>();
        this.phase = LoadPhase.PRELAUNCH;
    }

    public void applyModule(Class<?> clazz, boolean load)
    {
        if (!load)
        {
            return;
        }

        ICompatProxy subProxy = null;
        try
        {
            Object module = clazz.newInstance();

            if (module instanceof ICompatProxy)
                subProxy = (ICompatProxy) module;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (subProxy != null)
        {
            if (Loader.isModLoaded(subProxy.modId()))
            {
                compatModulesList.add(subProxy);
            }
        }
    }

    /** Call for modules late or as already existing modules, DO NOT CALL FOR REGISTERED Proxies! */
    @Deprecated
    public void applyModule(ICompatProxy module)
    {
        boolean registered = false;

        if (Loader.isModLoaded(module.modId()))
        {
            compatModulesList.add(module);
            registered = true;
        }

        if (registered)
        {
            switch (phase)
            {
                case DONE:
                    break;
                case POSTINIT:
                    module.preInit();
                    module.init();
                    module.preInit();
                    break;
                case INIT:
                    module.preInit();
                    module.init();
                    break;
                case PREINIT:

            }
        }
    }

    public void preInit()
    {
        phase = LoadPhase.PREINIT;
        for (ICompatProxy proxy : compatModulesList)
        {
            proxy.preInit();
        }

        System.out.println("subProxy list: " + compatModulesList);

    }

    public void init()
    {
        phase = LoadPhase.INIT;

        for (ICompatProxy proxy : compatModulesList)
        {
            proxy.init();
        }
    }

    public void postInit()
    {
        phase = LoadPhase.POSTINIT;
        for (ICompatProxy proxy : compatModulesList)
        {
            proxy.postInit();
        }

        phase = LoadPhase.DONE;
    }
}
