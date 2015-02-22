package com.builtbroken.mc.prefab.tile.module;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import com.builtbroken.mc.api.energy.IEnergyCapacitor;
import com.builtbroken.mc.api.tile.ITileModuleProvider;
import net.minecraftforge.common.util.ForgeDirection;

/** Module designed to take care of Thermal Expansion's RF energy
 * Created by robert on 2/22/2015.
 */
public class TileModuleRFSupport extends TileModule implements IEnergyConnection, IEnergyHandler
{
    protected int max_energy = 1000;
    protected int energy = 0;

    public TileModuleRFSupport(ITileModuleProvider parent)
    {
        super(parent);
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        //TODO setup to use compatibility handler
        if(allowConnection(from.getOpposite()))
        {
            int newEnergy = getEnergyStored(from) + maxReceive;
            if(newEnergy > getMaxEnergyStored(from))
            {
                int over = newEnergy - getMaxEnergyStored(from);
                if(!simulate)
                {
                    setEnergyStored(from, getMaxEnergyStored(from));
                }
                return maxReceive - over;
            }
            else
            {
                if(!simulate)
                {
                    setEnergyStored(from, newEnergy);
                }
                return maxReceive;
            }
        }
        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        //TODO setup to use compatibility handler
        if(allowConnection(from.getOpposite()))
        {
            if(maxExtract >= getEnergyStored(from))
            {
                if(!simulate)
                {
                    setEnergyStored(from, energy - getEnergyStored(from));
                }
                return getEnergyStored(from);
            }
            else
            {
                if(!simulate)
                {
                    setEnergyStored(from, energy - maxExtract);
                }
                return maxExtract;
            }
        }
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        if(getParent() instanceof IEnergyCapacitor)
        {
            return ((IEnergyCapacitor) getParent()).getEnergyForSide(from);
        }
        return energy;
    }

    public void setEnergyStored(ForgeDirection from, int energy)
    {
        if(getParent() instanceof IEnergyCapacitor)
        {
            ((IEnergyCapacitor) getParent()).setEnergyForSide(from, energy);
        }
        else
        {
            this.energy = energy;
        }
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        if(getParent() instanceof IEnergyCapacitor)
        {
            return ((IEnergyCapacitor) getParent()).getMaxEnergyForSide(from);
        }
        return max_energy;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return allowConnection(from.getOpposite());
    }
}
