package calclavia.lib.thermal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.Event.HasResult;
import net.minecraftforge.fluids.FluidStack;
import universalelectricity.api.vector.Vector3;

import com.builtbroken.common.science.ChemElement;

/**
 * A thermal block manager
 * 
 * @author Calclavia
 * 
 */
public class Thermal
{
	public static final Thermal INSTNACE = new Thermal();
	public static final int ROOM_TEMPERATURE = 295;

	/**
	 * A map of the temperature of the blocks
	 */
	public final HashMap<Vector3, Integer> thermalMap = new HashMap<Vector3, Integer>();

	public void update()
	{
		/**
		 * Reach thermal equilibrium
		 */
		Iterator<Entry<Vector3, Integer>> it = thermalMap.entrySet().iterator();

		while (it.hasNext())
		{
			Entry<Vector3, Integer> entry = it.next();

			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			{
				Vector3 checkPos = entry.getKey().clone().translate(dir);
				int neighbourTemp = getTemperature(checkPos);
				entry.setValue((entry.getValue() + neighbourTemp) / 2);
			}
		}
	}

	/**
	 * Adds energy to the fluid and thereby, increasing its temperature.
	 * 
	 * @param fluidStack - The fluid stack we are changing.
	 * @param specificHeatCapacity - E.g: Water: 4200. Iron: 450.
	 * @param energy - Amount of energy to put into the fluid.
	 * @return Change in temperature in Kelvin
	 */
	public int addEnergyToFluid(FluidStack fluidStack, int specificHeatCapacity, long energy)
	{
		// Mass (KG) = Volume (Cubic Meters) * Densitry (kg/m-cubed)
		int mass = (fluidStack.amount / 1000) * fluidStack.getFluid().getDensity(fluidStack);

		// c = Q/(mT); Therefore: Temperature (in Kelvin) = Q/mc
		int changeInTemperature = (int) (energy / (mass * specificHeatCapacity));

		if (fluidStack.getFluid() instanceof FluidThermal)
		{
			((FluidThermal) fluidStack.getFluid()).setTemperature(fluidStack, fluidStack.getFluid().getTemperature(fluidStack) + changeInTemperature);
		}

		return changeInTemperature;
	}

	/**
	 * Adds energy to a block in the form of heat.
	 */
	public void addEnergy(Vector3 position, ChemElement element, long energy)
	{
		// Mass (KG) = Volume (Cubic Meters) * Densitry (kg/m-cubed)
		int mass = (int) (1 * element.density);

		// c = Q/(mT); Therefore: Temperature (in Kelvin) = Q/mc
		int changeInTemperature = (int) (energy / (mass * element.heatData.specificHeat));

		setTemperature(position, getTemperature(position) + changeInTemperature);
	}

	public void setTemperature(Vector3 position, int temperature)
	{
		thermalMap.put(position, temperature);
	}

	public int getTemperature(Vector3 position)
	{
		if (thermalMap.containsKey(position))
		{
			return thermalMap.get(position);
		}

		return ROOM_TEMPERATURE;
	}

	@HasResult
	public static class EventVaporize extends Event
	{
		public final Vector3 position;

		public EventVaporize(Vector3 position)
		{
			this.position = position;
		}
	}
}
