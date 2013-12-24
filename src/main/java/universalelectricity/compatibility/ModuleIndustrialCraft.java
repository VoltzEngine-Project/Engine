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
	public boolean doCanConnect(Object obj, ForgeDirection direction)
	{
		if (obj instanceof TileEntity)
		{
			TileEntity tileEntity = (TileEntity) obj;
			Vector3 adjacentCoordinate = new Vector3(tileEntity).modifyPositionFromSide(direction.getOpposite());

			if (tileEntity instanceof IEnergySink)
			{
				return ((IEnergySink) tileEntity).acceptsEnergyFrom(adjacentCoordinate.getTileEntity(tileEntity.worldObj), direction);
			}
			else if (tileEntity instanceof IEnergySource)
			{
				return ((IEnergySource) tileEntity).emitsEnergyTo(adjacentCoordinate.getTileEntity(tileEntity.worldObj), direction);
			}
		}

		return false;
	}

	@Override
	public long doChargeItem(ItemStack itemStack, long joules, boolean docharge)
	{
		if (itemStack.getItem() instanceof IElectricItem)
		{
			return (long) (ElectricItem.manager.charge(itemStack, (int) (joules * CompatibilityType.INDUSTRIALCRAFT.ratio), 4, true, false) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
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
				return (long) (ElectricItem.manager.discharge(itemStack, (int) (joules * CompatibilityType.INDUSTRIALCRAFT.ratio), 4, true, doDischarge) * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
			}
		}
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
		return obj instanceof IEnergyStorage;
	}

	@Override
	public long doGetEnergy(Object obj, ForgeDirection direction)
	{
		return (long) (((IEnergyStorage) obj).getStored() * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);
	}
}
