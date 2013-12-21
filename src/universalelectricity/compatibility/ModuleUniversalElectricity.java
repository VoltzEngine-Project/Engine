package universalelectricity.compatibility;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.energy.IEnergyInterface;

/**
 * @author Calclavia
 * 
 */
public class ModuleUniversalElectricity extends CompatibilityModule
{
	@Override
	public long doReceiveEnergy(Object obj, ForgeDirection direction, long energy, boolean doReceive)
	{
		return ((IEnergyInterface) obj).onReceiveEnergy(direction, energy, doReceive);
	}

	@Override
	public boolean isHandler(Object obj)
	{
		return obj instanceof IEnergyInterface;
	}
}
