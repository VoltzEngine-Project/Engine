package universalelectricity.api;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraftforge.common.ForgeDirection;

/**
 * A module to extend for compatibility with other energy systems.
 * 
 */
public abstract class CompatibilityModule
{
	private static final Set<CompatibilityModule> loadedModules = new LinkedHashSet<CompatibilityModule>();

	/**
	 * A cache to know which module to use with when facing objects with a specific
	 * class.
	 */
	public static final HashMap<Class, CompatibilityModule> energyHandlerCache = new HashMap<Class, CompatibilityModule>();

	public static void register(CompatibilityModule module)
	{
		loadedModules.add(module);
	}

	/**
	 * Can the handler connect to this specific direction?
	 */
	public static boolean canConnect(Object handler, ForgeDirection direction)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doCanConnect(handler, direction);
		}

		return false;
	}

	/**
	 * Make the handler receive energy.
	 * 
	 * @return The actual energy that was used.
	 */
	public static long receiveEnergy(Object handler, ForgeDirection direction, long energy, boolean doReceive)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass()).doReceiveEnergy(handler, direction, energy, doReceive);
		}

		return 0;
	}

	/**
	 * Is this object a valid energy handler?
	 * 
	 * @param handler
	 */
	public static boolean isHandler(Object handler)
	{
		if (handler != null)
		{
			Class clazz = handler.getClass();

			if (energyHandlerCache.containsKey(clazz))
			{
				return true;
			}

			for (CompatibilityModule module : CompatibilityModule.loadedModules)
			{
				if (module.doIsHandler(handler))
				{
					energyHandlerCache.put(clazz, module);
					return true;
				}
			}
		}

		return false;
	}

	public abstract long doReceiveEnergy(Object obj, ForgeDirection direction, long energy, boolean doReceive);

	public abstract boolean doIsHandler(Object obj);

	public abstract boolean doCanConnect(Object obj, ForgeDirection direction);

}
