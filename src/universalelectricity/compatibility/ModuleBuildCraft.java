package universalelectricity.compatibility;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.Compatibility.CompatibilityType;
import universalelectricity.api.CompatibilityModule;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

/**
 * @author Calclavia
 * 
 */
public class ModuleBuildCraft extends CompatibilityModule
{
	@Override
	public long doReceiveEnergy(Object obj, ForgeDirection direction, long energy, boolean doReceive)
	{
		IPowerReceptor receptor = ((IPowerReceptor) obj);
		PowerReceiver receiver = receptor.getPowerReceiver(direction);

		if (receiver != null)
		{
			return (long) (receiver.receiveEnergy(Type.PIPE, energy * CompatibilityType.BUILDCRAFT.ratio, direction) * CompatibilityType.BUILDCRAFT.reciprocal_ratio);
		}

		return 0;
	}

	@Override
	public boolean isHandler(Object obj)
	{
		return obj instanceof IPowerReceptor;
	}
}
