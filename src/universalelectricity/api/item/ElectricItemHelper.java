package universalelectricity.api.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.energy.IEnergyInterface;

/** Some helper functions for electric items.
 * 
 * @author Calclavia */
public class ElectricItemHelper
{

    /** Recharges an electric item from a machine
     * 
     * @param machine - machine that will give the item power
     * @param chargingSide - side to be used to call the getEnergy methods from
     * @param joules - The joules being provided to the electric item */
    public static void chargeItemFromMachine(IEnergyInterface machine, ForgeDirection chargingSide, ItemStack itemStack)
    {
        if (itemStack != null && machine != null)
        {
            if (itemStack.getItem() instanceof IElectricalItem)
            {
                long room = ((IElectricalItem) itemStack.getItem()).getElectricityCapacity(itemStack) - ((IElectricalItem) itemStack.getItem()).getElectricityStored(itemStack);
                long joules = machine.onExtractEnergy(chargingSide, room, false);

                machine.onExtractEnergy(chargingSide, chargeItem(itemStack, joules), true);
            }
        }
    }

    /** Decharges an electric item into a machine
     * 
     * @param machine - machine that gets the power
     * @param chargingSide - side to be used to call the getEnergy methods from
     * @param joules - The joules being withdrawn from the electric item */
    public static void dischargeItemToMachine(IEnergyInterface machine, ForgeDirection chargingSide, ItemStack itemStack)
    {
        if (itemStack != null && machine != null)
        {
            if (itemStack.getItem() instanceof IElectricalItem)
            {
                long joules = ((IElectricalItem) itemStack.getItem()).getElectricityStored(itemStack);
                dischargeItem(itemStack, machine.onReceiveEnergy(chargingSide, dischargeItem(itemStack, machine.onReceiveEnergy(chargingSide, joules, false)), true), true);
            }
        }
    }

    /** Recharges an electric item.
     * 
     * @param joules - The joules being provided to the electric item
     * @return The total amount of joules provided by the provider. */
    public static long chargeItem(ItemStack itemStack, long joules, boolean doCharge)
    {
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof IElectricalItem)
            {
                return ((IElectricalItem) itemStack.getItem()).recharge(itemStack, joules, doCharge);
            }
        }

        return 0;
    }

    /** Decharges an electric item.
     * 
     * @param joules - The joules being withdrawn from the electric item
     * @return The total amount of joules the provider received. */
    public static long dischargeItem(ItemStack itemStack, long joules, boolean doDischarge)
    {
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof IElectricalItem)
            {
                return ((IElectricalItem) itemStack.getItem()).discharge(itemStack, Math.min(((IElectricalItem) itemStack.getItem()).getElectricityCapacity(itemStack), joules), doDischarge);
            }
        }

        return 0;
    }

    /** Recharges an electric item.
     * 
     * @param joules - The joules being provided to the electric item
     * @return The total amount of joules provided by the provider. */
    public static long chargeItem(ItemStack itemStack, long joules)
    {
        return chargeItem(itemStack, joules, true);
    }

    /** Decharges an electric item.
     * 
     * @param joules - The joules being withdrawn from the electric item
     * @return The total amount of joules the provider received. */
    public static long dischargeItem(ItemStack itemStack, long joules)
    {
        return dischargeItem(itemStack, joules, true);
    }

    /** Returns an uncharged version of the electric item. Use this if you want the crafting recipe
     * to use a charged version of the electric item instead of an empty version of the electric
     * item
     * 
     * @return An electrical ItemStack with a specific charge. */
    public static ItemStack getWithCharge(ItemStack itemStack, long joules)
    {
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof IElectricalItem)
            {
                ((IElectricalItem) itemStack.getItem()).setElectricity(itemStack, joules);
                return itemStack;
            }
        }

        return itemStack;
    }

    public static ItemStack getWithCharge(Item item, long joules)
    {
        return getWithCharge(new ItemStack(item), joules);
    }

    public static ItemStack getCloneWithCharge(ItemStack itemStack, long joules)
    {
        return getWithCharge(itemStack.copy(), joules);
    }

    public static ItemStack getUncharged(ItemStack itemStack)
    {
        return getWithCharge(itemStack, 0);
    }

    public static ItemStack getUncharged(Item item)
    {
        return getUncharged(new ItemStack(item));
    }
}
