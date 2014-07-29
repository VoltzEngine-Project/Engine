package universalelectricity.api.core.grid.electric;

import universalelectricity.api.core.grid.INode;

/** Applied to any object that acts as an electrical node in a network */
public interface IElectricNode extends IEnergyNode
{
	public double getVoltage();
}
