package resonant.lib.prefab.tile;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.energy.IEnergyContainer;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.vector.Vector3;

@UniversalClass
public class TileElectrical extends TileIO implements IEnergyInterface, IEnergyContainer
{
    protected EnergyStorageHandler energy;

    public TileElectrical()
    {
        super(null);
    }

    public TileElectrical(Material material)
    {
        super(material);
    }

    /** Recharges electric item. */
    public void recharge(ItemStack itemStack)
    {
        if (this.getEnergyHandler() != null)
        {
            this.getEnergyHandler().extractEnergy(CompatibilityModule.chargeItem(itemStack, this.getEnergyHandler().getEnergy(), true), true);
        }
    }

    /** Discharges electric item. */
    public void discharge(ItemStack itemStack)
    {
        if (this.getEnergyHandler() != null)
        {
            this.getEnergyHandler().receiveEnergy(CompatibilityModule.dischargeItem(itemStack, this.getEnergyHandler().getEmptySpace(), true), true);
        }
    }

    @Override
    public boolean canConnect(ForgeDirection direction, Object obj)
    {
        if (CompatibilityModule.isHandler(obj))
        {
            if (direction == null || direction.equals(ForgeDirection.UNKNOWN))
            {
                return false;
            }

            return this.getInputDirections().contains(direction) || this.getOutputDirections().contains(direction);
        }

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (this.getEnergyHandler() != null)
        {
            this.getEnergyHandler().readFromNBT(nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (this.getEnergyHandler() != null)
        {
            this.getEnergyHandler().writeToNBT(nbt);
        }
    }

    @Override
    public long getEnergy(ForgeDirection from)
    {
        if (this.getEnergyHandler() != null)
        {
            return this.getEnergyHandler().getEnergy();
        }
        else
        {
            return -1;
        }
    }

    @Override
    public long getEnergyCapacity(ForgeDirection from)
    {
        if (this.getEnergyHandler() != null)
        {
            return this.getEnergyHandler().getEnergyCapacity();
        }
        else
        {
            return -1;
        }
    }

    @Override
    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        if (this.getEnergyHandler() != null && (from == ForgeDirection.UNKNOWN || this.getInputDirections().contains(from)))
        {
            return this.getEnergyHandler().receiveEnergy(receive, doReceive);
        }

        return 0;
    }

    @Override
    public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract)
    {
        if (this.getEnergyHandler() != null && (from == ForgeDirection.UNKNOWN || this.getOutputDirections().contains(from)))
        {
            return this.getEnergyHandler().extractEnergy(extract, doExtract);
        }

        return 0;
    }

    @Override
    public void setEnergy(ForgeDirection from, long energy)
    {
        if (this.getEnergyHandler() != null)
            this.getEnergyHandler().setEnergy(energy);
    }

    protected long produce(long outputEnergy)
    {
        long usedEnergy = 0;

        for (ForgeDirection direction : this.getOutputDirections())
        {
            if (outputEnergy > 0)
            {
                TileEntity tileEntity = new Vector3(this).translate(direction).getTileEntity(this.worldObj);

                if (tileEntity != null)
                {
                    usedEnergy += CompatibilityModule.receiveEnergy(tileEntity, direction.getOpposite(), outputEnergy, true);
                }
            }
        }

        return usedEnergy;
    }

    protected long produce()
    {
        long totalUsed = 0;

        for (ForgeDirection direction : this.getOutputDirections())
        {
            if (this.getEnergyHandler().getEnergy() > 0)
            {
                TileEntity tileEntity = new Vector3(this).translate(direction).getTileEntity(this.worldObj);

                if (tileEntity != null)
                {
                    long used = CompatibilityModule.receiveEnergy(tileEntity, direction.getOpposite(), getEnergyHandler().extractEnergy(getEnergyHandler().getEnergy(), false), true);
                    totalUsed += this.getEnergyHandler().extractEnergy(used, true);
                }
            }
        }

        return totalUsed;
    }

    public EnergyStorageHandler getEnergyHandler()
    {
        return energy;
    }

    public void setEnergyHandler(EnergyStorageHandler energy)
    {
        this.energy = energy;
    }
}