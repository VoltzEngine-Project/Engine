package calclavia.lib.utility;

import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Fluid interactions.
 * 
 * @author Calclavia
 * 
 */
public class FluidUtility
{
	/**
	 * Adds energy to the fluid and thereby, increasing its temperature.
	 * 
	 * @param fluidStack - The fluid stack we are changing.
	 * @param specificHeatCapacity - E.g: Water: 4200. Iron: 450.
	 * @param energy - Amount of energy to put into the fluid.
	 * @return
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
}
