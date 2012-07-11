package universalelectricity.extend;

import java.util.ArrayList;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;


/**
 * The Class ItemElectric.
 */
public abstract class ItemElectric extends Item
{
    
    /**
     * Instantiates a new item electric.
     *
     * @param i the i
     */
    public ItemElectric(int i)
    {
        super(i);
        e(1);
        setMaxDurability((int)getElectricityCapacity());
        setNoRepair();
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     *
     * @param itemstack the itemstack
     * @param world the world
     * @param entityhuman the entityhuman
     */
    public void d(ItemStack itemstack, World world, EntityHuman entityhuman)
    {
        itemstack.setData((int)getElectricityCapacity());
    }

    /**
     * On receive electricity.
     *
     * @param f the f
     * @param itemstack the itemstack
     * @return the float
     */
    public float onReceiveElectricity(float f, ItemStack itemstack)
    {
        float f1 = Math.max((getElectricityStored(itemstack) + f) - getElectricityCapacity(), 0.0F);
        setElectricityStored(itemstack, (getElectricityStored(itemstack) + f) - f1);
        return f1;
    }

    /**
     * On use electricity.
     *
     * @param f the f
     * @param itemstack the itemstack
     * @return the float
     */
    public float onUseElectricity(float f, ItemStack itemstack)
    {
        float f1 = Math.min(getElectricityStored(itemstack), f);
        setElectricityStored(itemstack, getElectricityStored(itemstack) - f1);
        return f1;
    }

    /**
     * Can receive electricity.
     *
     * @return true, if successful
     */
    public boolean canReceiveElectricity()
    {
        return true;
    }

    /**
     * Can produce electricity.
     *
     * @return true, if successful
     */
    public boolean canProduceElectricity()
    {
        return false;
    }

    /**
     * Sets the electricity stored.
     *
     * @param itemstack the itemstack
     * @param f the f
     */
    protected void setElectricityStored(ItemStack itemstack, float f)
    {
        if (itemstack.tag == null)
        {
            itemstack.setTag(new NBTTagCompound());
        }

        float f1 = Math.max(Math.min(f, getElectricityCapacity()), 0.0F);
        itemstack.tag.setFloat("electricity", f1);
        itemstack.setData((int)(getElectricityCapacity() - f1));
    }

    /**
     * Gets the electricity stored.
     *
     * @param itemstack the itemstack
     * @return the electricity stored
     */
    protected float getElectricityStored(ItemStack itemstack)
    {
        if (itemstack.tag == null)
        {
            return 0.0F;
        }
        else
        {
            float f = itemstack.tag.getFloat("electricity");
            itemstack.setData((int)(getElectricityCapacity() - f));
            return f;
        }
    }

    /**
     * Gets the electricity capacity.
     *
     * @return the electricity capacity
     */
    public abstract float getElectricityCapacity();

    /**
     * Gets the transfer rate.
     *
     * @return the transfer rate
     */
    public abstract float getTransferRate();

    /**
     * Gets the volts.
     *
     * @return the volts
     */
    public abstract float getVolts();

    /**
     * Gets the charged item stack.
     *
     * @return the charged item stack
     */
    public ItemStack getChargedItemStack()
    {
        ItemStack itemstack = new ItemStack(this);
        itemstack.setData((int)getElectricityCapacity());
        return itemstack;
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Item#addCreativeItems(java.util.ArrayList)
     */
    public void addCreativeItems(ArrayList arraylist)
    {
        ItemStack itemstack = new ItemStack(this, 1);
        itemstack.setData((int)getElectricityCapacity());
        arraylist.add(itemstack);
        ItemStack itemstack1 = new ItemStack(this, 1);
        setElectricityStored(itemstack1, ((ItemElectric)itemstack1.getItem()).getElectricityCapacity());
        arraylist.add(itemstack1);
    }
}
