package universalelectricity.compatibility;

import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A module to extend for compatibility with other energy systems.
 *
 * @author Calclavia
 */
public class Compatibility
{
	public static final Set<CompatibilityModule> loadedModules = new LinkedHashSet<CompatibilityModule>();

	/**
	 * A cache to know which module to use with when facing objects with a specific class.
	 */
	public static final HashMap<Class, CompatibilityModule> energyHandlerCache = new HashMap<Class, CompatibilityModule>();
	public static final HashMap<Class, CompatibilityModule> energyStorageCache = new HashMap<Class, CompatibilityModule>();

	public static void register(CompatibilityModule module)
	{
		loadedModules.add(module);
	}

	public static CompatibilityModule getHandler(Object handler)
	{
		if (isHandler(handler))
		{
			return energyHandlerCache.get(handler.getClass());
		}

		return null;
	}

	/**
	 * Is this object a valid energy handler?
	 *
	 * @return True if the handler can store energy. This can be for items and blocks.
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

			for (CompatibilityModule module : loadedModules)
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

	public static CompatibilityModule getContainer(Object handler)
	{
		if (isEnergyContainer(handler))
		{
			return energyStorageCache.get(handler.getClass());
		}

		return null;
	}

	/**
	 * Is this object able to store energy?
	 *
	 * @param handler
	 * @return True if the handler can store energy. The handler MUST be a block.
	 */
	public static boolean isEnergyContainer(Object handler)
	{
		if (handler != null)
		{
			Class clazz = handler.getClass();

			if (energyStorageCache.containsKey(clazz))
			{
				return true;
			}

			for (CompatibilityModule module : loadedModules)
			{
				if (module.doIsEnergyContainer(handler))
				{
					energyStorageCache.put(clazz, module);
					return true;
				}
			}
		}

		return false;
	}

	public static abstract class CompatibilityModule
	{
		public final String modID;
		public final String moduleName;
		public final String fullUnit;
		public final String unit;

		/**
		 * Multiply UE energy by this ratio to convert it to the forgien ratio.
		 */
		public double ratio;

		/**
		 * Multiply the forgien energy by this ratio to convert it into UE energy.
		 */
		public double reciprocal_ratio;

		public boolean isEnabled;

		public CompatibilityModule(String modID, String moduleName, String fullUnit, String unit, int ratio)
		{
			this.modID = modID;
			this.moduleName = moduleName;
			this.fullUnit = fullUnit;
			this.unit = unit;
			this.ratio = 1.0 / ratio;
			this.reciprocal_ratio = ratio;
		}

		public boolean isLoaded()
		{
			return Loader.isModLoaded(this.modID);
		}

		public abstract double receiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive);

		public abstract double extractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract);

		/**
		 * Charges an item with the given energy
		 *
		 * @param itemStack - item stack that is the item
		 * @param joules    - input energy
		 * @param docharge  - do the action
		 * @return amount of energy accepted
		 */
		public abstract double chargeItem(ItemStack itemStack, double joules, boolean docharge);

		/**
		 * discharges an item with the given energy
		 *
		 * @param itemStack   - item stack that is the item
		 * @param joules      - input energy
		 * @param doDischarge - do the action
		 * @return amount of energy that was removed
		 */
		public abstract double dischargeItem(ItemStack itemStack, double joules, boolean doDischarge);

		public abstract boolean doIsHandler(Object obj);

		public abstract boolean doIsEnergyContainer(Object obj);

		public abstract double getEnergy(Object obj, ForgeDirection direction);

		public abstract boolean canConnect(Object obj, ForgeDirection direction, Object source);

		public abstract ItemStack getItemWithCharge(ItemStack itemStack, double energy);

		public abstract double getMaxEnergy(Object handler, ForgeDirection direction);

		public abstract double getEnergyItem(ItemStack is);

		public abstract double getMaxEnergyItem(ItemStack is);
	}

}
