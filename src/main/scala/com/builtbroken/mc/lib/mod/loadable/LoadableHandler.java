package com.builtbroken.mc.lib.mod.loadable;

import com.builtbroken.jlib.type.Pair;
import cpw.mods.fml.common.Loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Object that handles the load calls or submods of the mod
 * <p/>
 * to have the submodules work, You must register them in this class, Adding support for a submodule
 * includes only acquiring its class and throwing it in the registerModules method, this is handled
 * as such to allow turning these modules off by configuration, and disable them if the parent mod
 * is not loaded (Integration modules with other mods)
 * <p/>
 * Replace @Mod annotation with this system and it allows better handling in the end of it
 *
 * @author tgame14, Calclavia
 * @since 23/02/14
 */
public class LoadableHandler
{
    private HashMap<ILoadable, Pair<Boolean, Boolean>> loadables = new HashMap();
    private LoadPhase phase = LoadPhase.PRELAUNCH;

    public void applyModule(Class<?> clazz)
    {
        applyModule(clazz, true);
    }

    /**
     * Applies a specific ILoadable module to be loaded.
     */
    public void applyModule(Class<?> clazz, boolean load)
    {
        if (load)
        {
            if (clazz.getAnnotation(LoadWithMod.class) != null)
            {
                String id = clazz.getAnnotation(LoadWithMod.class).mod_id();
                if (!Loader.isModLoaded(id))
                {
                    return;
                }
            }
            try
            {
                Object module = clazz.newInstance();

                if (module instanceof ILoadableProxy)
                {
                    ILoadableProxy subProxy = (ILoadableProxy) module;

                    if (subProxy.shouldLoad())
                    {
                        loadables.put(subProxy, new Pair(false, false));
                    }
                }
                else if (module instanceof ILoadable)
                {
                    loadables.put((ILoadable) module, new Pair(false, false));
                }
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Call for modules late or as already existing modules, DO NOT CALL FOR REGISTERED Proxies!
     */
    public void applyModule(ILoadable module)
    {
        loadables.put(module, new Pair(false, false));

        switch (phase)
        {
            case DONE:
                break;
            case POSTINIT:
                module.preInit();
                module.init();
                module.postInit();
                break;
            case INIT:
                module.preInit();
                module.init();
                break;
            case PREINIT:

        }
    }

    public void preInit()
    {
        phase = LoadPhase.PREINIT;

        for (Map.Entry<ILoadable, Pair<Boolean, Boolean>> proxy : loadables.entrySet())
        {
            proxy.getKey().preInit();
        }

    }

    public void init()
    {
        phase = LoadPhase.INIT;

        for (Map.Entry<ILoadable, Pair<Boolean, Boolean>> proxy : loadables.entrySet())
        {
            if(!proxy.getValue().left())
            {
                proxy.getValue().setLeft(true);
                proxy.getKey().preInit();
            }
            proxy.getKey().init();
        }
    }

    public void postInit()
    {
        phase = LoadPhase.POSTINIT;

        for (Map.Entry<ILoadable, Pair<Boolean, Boolean>> proxy : loadables.entrySet())
        {
            if(!proxy.getValue().left())
            {
                proxy.getValue().setLeft(true);
                proxy.getKey().preInit();
            }
            if(!proxy.getValue().right())
            {
                proxy.getValue().setRight(true);
        proxy.getKey().init();
    }
    proxy.getKey().postInit();
}

phase = LoadPhase.DONE;
        }

public enum LoadPhase
{
    PRELAUNCH,
    PREINIT,
        INIT,
        POSTINIT,
        DONE
    }
}
