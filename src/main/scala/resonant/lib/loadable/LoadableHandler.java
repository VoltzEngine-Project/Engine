package resonant.lib.loadable;

import cpw.mods.fml.common.Loader;

import java.util.HashSet;
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
	private Set<ILoadable> loadables = new HashSet();
	private LoadPhase phase = LoadPhase.PRELAUNCH;

	public enum LoadPhase
	{
		PRELAUNCH,
		PREINIT,
		INIT,
		POSTINIT,
		DONE;
	}

	/**
	 * Applies a specific ILoadable module to be loaded.
	 */
	public void applyModule(Class<?> clazz, boolean load)
	{
		if (!load)
		{
			return;
		}

		try
		{
			Object module = clazz.newInstance();

			if (module instanceof ICompatProxy)
			{
				ICompatProxy subProxy = (ICompatProxy) module;

				if (Loader.isModLoaded(subProxy.modId()))
				{
					loadables.add(subProxy);
				}
			}
			else if (module instanceof ILoadable)
			{
				loadables.add((ILoadable) module);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Call for modules late or as already existing modules, DO NOT CALL FOR REGISTERED Proxies!
	 */
	public void applyModule(ILoadable module)
	{
		loadables.add(module);

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

		for (ILoadable proxy : loadables)
		{
			proxy.preInit();
		}

	}

	public void init()
	{
		phase = LoadPhase.INIT;

		for (ILoadable proxy : loadables)
		{
			proxy.init();
		}
	}

	public void postInit()
	{
		phase = LoadPhase.POSTINIT;

		for (ILoadable proxy : loadables)
		{
			proxy.postInit();
		}

		phase = LoadPhase.DONE;
	}
}
