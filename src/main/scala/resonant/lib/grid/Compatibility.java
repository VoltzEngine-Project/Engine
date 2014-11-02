package resonant.lib.grid;

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
	//Multiply UE by this value to convert to RF
	public static final double redstoneFluxRatio = 1;

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

	public static CompatibilityModule getHandler(Object handler, ForgeDirection dir)
	{
		if (isHandler(handler, dir))
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
	public static boolean isHandler(Object handler, ForgeDirection dir)
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
				if (module.doIsHandler(handler, dir))
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

    public static boolean canConnect(Object obj, ForgeDirection direction, Object source)
    {
        CompatibilityModule module = getHandler(obj, direction);
        if(module != null)
        {
            return module.canConnect(obj, direction, source);
        }
        return false;
    }

    /**
     * Called to give an amount of energy to the handler in the direction given
     * @param handler - tile/multipart/etc getting the energy
     * @param direction - direction in relate to the handler
     * @param energy - energy(should be possitive)
     * @param doReceive - true will actually give the energy, false will test
     * @return amount of energy received
     */
    public static double fill(Object handler, ForgeDirection direction, double energy, boolean doReceive)
    {
        return receiveEnergy(handler, direction, energy, doReceive);
    }

    /**
     * Called to give an amount of energy to the handler in the direction given
     * @param handler - tile/multipart/etc getting the energy
     * @param direction - direction in relate to the handler
     * @param energy - energy(should be possitive)
     * @param doReceive - true will actually give the energy, false will test
     * @return amount of energy received
     */
    public static double receiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive)
    {
        CompatibilityModule module = getHandler(handler, direction);
        if(module != null)
        {
            return module.receiveEnergy(handler, direction, energy, doReceive);
        }
        return 0;
    }

    public static double drain(Object handler, ForgeDirection direction, double energy, boolean doExtract)
    {
        return extractEnergy(handler, direction, energy, doExtract);
    }

    /**
     * Called to take an amount of energy from the handler in the direction given
     * @param handler - tile/multipart/etc getting the energy
     * @param direction - direction in relate to the handler
     * @param energy - energy(should be possitive)
     * @param doExtract - true will actually take the energy, false will test
     * @return amount of energy taken
     */
    public static double extractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract)
    {
        CompatibilityModule module = getHandler(handler, direction);
        if(module != null)
        {
            return module.extractEnergy(handler, direction, energy, doExtract);
        }
        return 0;
    }

    /**
     * Charge's an ItemStack with energy
     * @param itemStack - stack being charged
     * @param joules - energy to charge
     * @param doCharge - true will add the energy, false will test
     * @return energy actually charged
     */
    public static double fill(ItemStack itemStack, double joules, boolean doCharge)
    {
        return chargeItem(itemStack, joules, doCharge);
    }

    /**
     * Charge's an ItemStack with energy
     * @param itemStack - stack being charged
     * @param joules - energy to charge
     * @param doCharge - true will add the energy, false will test
     * @return energy actually charged
     */
    public static double chargeItem(ItemStack itemStack, double joules, boolean doCharge)
    {
        CompatibilityModule module = getHandler(itemStack, null);
        if(module != null)
        {
            return module.chargeItem(itemStack, joules, doCharge);
        }
        return 0;
    }

    /**
     * Discharge's an ItemStack's contained energy
     * @param itemStack - stack being charged
     * @param joules - energy to charge
     * @param doDischarge - true will remove the energy, false will test
     * @return energy actually taken
     */
    public static double drain(ItemStack itemStack, double joules, boolean doDischarge)
    {
        return dischargeItem(itemStack, joules, doDischarge);
    }

    /**
     * Discharge's an ItemStack's contained energy
     * @param itemStack - stack being charged
     * @param joules - energy to charge
     * @param doDischarge - true will remove the energy, false will test
     * @return energy actually taken
     */
    public static double dischargeItem(ItemStack itemStack, double joules, boolean doDischarge)
    {
        CompatibilityModule module = getHandler(itemStack, null);
        if(module != null)
        {
            return module.dischargeItem(itemStack, joules, doDischarge);
        }
        return 0;
    }


    public static ItemStack getItemWithCharge(ItemStack itemStack, double energy)
    {
        CompatibilityModule module = getHandler(itemStack, null);
        if(module != null)
        {
            return module.getItemWithCharge(itemStack, energy);
        }
        return itemStack;
    }

    public static double getEnergy(Object obj, ForgeDirection direction)
    {
        CompatibilityModule module = getHandler(obj, direction);
        if(module != null)
        {
            return module.getEnergy(obj, direction);
        }
        return 0;
    }

    public static double getMaxEnergy(Object handler, ForgeDirection direction)
    {
        CompatibilityModule module = getHandler(handler, direction);
        if(module != null)
        {
            return module.getMaxEnergy(handler, direction);
        }
        return 0;
    }

    public static double getEnergyItem(ItemStack is)
    {
        CompatibilityModule module = getHandler(is, null);
        if(module != null)
        {
            return module.getEnergyItem(is);
        }
        return 0;
    }

    public static double getMaxEnergyItem(ItemStack is){
        CompatibilityModule module = getHandler(is, null);
        if(module != null)
        {
            return module.getMaxEnergyItem(is);
        }
        return 0;
    }

    /**
     * Prefab for handling each mod's energy system in relation to UE's energy system
     */
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

		public abstract boolean doIsHandler(Object obj, ForgeDirection dir);

		public abstract boolean doIsEnergyContainer(Object obj);

		public abstract boolean canConnect(Object obj, ForgeDirection direction, Object source);

		public abstract ItemStack getItemWithCharge(ItemStack itemStack, double energy);

        public abstract double getEnergy(Object obj, ForgeDirection direction);

		public abstract double getMaxEnergy(Object handler, ForgeDirection direction);

		public abstract double getEnergyItem(ItemStack is);

		public abstract double getMaxEnergyItem(ItemStack is);
	}

}
