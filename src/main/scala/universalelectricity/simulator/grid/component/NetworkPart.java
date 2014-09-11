package universalelectricity.simulator.grid.component;

import universalelectricity.simulator.grid.SimulatedGrid;

/**
 * @author Dark
 */
public class NetworkPart implements IComponent
{
	SimulatedGrid sim;

	public NetworkPart(SimulatedGrid sim)
	{
		this.sim = sim;
	}

	@Override
	public boolean hasInputDevices()
	{
		return false;
	}

	@Override
	public boolean hasOutputDevices()
	{
		return false;
	}
}
