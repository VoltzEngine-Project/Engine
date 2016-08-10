package com.builtbroken.mc.lib.mod.compat.te;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyStorage;
import cofh.thermalexpansion.block.TilePowered;
import com.builtbroken.mc.lib.mod.compat.rf.RFEnergyHandler;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/10/2016.
 */
public class TEEnergyHandler extends RFEnergyHandler
{
    public TEEnergyHandler()
    {
        super(RFEnergyHandler.INSTANCE.toUEEnergy);
    }

    @Override
    public double clearEnergy(Object handler, boolean doAction)
    {
        if (handler instanceof TilePowered)
        {
            IEnergyStorage storage = ((TilePowered) handler).getEnergyStorage();
            if (doAction)
            {
                int energyStored = storage.getEnergyStored();
                if (storage instanceof EnergyStorage)
                {
                    ((EnergyStorage) storage).setEnergyStored(0);
                }
                else
                {
                    storage.extractEnergy(Integer.MAX_VALUE, !doAction);
                }
                return energyStored;
            }
            return storage.getEnergyStored();
        }
        return super.clearEnergy(handler, doAction);
    }

    @Override
    public double getEnergy(Object obj, ForgeDirection direction)
    {
        return obj instanceof TilePowered ? ((TilePowered) obj).getEnergyStorage() != null ? ((TilePowered) obj).getEnergyStorage().getEnergyStored() : 0 : super.getEnergy(obj, direction);
    }

    @Override
    public double getMaxEnergy(Object obj, ForgeDirection direction)
    {
        //Override for thermal expansion support, improves performance
        return obj instanceof TilePowered ? ((TilePowered) obj).getEnergyStorage() != null ? ((TilePowered) obj).getEnergyStorage().getMaxEnergyStored() : 0 : super.getMaxEnergy(obj, direction);
    }

    @Override
    protected boolean handle(Object handler)
    {
        return handler instanceof TilePowered;
    }
}
