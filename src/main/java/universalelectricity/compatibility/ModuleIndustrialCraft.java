package universalelectricity.compatibility;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.CompatibilityType;
import universalelectricity.api.vector.Vector3;

/** @author Calclavia */
public class ModuleIndustrialCraft extends CompatibilityModule
{
	@Override
	public long doReceiveEnergy(Object handler, ForgeDirection direction, long energy, boolean doReceive)
	{
		if (handler instanceof IEnergySink)
		{
			long request = (long) Math.min(((IEnergySink) handler).demandedEnergyUnits() * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio, energy);

			if (doReceive)
			{
				double rejected = ((IEnergySink) handler).injectEnergyUnits(direction, request * CompatibilityType.INDUSTRIALCRAFT.ratio);
				return (long) Math.max(energy - (rejected * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio), 0);
			}

			return request;
		}

		return 0;
	}

	@Override
	public long doExtractEnergy(Object handler, ForgeDirection direction, long energy, boolean doExtract)
	{
		if (handler instanceof IEnergySource)
		{
			long demand = (long) Math.min(((IEnergySource) handler).getOfferedEnergy() * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio, energy);

			if (doExtract)
			{
				((IEnergySource) handler).drawEnergy(demand * CompatibilityType.INDUSTRIALCRAFT.ratio);
			}

			return demand;
		}

		return 0;
	}

	@Override
	public boolean doIsHandler(Object obj)
	{
		return obj instanceof IEnergySink || obj instanceof IEnergySource || obj instanceof IElectricItem;
	}

	@Override
	public boolean doCanConnect(Object obj, ForgeDirection direction, Object source)
	{
		// TODO: Does not support multipart sources.
		if (obj instanceof TileEntity && source instanceof TileEntity)
		{
			TileEntity tileEntity = (TileEntity) obj;

			if (tileEntity instanceof IEnergySink)
			{
				return ((IEnergySink) tileEntity).acceptsEnergyFrom((TileEntity) source, direction);
			}
			else if (tileEntity instanceof IEnergySource)
			{
				return ((IEnergySource) tileEntity).emitsEnergyTo((TileEntity) source, direction);
			}
		}

		return false;
	}

	@Override
	public long doChargeItem(ItemStack itemStack, long joules, boolean doCharge)
	{
		if (itemStack.getItem() instanceof IElectricItem)
		{
			return (long) (ElectricItem.manager.charge(itemStack, (int) Math.ceil(joules * CompatibilityType.INDUSTRIALCRAFT.ratio), 4, true, !doCharge) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
		}
		return 0;
	}

	@Override
	public long doDischargeItem(ItemStack itemStack, long joules, boolean doDischarge)
	{
		if (itemStack.getItem() instanceof IElectricItem)
		{
			IElectricItem item = (IElectricItem) itemStack.getItem();

			if (item.canProvideEnergy(itemStack))
			{
				return (long) (ElectricItem.manager.discharge(itemStack, (int) Math.ceil(joules * CompatibilityType.INDUSTRIALCRAFT.ratio), 4, true, !doDischarge) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
			}
		}
		return 0;
	}

	@Override
	public ItemStack doGetItemWithCharge(ItemStack itemStack, long energy)
	{
		ItemStack is = itemStack.copy();

		ElectricItem.manager.discharge(is, Integer.MAX_VALUE, 1, true, false);
		ElectricItem.manager.charge(is, (int) (energy * CompatibilityType.INDUSTRIALCRAFT.ratio), 1, true, false);

		return is;
	}

	@Override
	public boolean doIsEnergyContainer(Object obj)
	{
		return obj instanceof IEnergyStorage;
	}

	@Override
	public long doGetEnergy(Object obj, ForgeDirection direction)
	{
		return (long) (((IEnergyStorage) obj).getStored() * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
	}

	@Override
	public long doGetMaxEnergy(Object obj, ForgeDirection direction)
	{
		return (long) (((IEnergyStorage) obj).getCapacity() * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
	}

	@Override
	public long doGetEnergyItem(ItemStack is)
	{
		return (long) (ElectricItem.manager.getCharge(is) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
	}

	@Override
	public long doGetMaxEnergyItem(ItemStack is)
	{
		return (long) (((IElectricItem) is.getItem()).getMaxCharge(is) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
	}
}
