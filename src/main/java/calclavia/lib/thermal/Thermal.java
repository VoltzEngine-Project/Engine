package calclavia.lib.thermal;

import java.util.HashMap;

import net.minecraftforge.fluids.FluidStack;
import universalelectricity.api.vector.Vector3;

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

		if (fluidStack.getFluid() instanceof FluidAdvanced)
		{
			((FluidAdvanced) fluidStack.getFluid()).setTemperature(fluidStack, fluidStack.getFluid().getTemperature(fluidStack) + changeInTemperature);
		}

		return changeInTemperature;
	}

	public void addEnergy()
	{

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
}
