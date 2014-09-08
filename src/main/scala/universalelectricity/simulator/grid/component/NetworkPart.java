package universalelectricity.simulator.grid.component;

import universalelectricity.simulator.grid.LinkedGrid;

/**
 * @author Dark
 */
public class NetworkPart implements IComponent
{
	LinkedGrid sim;

	public NetworkPart(LinkedGrid sim)
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
