package resonant.api.electric;

import net.minecraftforge.common.util.ForgeDirection;

/** Enhanced version of the IEnergyNode to allow for voltage */
public interface IElectricNode extends IEnergyNode
{
    /** Voltage level of the node */
	public double getVoltage(ForgeDirection direction);
}
