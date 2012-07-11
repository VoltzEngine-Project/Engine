package basiccomponents;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import universalelectricity.UniversalElectricity;
import universalelectricity.Vector3;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.electricity.TileEntityElectricUnit;
import universalelectricity.extend.IRedstoneProvider;
import universalelectricity.extend.ISlotInput;
import universalelectricity.extend.ISlotOuput;
import universalelectricity.extend.ItemElectric;
import universalelectricity.extend.TileEntityConductor;
import universalelectricity.network.IPacketSender;
import forge.ISidedInventory;
import forge.ITextureProvider;


/**
 * The Class TileEntityBatteryBox.
 */
public class TileEntityBatteryBox extends TileEntityElectricUnit implements ISlotInput, ISlotOuput, IRedstoneProvider, ITextureProvider, IInventory, ISidedInventory, IPacketSender
{
    
    /** The electricity stored. */
    public float electricityStored;
    
    /** The containing items. */
    private ItemStack containingItems[];
    
    /** The is full. */
    private boolean isFull;

    /**
     * Instantiates a new tile entity battery box.
     */
    public TileEntityBatteryBox()
    {
        electricityStored = 0.0F;
        containingItems = new ItemStack[2];
        ElectricityManager.registerElectricUnit(this);
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.IElectricUnit#electricityRequest()
     */
    public float electricityRequest()
    {
        if (!isDisabled())
        {
            return getElectricityCapacity() - electricityStored;
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
     * @see universalelectricity.electricity.TileEntityElectricUnit#canConnect(byte)
     */
    public boolean canConnect(byte byte0)
    {
        return canReceiveFromSide(byte0) || byte0 == UniversalElectricity.getOrientationFromSide((byte)k(), (byte)2);
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.TileEntityElectricUnit#onUpdate(float, float, byte)
     */
    public void onUpdate(float f, float f1, byte byte0)
    {
        if (!world.isStatic)
        {
            super.onUpdate(f, f1, byte0);

            if (!isDisabled())
            {
                if (electricityRequest() > 0.0F && canConnect(byte0))
                {
                    float f2 = (float)Math.max((electricityStored + f) - getElectricityCapacity(), 0.0D);
                    electricityStored = (float)Math.max((electricityStored + f) - f2, 0.0D);
                }

                if (containingItems[0] != null && electricityStored > 0.0F && (containingItems[0].getItem() instanceof ItemElectric))
                {
                    ItemElectric itemelectric = (ItemElectric)containingItems[0].getItem();
                    float f3 = itemelectric.onReceiveElectricity(itemelectric.getTransferRate(), containingItems[0]);
                    electricityStored -= itemelectric.getTransferRate() - f3;
                }

                if (containingItems[1] != null && electricityStored < getElectricityCapacity() && (containingItems[1].getItem() instanceof ItemElectric))
                {
                    ItemElectric itemelectric1 = (ItemElectric)containingItems[1].getItem();

                    if (itemelectric1.canProduceElectricity())
                    {
                        float f4 = itemelectric1.onUseElectricity(itemelectric1.getTransferRate(), containingItems[1]);
                        electricityStored = Math.max(electricityStored + f4, 0.0F);
                    }
                }

                boolean flag = false;

                if (electricityStored >= getElectricityCapacity())
                {
                    flag = true;
                }

                if (isFull != flag)
                {
                    isFull = flag;
                    world.applyPhysics(x, y, z, getBlockType().id);
                }

                if (electricityStored > 0.0F)
                {
                    net.minecraft.server.TileEntity tileentity = UniversalElectricity.getUEUnitFromSide(world, new Vector3(x, y, z), UniversalElectricity.getOrientationFromSide((byte)k(), (byte)2));

                    if (tileentity != null && (tileentity instanceof TileEntityConductor))
                    {
                        float f5 = ElectricityManager.electricityRequired(((TileEntityConductor)tileentity).connectionID);
                        float f6 = Math.min(100F, Math.min(electricityStored, f5));
                        ElectricityManager.produceElectricity((TileEntityConductor)tileentity, f6, getVoltage());
                        electricityStored -= f6;
                    }
                }
            }
        }

        BasicComponents.packetManager.sendPacketData(this, new double[]
                {
                    (double)electricityStored, (double)disabledTicks
                });
    }

    /**
     * Reads a tile entity from NBT.
     *
     * @param nbttagcompound the nbttagcompound
     */
    public void a(NBTTagCompound nbttagcompound)
    {
        super.a(nbttagcompound);
        electricityStored = nbttagcompound.getFloat("electricityStored");
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
        nbttagcompound.setFloat("electricityStored", electricityStored);
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

    /**
     * Gets the electricity capacity.
     *
     * @return the electricity capacity
     */
    public float getElectricityCapacity()
    {
        return 100000F;
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
        return "Battery Box";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     *
     * @return the max stack size
     */
    public int getMaxStackSize()
    {
        return 1;
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
     * @see universalelectricity.extend.IRedstoneProvider#isPoweringTo(byte)
     */
    public boolean isPoweringTo(byte byte0)
    {
        return isFull;
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.IRedstoneProvider#isIndirectlyPoweringTo(byte)
     */
    public boolean isIndirectlyPoweringTo(byte byte0)
    {
        return isPoweringTo(byte0);
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.TileEntityElectricUnit#getTickInterval()
     */
    public int getTickInterval()
    {
        return 1;
    }

    /* (non-Javadoc)
     * @see universalelectricity.network.IPacketSender#getPacketID()
     */
    public int getPacketID()
    {
        return 1;
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
		// TODO Auto-generated method stub
	}
	
	/* (non-Javadoc)
	 * @see universalelectricity.extend.ISlotOuput#getSlotOutputs()
	 */
	@Override
	public int[] getSlotOutputs()
	{
		return new int[]{0, 1};
	}

	/* (non-Javadoc)
	 * @see universalelectricity.extend.ISlotInput#getSlotInputs(net.minecraft.server.ItemStack)
	 */
	@Override
	public int[] getSlotInputs(ItemStack item)
	{
		if(item.getItem() instanceof ItemElectric)
		{
			return new int[]{0, 1};
		}
		
		return null;
	}

}
