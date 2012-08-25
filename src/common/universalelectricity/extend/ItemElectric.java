package universalelectricity.extend;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import universalelectricity.UniversalElectricity;

/**
 * REQUIRED
 * Extend from this class if your item requires electricity or to be charged.
 * @author Henry
 *
 */
public abstract class ItemElectric extends Item implements IItemElectric
{
    public ItemElectric(int par1)
    {
        super(par1);
        this.setMaxStackSize(1);
        this.setMaxDamage((int) getElectricityCapacity());
        this.setNoRepair();
        this.setTabToDisplayOn(CreativeTabs.tabTools);
    }

    /**
     * Allows items to add custom lines of information to the mouseover description. If you want to add more
     * information to your item, you can super.addInformation() to keep the electiricty info in the item info bar.
     */
    @Override
    public void addInformation(ItemStack par1ItemStack, List par2List)
    {
        String color = "";
        float watts = this.getElectricityStored(par1ItemStack);

        if (watts <= this.getElectricityCapacity() / 3)
        {
            color = "\u00a74";
        }
        else if (watts > this.getElectricityCapacity() * 2 / 3)
        {
            color = "\u00a72";
        }
        else
        {
            color = "\u00a76";
        }

        par2List.add(color + UniversalElectricity.getAmpHourDisplay(watts, this.getVoltage()) + " - " + Math.round((watts / this.getElectricityCapacity()) * 100) + "%");
    }

    @Override
    /**
     * Makes sure the item is uncharged when it is crafted and not charged.
     * Change this if you do not want this to happen!
     */
    public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par1ItemStack.setItemDamage((int) this.getElectricityCapacity());
    }

    /**
     * Called when this item is being "recharged" or receiving electricity.
     * @param ampHours - The amount of joules this item is receiving.
     * @param itemStack - The ItemStack of this item
     * @return Return the rejected electricity from this item
     */
    public float onReceiveElectricity(float ampHours, ItemStack itemStack)
    {
        float rejectedElectricity = Math.max((this.getElectricityStored(itemStack) + ampHours) - this.getElectricityCapacity(), 0);
        this.setElectricityStored(itemStack, this.getElectricityStored(itemStack) + ampHours - rejectedElectricity);
        return rejectedElectricity;
    }

    /**
     * Called when this item's electricity is being used.
     * @param ampHours - The amount of electricity requested from this item
     * @param itemStack - The ItemStack of this item
     * @return The electricity that is given to the requester
     */
    public float onUseElectricity(float ampHours, ItemStack itemStack)
    {
        float electricityToUse = Math.min(this.getElectricityStored(itemStack), ampHours);
        this.setElectricityStored(itemStack, this.getElectricityStored(itemStack) - electricityToUse);
        return electricityToUse;
    }

    /**
     * @return Returns true or false if this consumer can receive electricity at this given tick or moment.
     */
    public boolean canReceiveElectricity()
    {
        return true;
    }

    /**
     * Can this item give out electricity when placed in an tile entity? Electric items like batteries
     * should be able to produce electricity (if they are rechargable).
     * @return - True or False.
     */
    public boolean canProduceElectricity()
    {
        return false;
    }

    /**
     * This function sets the electriicty. Do not directly call this function.
     * Try to use onReceiveElectricity or onUseElectricity instead.
     * @param ampHours - The amount of electricity in joules
     */
    public void setElectricityStored(ItemStack itemStack, float ampHours)
    {
        //Saves the frequency in the itemstack
        if (itemStack.stackTagCompound == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        float electricityStored = Math.max(Math.min(ampHours, this.getElectricityCapacity()), 0);
        itemStack.stackTagCompound.setFloat("electricity", electricityStored);
        itemStack.setItemDamage((int)(getElectricityCapacity() - electricityStored));
    }

    /**
     * This function is called to get the electricity stored in this item
     * @return - The amount of electricity stored in watts
     */
    public float getElectricityStored(ItemStack itemStack)
    {
        if(itemStack.stackTagCompound == null)
        {
            return 0;
        }

        float electricityStored = itemStack.stackTagCompound.getFloat("electricity");
        itemStack.setItemDamage((int)(getElectricityCapacity() - electricityStored));
        return electricityStored;
    }

    /**
     * This function is called to get the electricity maximum capacity in this item
     * @return - The amount of electricity maximum capacity
     */
    public abstract float getElectricityCapacity();

    /**
     * This function is called to get the maximum transfer rate this electric item can receive per tick
     * @return - The amount of electricity maximum capacity
     */
    public abstract float getTransferRate();

    /**
     * Gets the voltage of this item
     * @return The Voltage of this item
     */
    public abstract float getVoltage();

    /**
     * Returns a charged version of the electric item. Use this if you want
     * the crafting recipe to use a charged version of the electric item
     * instead of an empty version of the electric item
     * @return The ItemStack of a fully charged electric item
     */
    public ItemStack getChargedItemStack()
    {
        ItemStack chargedItem = new ItemStack(this);
        chargedItem.setItemDamage((int) this.getElectricityCapacity());
        return chargedItem;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	//Add an uncharged version of the electric item
        ItemStack unchargedItem = new ItemStack(this, 1);
        unchargedItem.setItemDamage((int) this.getElectricityCapacity());
        par3List.add(unchargedItem);
        //Add an electric item to the creative list that is fully charged
        ItemStack chargedItem = new ItemStack(this, 1);
        this.setElectricityStored(chargedItem, ((IItemElectric)chargedItem.getItem()).getElectricityCapacity());
        par3List.add(chargedItem);
    }
}
