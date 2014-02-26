package calclavia.lib.modproxy;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * the Class that handles the submods of All ICBM
 *
 * to have the submodules work, You must register them in this class,
 * Adding support for a submodule includes only aquiring its class and throwing it in the
 * registerModules method, this is handled as such to allow turning these modules off by configuration,
 * and disable them if the parent mod is not loaded (Integration modules with other mods)
 *
 * Replace @Mod annotation with this system and it allows better handling in the end of it
 *
 * @since 23/02/14
 * @author tgame14
 */
public abstract class PrefabProxyHandler
{

    private static List<IMod> submodList = new LinkedList<IMod>();
    private static List<ICompatProxy> compatModulesList = new LinkedList<ICompatProxy>();
    private static LoadPhase phase = LoadPhase.PRELAUNCH;

    public static void applyModule (Class<?> clazz, boolean load)
    {
        if (!load)
        {
            return;
        }
        IMod submodule = null;
        try
        {
            Object module = clazz.newInstance();
            if (module instanceof IMod)
                submodule = (IMod) module;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (submodule != null)
        {

            if (submodule instanceof ICompatProxy)
            {
                ICompatProxy proxy = (ICompatProxy) submodule;
                if (Loader.isModLoaded(proxy.modId()))
                {
                    compatModulesList.add(proxy);
                }
            }

            else
            {
                submodList.add(submodule);
            }

        }
    }

    /** Call for modules late or as already existing modules, DO NOT CALL FOR REGISTERED Proxies! */
    public static void applyModule (ICompatProxy module)
    {
        boolean registered = false;

        if (Loader.isModLoaded(module.modId()))
        {
            compatModulesList.add((ICompatProxy) module);
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

    public static void preInit (FMLPreInitializationEvent event)
    {
        phase = LoadPhase.PREINIT;

        for (IMod submod : submodList)
        {
            submod.preInit(event);
        }

        for (ICompatProxy proxy : compatModulesList)
        {
            proxy.preInit();
        }

        System.out.println("submod list: " + submodList);

    }

    public static void init (FMLInitializationEvent event)
    {
        phase = LoadPhase.INIT;

        for (IMod submod : submodList)
        {
            submod.init(event);
        }

        for (ICompatProxy proxy : compatModulesList)
        {
            proxy.init();
        }
    }

    public static void postInit (FMLPostInitializationEvent event)
    {
        phase = LoadPhase.POSTINIT;

        for (IMod submod : submodList)
        {
            submod.postInit(event);
        }

        for (ICompatProxy proxy : compatModulesList)
        {
            proxy.postInit();
        }

        phase = LoadPhase.DONE;
        System.out.println("submod list: " + submodList);
    }
}
