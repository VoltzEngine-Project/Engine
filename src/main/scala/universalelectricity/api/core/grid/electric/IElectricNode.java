package universalelectricity.api.core.grid.electric;

import universalelectricity.api.core.grid.INode;

/** Enhanced version of the IEnergyNode to allow for voltage */
public interface IElectricNode extends IEnergyNode
{
    /** Voltage level of the node */
	public double getVoltage();
}
