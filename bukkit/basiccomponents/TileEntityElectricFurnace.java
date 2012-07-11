package basiccomponents;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.FurnaceRecipes;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.electricity.TileEntityElectricUnit;
import universalelectricity.extend.ItemElectric;
import universalelectricity.network.IPacketSender;
import forge.ISidedInventory;
import forge.ITextureProvider;


/**
 * The Class TileEntityElectricFurnace.
 */
public class TileEntityElectricFurnace extends TileEntityElectricUnit implements IPacketSender, ITextureProvider, IInventory, ISidedInventory
{
    
    /** The smelting time required. */
    public final int smeltingTimeRequired = 150;
    
    /** The smelting ticks. */
    public int smeltingTicks;
    
    /** The electricity stored. */
    public float electricityStored;
    
    /** The electricity required. */
    public final float electricityRequired = 4F;
    
    /** The containing items. */
    private ItemStack containingItems[];

    /**
     * Instantiates a new tile entity electric furnace.
     */
    public TileEntityElectricFurnace()
    {
        smeltingTicks = 0;
        electricityStored = 0.0F;
        containingItems = new ItemStack[3];
        ElectricityManager.registerElectricUnit(this);
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.IElectricUnit#electricityRequest()
     */
    public float electricityRequest()
    {
        if (!isDisabled() && canSmelt())
        {
            getClass();
            return Math.max(0.0F, 4F - electricityStored);
        }
        else
        {
            return 0.0F;
        }
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.IElectricUnit#canReceiveFromSide(byte)
     */
    public boolean canReceiveFromSide(byte byte0)
    {
        return byte0 == k();
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.TileEntityElectricUnit#onUpdate(float, float, byte)
     */
    public void onUpdate(float f, float f1, byte byte0)
    {
        super.onUpdate(f, f1, byte0);

        if (!world.isStatic)
        {
            if (f1 > getVoltage())
            {
                world.explode((Entity)null, x, y, z, 0.7F);
            }

            if (containingItems[0] != null && (containingItems[0].getItem() instanceof ItemElectric))
            {
                ItemElectric itemelectric = (ItemElectric)containingItems[0].getItem();

                if (itemelectric.canProduceElectricity())
                {
                    double d = itemelectric.onUseElectricity(itemelectric.getTransferRate(), containingItems[0]);
                    electricityStored += d;
                }
            }

            electricityStored += f;
            getClass();

            if (electricityStored >= 4F && !isDisabled())
            {
                if (containingItems[1] != null && canSmelt() && smeltingTicks == 0)
                {
                    getClass();
                    smeltingTicks = 150;
                }

                if (canSmelt() && smeltingTicks > 0)
                {
                    smeltingTicks -= getTickInterval();

                    if (smeltingTicks < 1 * getTickInterval())
                    {
                        smeltItem();
                        smeltingTicks = 0;
                    }
                }
                else
                {
                    smeltingTicks = 0;
                }

                electricityStored = 0.0F;
            }
        }

        BasicComponents.packetManager.sendPacketData(this, new double[]
                {
                    (double)smeltingTicks, (double)disabledTicks
                });
    }

    /**
     * Can smelt.
     *
     * @return true, if successful
     */
    public boolean canSmelt()
    {
        if (FurnaceRecipes.getInstance().getSmeltingResult(containingItems[1]) == null)
        {
            return false;
        }

        if (containingItems[1] == null)
        {
            return false;
        }

        if (containingItems[2] != null)
        {
            if (!containingItems[2].doMaterialsMatch(FurnaceRecipes.getInstance().getSmeltingResult(containingItems[1])))
            {
                return false;
            }

            if (containingItems[2].count + 1 > 64)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Smelt item.
     */
    public void smeltItem()
    {
        if (canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.getInstance().getSmeltingResult(containingItems[1]);

            if (containingItems[2] == null)
            {
                containingItems[2] = itemstack.cloneItemStack();
            }
            else if (containingItems[2].doMaterialsMatch(itemstack))
            {
                containingItems[2].count++;
            }

            containingItems[1].count--;

            if (containingItems[1].count <= 0)
            {
                containingItems[1] = null;
            }
        }
    }

    /**
     * Reads a tile entity from NBT.
     *
     * @param nbttagcompound the nbttagcompound
     */
    public void a(NBTTagCompound nbttagcompound)
    {
        super.a(nbttagcompound);
        smeltingTicks = nbttagcompound.getInt("smeltingTicks");
        NBTTagList nbttaglist = nbttagcompound.getList("Items");
        containingItems = new ItemStack[getSize()];

        for (int i = 0; i < nbttaglist.size(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.get(i);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if (byte0 >= 0 && byte0 < containingItems.length)
            {
                containingItems[byte0] = ItemStack.a(nbttagcompound1);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     *
     * @param nbttagcompound the nbttagcompound
     */
    public void b(NBTTagCompound nbttagcompound)
    {
        super.b(nbttagcompound);
        nbttagcompound.setInt("smeltingTicks", smeltingTicks);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < containingItems.length; i++)
        {
            if (containingItems[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                containingItems[i].save(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.set("Items", nbttaglist);
    }

    /* (non-Javadoc)
     * @see forge.ISidedInventory#getStartInventorySide(int)
     */
    public int getStartInventorySide(int i)
    {
        if (i == 0)
        {
            return 1;
        }

        return i != 1 ? 2 : 0;
    }

    /* (non-Javadoc)
     * @see forge.ISidedInventory#getSizeInventorySide(int)
     */
    public int getSizeInventorySide(int i)
    {
        return getSize();
    }

    /**
     * Returns the number of slots in the inventory.
     *
     * @return the size
     */
    public int getSize()
    {
        return containingItems.length;
    }

    /**
     * Returns the stack in slot i.
     *
     * @param i the i
     * @return the item
     */
    public ItemStack getItem(int i)
    {
        return containingItems[i];
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     *
     * @param i the i
     * @param j the j
     * @return the item stack
     */
    public ItemStack splitStack(int i, int j)
    {
        if (containingItems[i] != null)
        {
            if (containingItems[i].count <= j)
            {
                ItemStack itemstack = containingItems[i];
                containingItems[i] = null;
                return itemstack;
            }

            ItemStack itemstack1 = containingItems[i].a(j);

            if (containingItems[i].count == 0)
            {
                containingItems[i] = null;
            }

            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     *
     * @param i the i
     * @return the item stack
     */
    public ItemStack splitWithoutUpdate(int i)
    {
        if (containingItems[i] != null)
        {
            ItemStack itemstack = containingItems[i];
            containingItems[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     *
     * @param i the i
     * @param itemstack the itemstack
     */
    public void setItem(int i, ItemStack itemstack)
    {
        containingItems[i] = itemstack;

        if (itemstack != null && itemstack.count > getMaxStackSize())
        {
            itemstack.count = getMaxStackSize();
        }
    }

    /**
     * Returns the name of the inventory.
     *
     * @return the name
     */
    public String getName()
    {
        return "Electric Furnace";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     *
     * @return the max stack size
     */
    public int getMaxStackSize()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container.
     *
     * @param entityhuman the entityhuman
     * @return true, if successful
     */
    public boolean a(EntityHuman entityhuman)
    {
        return world.getTileEntity(x, y, z) == this ? entityhuman.e((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D : false;
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.IInventory#f()
     */
    public void f()
    {
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.IInventory#g()
     */
    public void g()
    {
    }

    /* (non-Javadoc)
     * @see forge.ITextureProvider#getTextureFile()
     */
    public String getTextureFile()
    {
        return "/basiccomponents/textures/blocks.png";
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.TileEntityElectricUnit#getVoltage()
     */
    public float getVoltage()
    {
        return 120F;
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.TileEntityElectricUnit#getTickInterval()
     */
    public int getTickInterval()
    {
        return 3;
    }

    /* (non-Javadoc)
     * @see universalelectricity.network.IPacketSender#getPacketID()
     */
    public int getPacketID()
    {
        return 3;
    }

	/* (non-Javadoc)
	 * @see net.minecraft.server.IInventory#getContents()
	 */
	@Override
	public ItemStack[] getContents() {
		return this.containingItems;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.server.IInventory#setMaxStackSize(int)
	 */
	@Override
	public void setMaxStackSize(int arg0) {
		
	}
}
