package com.builtbroken.mc.lib.mod.compat.rf;

import cofh.api.energy.*;
import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.energy.EnergyHandler;
import com.builtbroken.mc.lib.helper.ReflectionUtility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Compatibility with RF energy system
 * Created by Robert on 8/10/2015.
 */
public class RFEnergyHandler extends EnergyHandler
{
    public static final RFEnergyHandler INSTANCE = new RFEnergyHandler(2); //TODO make ratio configurable
    /** Overrides handling for thermal expansion machines directly */
    public static RFEnergyHandler thermalExpansionHandler;

    /** Ratio of converting RF to UE power */
    public static double TO_RF_FROM_UE;
    /** Ratio of converting UE to RF power */
    public static double TO_UE_FROM_RF;

    /** Map of energy fields for bypassing API calls to solve extraction limits */
    private Map<Class, Field> classToEnergyFieldMap = new HashMap();

    //TODO ensure that ratio does come with a loss in energy due to rounding errors
    protected RFEnergyHandler(double ratio)
    {
        super("rf", "flux", "rf", ratio);
        TO_RF_FROM_UE = toForgienEnergy;
        TO_UE_FROM_RF = toUEEnergy;
        //TODO load handler even if TE or it's core is not loaded
        //Instead check that the 4 API files used exist then load so we support any mod that uses RF
    }

