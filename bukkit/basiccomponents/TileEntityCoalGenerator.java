package basiccomponents;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntity;
import universalelectricity.UniversalElectricity;
import universalelectricity.Vector3;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.electricity.TileEntityElectricUnit;
import universalelectricity.extend.ISlotInput;
import universalelectricity.extend.TileEntityConductor;
import universalelectricity.network.IPacketSender;
import forge.ISidedInventory;
import forge.ITextureProvider;


/**
 * The Class TileEntityCoalGenerator.
 */
public class TileEntityCoalGenerator extends TileEntityElectricUnit implements  ISlotInput, IPacketSender, ITextureProvider, IInventory, ISidedInventory
{
    
    /** The Constant maxGenerateRate. */
    public static final int maxGenerateRate = 560;
    
    /** The generate rate. */
    public float generateRate;
    
    /** The connected electric unit. */
    public TileEntityConductor connectedElectricUnit;
    
    /** The item cook time. */
    public int itemCookTime;
    
    /** The containing items. */
    private ItemStack containingItems[];

    /**
     * Instantiates a new tile entity coal generator.
     */
    public TileEntityCoalGenerator()
    {
        generateRate = 0.0F;
        connectedElectricUnit = null;
        itemCookTime = 0;
        containingItems = new ItemStack[1];
        ElectricityManager.registerElectricUnit(this);
    }

    /**
     * Can produce electricity.
     *
     * @param byte0 the byte0
     * @return true, if successful
     */
    public boolean canProduceElectricity(byte byte0)
    {
        return canConnect(byte0) && !isDisabled();
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.IElectricUnit#canReceiveFromSide(byte)
     */
    public boolean canReceiveFromSide(byte byte0)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.TileEntityElectricUnit#canConnect(byte)
     */
    public boolean canConnect(byte byte0)
    {
        return byte0 == k();
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.TileEntityElectricUnit#onUpdate(float, float, byte)
     */
    public void onUpdate(float f, float f1, byte byte0)
    {
        TileEntity tileentity = UniversalElectricity.getUEUnitFromSide(world, new Vector3(x, y, z), UniversalElectricity.getOrientationFromSide((byte)k(), (byte)3));

        if (tileentity instanceof TileEntityConductor)
        {
            if (ElectricityManager.electricityRequired(((TileEntityConductor)tileentity).connectionID) > 0.0F)
            {
                connectedElectricUnit = (TileEntityConductor)tileentity;
            }
            else
            {
                connectedElectricUnit = null;
            }
        }
        else
        {
            connectedElectricUnit = null;
        }

        if (!world.isStatic)
        {
            super.onUpdate(f, f1, byte0);

            if (!isDisabled())
            {
                if (containingItems[0] != null && connectedElectricUnit != null && containingItems[0].getItem().id == Item.COAL.id && itemCookTime <= 0)
                {
                    itemCookTime = Math.max(500 - (int)(generateRate * 20F), 200);
                    splitStack(0, 1);
                }

                if (itemCookTime > 0)
                {
                    itemCookTime--;

                    if (connectedElectricUnit != null)
                    {
                        generateRate = (float)Math.min((double)generateRate + Math.min((double)generateRate * 0.001D + 0.0015D, 0.05000000074505806D), 560 / 20);
                    }
                }

                if (connectedElectricUnit == null || itemCookTime <= 0)
                {
                    generateRate = (float)Math.max((double)generateRate - 0.050000000000000003D, 0.0D);
                }

                if (generateRate > 1.0F)
                {
                    ElectricityManager.produceElectricity(connectedElectricUnit, generateRate * (float)getTickInterval(), getVoltage());
                }
            }
        }

        BasicComponents.packetManager.sendPacketData(this, new double[]
                {
                    (double)generateRate, (double)disabledTicks
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
        itemCookTime = nbttagcompound.getInt("itemCookTime");
        generateRate = nbttagcompound.getFloat("generateRate");
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
        nbttagcompound.setInt("itemCookTime", itemCookTime);
        nbttagcompound.setFloat("generateRate", (int)generateRate);
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
        return "Coal Generator";
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
        return 1;
    }

    /* (non-Javadoc)
     * @see universalelectricity.electricity.IElectricUnit#electricityRequest()
     */
    public float electricityRequest()
    {
        return 0.0F;
    }

    /* (non-Javadoc)
     * @see universalelectricity.network.IPacketSender#getPacketID()
     */
    public int getPacketID()
    {
        return 2;
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
	 * @see universalelectricity.extend.ISlotInput#getSlotInputs(net.minecraft.server.ItemStack)
	 */
	@Override
	public int[] getSlotInputs(ItemStack item)
	{
		if(item.id == Item.COAL.id)
		{
			return new int[]{0};
		}
		
		return null;
	}

}
