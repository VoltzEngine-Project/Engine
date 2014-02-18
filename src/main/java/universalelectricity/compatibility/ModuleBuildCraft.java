package universalelectricity.compatibility;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.CompatibilityType;
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
	public long doReceiveEnergy(Object handler, ForgeDirection direction, long energy, boolean doReceive)
	{
		IPowerReceptor receptor = ((IPowerReceptor) handler);
		PowerReceiver receiver = receptor.getPowerReceiver(direction);

		if (receiver != null)
		{
			if (doReceive)
			{
				return (long) (receiver.receiveEnergy(Type.PIPE, (float) (energy * CompatibilityType.BUILDCRAFT.ratio), direction) * CompatibilityType.BUILDCRAFT.reciprocal_ratio);
			}

			return (long) (receiver.powerRequest() * CompatibilityType.BUILDCRAFT.reciprocal_ratio);
		}

		return 0;
	}

	@Override
	public long doExtractEnergy(Object handler, ForgeDirection direction, long energy, boolean doExtract)
	{
		return 0;
	}

	@Override
	public boolean doIsHandler(Object obj)
	{
		return obj instanceof IPowerReceptor;
	}

	@Override
	public boolean doCanConnect(Object obj, ForgeDirection direction, Object source)
	{
		return ((IPowerReceptor) obj).getPowerReceiver(direction) != null;
	}

	@Override
	public long doChargeItem(ItemStack itemStack, long joules, boolean docharge)
	{
		return 0;
	}

	@Override
	public long doDischargeItem(ItemStack itemStack, long joules, boolean doDischarge)
	{
		return 0;
	}

	@Override
	public ItemStack doGetItemWithCharge(ItemStack itemStack, long energy)
	{
		return null;
	}

	@Override
	public boolean doIsEnergyContainer(Object obj)
	{
		return obj instanceof IPowerReceptor;
	}

	@Override
	public long doGetEnergy(Object obj, ForgeDirection direction)
	{
		if (obj instanceof IPowerReceptor)
		{
			if (((IPowerReceptor) obj).getPowerReceiver(direction) != null)
			{
				return (long) (((IPowerReceptor) obj).getPowerReceiver(direction).getEnergyStored() * CompatibilityType.BUILDCRAFT.reciprocal_ratio);
			}
		}

		return 0;
	}

	@Override
	public long doGetMaxEnergy(Object obj, ForgeDirection direction)
	{
		if (obj instanceof IPowerReceptor)
		{
			if (((IPowerReceptor) obj).getPowerReceiver(direction) != null)
			{
				return (long) (((IPowerReceptor) obj).getPowerReceiver(direction).getMaxEnergyStored() * CompatibilityType.BUILDCRAFT.reciprocal_ratio);
			}
		}

		return 0;
	}

	@Override
	public long doGetEnergyItem(ItemStack is)
	{
		return 0;
	}

	@Override
	public long doGetMaxEnergyItem(ItemStack is)
	{
		return 0;
	}
}