    @Override
    public double receiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive)
    {
        if (!(handler instanceof IEnergyConnection) || ((IEnergyConnection) handler).canConnectEnergy(direction))
        {
            if (handler instanceof IEnergyReceiver)
            {
                //TODO check for rounding errors
                return ((IEnergyReceiver) handler).receiveEnergy(direction, (int) (energy * toForgienEnergy), !doReceive) * toUEEnergy;
            }
        }
        return 0;
    }

    @Override
    public double extractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract)
    {
        if (!(handler instanceof IEnergyConnection) || ((IEnergyConnection) handler).canConnectEnergy(direction))
        {
            if (handler instanceof IEnergyProvider)
            {
                return ((IEnergyProvider) handler).extractEnergy(direction, (int) (energy * toForgienEnergy), !doExtract) * toUEEnergy;
            }
        }
        return 0;
    }

    @Override
    public double clearEnergy(Object handler, boolean doAction)
    {
        //Override for thermal expansion support, improves performance
        if(thermalExpansionHandler != null && thermalExpansionHandler.handle(handler))
        {
            return thermalExpansionHandler.clearEnergy(handler, doAction);
        }

        //Reflection code to bypass Thermal Expansions energy output limits of 32K on a 80M RF tile which would take 2500 loop calls
        final Class handlerClass = handler.getClass();
        if (classToEnergyFieldMap.containsKey(handlerClass) && classToEnergyFieldMap.get(handlerClass) != null)
        {
            long start = System.nanoTime();
            try
            {
                Field field = classToEnergyFieldMap.get(handlerClass);
                IEnergyStorage storage = (IEnergyStorage) field.get(handler);
                if (storage instanceof EnergyStorage)
                {
                    int energy = storage.getEnergyStored();
                    if (doAction)
                    {
                        ((EnergyStorage) storage).setEnergyStored(0);
                    }
                    return energy;
                }
                //TODO Has the same issue as calling it normally meh, we need another work around
                return storage.extractEnergy(Integer.MAX_VALUE, !doAction) * toUEEnergy;
            }
            catch (Exception e)
            {
                Engine.logger().error("Failed to directly set energy value for " + handler, e);
                classToEnergyFieldMap.put(handlerClass, null); //Remove to prevent increasing errors
                //TODO check error type to ignore non-reflection based errors that could be NPE, divide by zero, or other normal errors
            }
            Engine.logger().info("Took " + StringHelpers.formatTimeDifference(start, System.nanoTime()) + " to process reflection set for " + handler);
        }
        else if (handler instanceof IEnergyProvider)
        {
            int drained = 0;
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                int energyStored = ((IEnergyProvider) handler).getEnergyStored(direction);
                int drain = ((IEnergyProvider) handler).extractEnergy(direction, Integer.MAX_VALUE, true);

                //If simulating do not run loop code
                if (!doAction)
                {
                    drained += energyStored;
                    continue;
                }

                //Thermal expansion has limits on its max drain per call, to get around this we just keep calling it...
                //      Yes this is stupid but the API has no away around it
                if (drain < energyStored)
                {
                    if (!classToEnergyFieldMap.containsKey(handlerClass))
                    {
                        long start = System.nanoTime();
                        try
                        {
                            //TODO make helper for finding fields of type
                            List<Field> fields = ReflectionUtility.getAllFields(handlerClass);
                            for (Field field : fields)
                            {
                                if (IEnergyStorage.class.isAssignableFrom(field.getType()))
                                {
                                    field.setAccessible(true);
                                    classToEnergyFieldMap.put(handlerClass, field);
                                    Engine.logger().info("Took " + StringHelpers.formatTimeDifference(start, System.nanoTime()) + " to find energy field class for " + handlerClass);
                                    return clearEnergy(handler, doAction);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            Engine.logger().error("Unexpected error while finding energy storage field for " + handler, e);
                            //TODO check error type to ignore non-reflection based errors that could be NPE, divide by zero, or other normal errors
                        }
                        Engine.logger().info("Took " + StringHelpers.formatTimeDifference(start, System.nanoTime()) + " to fail find energy field class for " + handlerClass);
                        classToEnergyFieldMap.put(handlerClass, null); //If error or failed to find add it to the ignore list with a null value
                    }
                    //TODO report tiles to database that can't be quickly set so mod support can be added
                    drain = 0;
                    //Ensure method call is no longer than 1/10 second
                    long startTime = System.currentTimeMillis();
                    while (energyStored > 0 && System.currentTimeMillis() - startTime > 100)
                    {
                        int d = ((IEnergyProvider) handler).extractEnergy(direction, Integer.MAX_VALUE, false);
                        energyStored -= d;
                        drain += d;
                    }
                    drained += drain;
                }
                //In the rare case of no limits just call normal drain code
                else
                {
                    drained += ((IEnergyProvider) handler).extractEnergy(direction, Integer.MAX_VALUE, false);
                }
            }
            return drained * toUEEnergy;
        }
        return 0;
    }

    @Override
    public boolean doIsHandler(Object obj, ForgeDirection dir)
    {
        return obj instanceof IEnergyHandler || obj instanceof IEnergyProvider || obj instanceof IEnergyReceiver;
    }

    @Override
    public boolean doIsHandler(Object obj)
    {
        return obj instanceof IEnergyHandler || obj instanceof IEnergyProvider || obj instanceof IEnergyReceiver;
    }

    @Override
    public boolean doIsEnergyContainer(Object obj)
    {
        return obj instanceof IEnergyHandler || obj instanceof IEnergyProvider || obj instanceof IEnergyReceiver;
    }

    @Override
    public boolean canConnect(Object obj, ForgeDirection direction, Object source)
    {
        return obj instanceof IEnergyConnection ? ((IEnergyConnection) obj).canConnectEnergy(direction) : doIsEnergyContainer(obj);
    }

    @Override
    public double getEnergy(Object obj, ForgeDirection direction)
    {
        //Override for thermal expansion support, improves performance
        if(thermalExpansionHandler != null && thermalExpansionHandler.handle(obj))
        {
            return thermalExpansionHandler.getEnergy(obj, direction);
        }
        return obj instanceof IEnergyReceiver ? ((IEnergyReceiver) obj).getEnergyStored(direction) * toUEEnergy : obj instanceof IEnergyProvider ? ((IEnergyProvider) obj).getEnergyStored(direction) * toUEEnergy : 0;
    }

    @Override
    public double getMaxEnergy(Object obj, ForgeDirection direction)
    {
        //Override for thermal expansion support, improves performance
        if(thermalExpansionHandler != null && thermalExpansionHandler.handle(obj))
        {
            return thermalExpansionHandler.getMaxEnergy(obj, direction);
        }
        return obj instanceof IEnergyReceiver ? ((IEnergyReceiver) obj).getMaxEnergyStored(direction) * toUEEnergy : obj instanceof IEnergyProvider ? ((IEnergyProvider) obj).getMaxEnergyStored(direction) * toUEEnergy : 0;
    }

    @Override
    public double chargeItem(ItemStack is, double joules, boolean docharge)
    {
        if (is != null && is.getItem() instanceof IEnergyContainerItem)
        {
            return ((IEnergyContainerItem) is.getItem()).receiveEnergy(is, (int) (joules * toForgienEnergy), !docharge) * toUEEnergy;
        }
        return 0;
    }

    @Override
    public double dischargeItem(ItemStack is, double joules, boolean doDischarge)
    {
        if (is != null && is.getItem() instanceof IEnergyContainerItem)
        {
            return ((IEnergyContainerItem) is.getItem()).extractEnergy(is, (int) (joules * toForgienEnergy), !doDischarge) * toUEEnergy;
        }
        return 0;
    }

    @Override
    public ItemStack getItemWithCharge(ItemStack itemStack, double energy)
    {
        chargeItem(itemStack, (energy * toForgienEnergy), true);
        return itemStack;
    }

    @Override
    public double getEnergyItem(ItemStack is)
    {
        return is != null && is.getItem() instanceof IEnergyContainerItem ? ((IEnergyContainerItem) is.getItem()).getEnergyStored(is) * toUEEnergy : 0;
    }

    @Override
    public double getMaxEnergyItem(ItemStack is)
    {
        return is != null && is.getItem() instanceof IEnergyContainerItem ? ((IEnergyContainerItem) is.getItem()).getMaxEnergyStored(is) * toUEEnergy : 0;
    }

    protected boolean handle(Object handler)
    {
        //Overridden in sub versions of the object
        return false;
    }
}
