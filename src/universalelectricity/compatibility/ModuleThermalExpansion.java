package universalelectricity.compatibility;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.Compatibility;
import universalelectricity.api.Compatibility.CompatibilityType;
import universalelectricity.api.CompatibilityModule;
import cofh.api.energy.IEnergyHandler;

/**
 * @author Calclavia
 * 
 */
public class ModuleThermalExpansion extends CompatibilityModule
{
	@Override
	public long doReceiveEnergy(Object obj, ForgeDirection direction, long energy, boolean doReceive)
	{
		return (long) (((IEnergyHandler) obj).receiveEnergy(direction, (int) (energy * CompatibilityType.THERMAL_EXPANSION.ratio), !doReceive) * CompatibilityType.THERMAL_EXPANSION.reciprocal_ratio);
	}

	@Override
	public boolean doIsHandler(Object obj)
	{
		return obj instanceof IEnergyHandler;
	}

	@Override
	public boolean doCanConnect(Object obj, ForgeDirection direction)
	{
		return ((IEnergyHandler) obj).canInterface(direction);
	}
}
