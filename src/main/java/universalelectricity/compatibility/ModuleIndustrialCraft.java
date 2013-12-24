package universalelectricity.compatibility;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
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
	public long doReceiveEnergy(Object obj, ForgeDirection direction, long energy, boolean doReceive)
	{
		if (obj instanceof IEnergySink)
		{
			double rejected = ((IEnergySink) obj).injectEnergyUnits(direction, energy * CompatibilityType.INDUSTRIALCRAFT.ratio);
			return (long) (energy - (rejected * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio));
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
			Vector3 adjacentCoordinate = new Vector3(tileEntity).modifyPositionFromSide(direction);

			if (tileEntity instanceof IEnergySink)
			{
				((IEnergySink) tileEntity).acceptsEnergyFrom(adjacentCoordinate.getTileEntity(tileEntity.worldObj), direction);
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
}
