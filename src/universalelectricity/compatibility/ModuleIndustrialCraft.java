package universalelectricity.compatibility;

import ic2.api.energy.tile.IEnergySink;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.Compatibility.CompatibilityType;
import universalelectricity.api.CompatibilityModule;

/**
 * @author Calclavia
 * 
 */
public class ModuleIndustrialCraft extends CompatibilityModule
{
	@Override
	public long doReceiveEnergy(Object obj, ForgeDirection direction, long energy, boolean doReceive)
	{
		double rejected = ((IEnergySink) obj).injectEnergyUnits(direction, energy * CompatibilityType.INDUSTRIALCRAFT.ratio);
		return (long) (energy - (rejected * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio));
	}

	@Override
	public boolean isHandler(Object obj)
	{
		return obj instanceof IEnergySink;
	}
}
