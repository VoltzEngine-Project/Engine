package universalelectricity.api.core.grid.electric;

import universalelectricity.api.core.grid.INode;

/** Applied to all object that can act as an energy node in a network */
public interface IEnergyNode extends INode
{
	/** Gets the nodes energy storage object, used to get energy, limit input, limit output, and get capacity */
	public EnergyStorage getEnergyStorage();
}
