package universalelectricity.compatibility;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.Compatibility.CompatibilityType;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.vector.Vector3;

/** @author Calclavia */
public class ModuleIndustrialCraft extends CompatibilityModule
{
    @Override
    public long doReceiveEnergy(Object obj, ForgeDirection direction, long energy, boolean doReceive)
    {
        double rejected = ((IEnergySink) obj).injectEnergyUnits(direction, energy * CompatibilityType.INDUSTRIALCRAFT.ratio);
        return (long) (energy - (rejected * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio));
    }

    @Override
    public boolean doIsHandler(Object obj)
    {
        return obj instanceof IEnergySink;
    }

    @Override
    public boolean doCanConnect(Object obj, ForgeDirection direction)
    {
        if (obj instanceof TileEntity)
        {
            TileEntity tileEntity = (TileEntity) obj;
            Vector3 adjacentCoordinate = new Vector3(tileEntity).modifyPositionFromSide(direction);
            return ((IEnergySink) obj).acceptsEnergyFrom(adjacentCoordinate.getTileEntity(tileEntity.worldObj), direction);
        }

        return false;
    }

    @Override
    public long chargeItem(ItemStack itemStack, long joules, boolean docharge)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long dischargeItem(ItemStack itemStack, long joules, boolean doDischarge)
    {
        if (itemStack.getItem() instanceof IElectricItem)
        {
            IElectricItem item = (IElectricItem) itemStack.getItem();

            if (item.canProvideEnergy(itemStack))
            {
                return ElectricItem.manager.discharge(itemStack, (int) joules, 4, true, doDischarge);
            }
        }
        return 0;
    }

    @Override
    public boolean doIsEnergyItem(Item item)
    {
        return item instanceof IElectricItem;
    }
}
