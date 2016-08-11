package com.builtbroken.mc.lib.mod.compat.ic;

import com.builtbroken.mc.lib.energy.EnergyHandler;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/11/2016.
 */
public class ICHandler extends EnergyHandler
{
    /** Ratio of converting EU to UE power */
    public static double TO_UE;
    /** Ratio of converting UE to EU power */
    public static double FROM_UE;

    public ICHandler()
    {
        super("IndustrialCraft", "Energy Unit", "eu", 10);
        TO_UE = toForgienEnergy;
        FROM_UE = toUEEnergy;
    }

    @Override
    public double receiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive)
    {
        if (handler instanceof IEnergyStorage)
        {
            double provided = energy * toForgienEnergy;
            int energyStored = ((IEnergyStorage) handler).getStored();
            int space = ((IEnergyStorage) handler).getCapacity() - energyStored;
            if (!doReceive)
            {
                return Math.min(provided, space) * toUEEnergy;
            }
            if (space > provided)
            {
                int newStored = ((IEnergyStorage) handler).addEnergy((int) provided);
                return (newStored - energyStored) * toUEEnergy;
            }
            else
            {
                int newStored = ((IEnergyStorage) handler).addEnergy(space);
                return (newStored - energyStored) * toUEEnergy;
            }
        }
        else if (handler instanceof IEnergySink && ((IEnergySink) handler).acceptsEnergyFrom(null, direction))
        {
            if (!doReceive)
            {
                return ((IEnergySink) handler).getDemandedEnergy() * toUEEnergy;
            }
            return ((IEnergySink) handler).injectEnergy(direction, energy * toForgienEnergy, getVoltageForTier(((IEnergySink) handler).getSinkTier())) * toUEEnergy;
        }
        return 0;
    }

    @Override
    public double extractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract)
    {
        if (handler instanceof IEnergyStorage)
        {

        }
        else if (handler instanceof IEnergySource && ((IEnergySource) handler).emitsEnergyTo(null, direction))
        {
            double request = energy * toForgienEnergy;
            double e = Math.min(request, ((IEnergySource) handler).getOfferedEnergy());
            ((IEnergySource) handler).drawEnergy(e);
            return e * toUEEnergy;
        }
        return 0;
    }

    @Override
    public double chargeItem(ItemStack itemStack, double joules, boolean docharge)
    {
        return 0;
    }

    @Override
    public double dischargeItem(ItemStack itemStack, double joules, boolean doDischarge)
    {
        return 0;
    }

    @Override
    public boolean doIsHandler(Object obj, ForgeDirection dir)
    {
        return obj instanceof IEnergySink || obj instanceof IEnergyEmitter;
    }

    @Override
    public boolean doIsHandler(Object obj)
    {
        return obj instanceof IEnergySink || obj instanceof IEnergyEmitter;
    }

    @Override
    public boolean doIsEnergyContainer(Object obj)
    {
        return obj instanceof IEnergyStorage;
    }

    @Override
    public boolean canConnect(Object obj, ForgeDirection direction, Object source)
    {
        return obj instanceof IEnergyAcceptor;
    }

    @Override
    public ItemStack getItemWithCharge(ItemStack itemStack, double energy)
    {
        return null;
    }

    @Override
    public double getEnergy(Object obj, ForgeDirection direction)
    {
        if (obj instanceof IEnergyStorage)
        {
            return ((IEnergyStorage) obj).getStored() * toUEEnergy;
        }
        else if (obj instanceof IEnergySource)
        {
            return ((IEnergySource) obj).getOfferedEnergy() * toUEEnergy;
        }
        return obj instanceof IEnergySink ? ((IEnergySink) obj).getDemandedEnergy() * toUEEnergy : 0;
    }

    @Override
    public double getMaxEnergy(Object obj, ForgeDirection direction)
    {
        if (obj instanceof IEnergyStorage)
        {
            return ((IEnergyStorage) obj).getCapacity() * toUEEnergy;
        }
        //TODO find solution
        else if (obj instanceof IEnergySource)
        {
            return ((IEnergySource) obj).getOfferedEnergy() * toUEEnergy;
        }
        //TODO find solution
        return obj instanceof IEnergySink ? ((IEnergySink) obj).getDemandedEnergy() * toUEEnergy : 0;
    }

    @Override
    public double getEnergyItem(ItemStack is)
    {
        return 0;
    }

    @Override
    public double getMaxEnergyItem(ItemStack is)
    {
        return 0;
    }

    @Override
    public double clearEnergy(Object handler, boolean doAction)
    {
        if (!doAction)
        {
            return getEnergy(handler, null);
        }
        int e = 0;
        if (handler instanceof IEnergyStorage)
        {
            e = ((IEnergyStorage) handler).getStored();
            ((IEnergyStorage) handler).setStored(0);
        }
        else
        {
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            {
                e += extractEnergy(handler, dir, Integer.MAX_VALUE, doAction);
            }
        }
        return e * toUEEnergy;
    }

    private int getVoltageForTier(int tier)
    {
        switch (tier)
        {
            //http://wiki.industrial-craft.net/index.php?title=HV_Transformer
            //1 = LV, 2 = MV, 3 = HV, 4 = EV etc.
            case 1:
                return 32;
            case 2:
                return 128;
            case 3:
                return 512;
            case 4:
                return 2048;
            default:
                return 8;
        }
    }
}
