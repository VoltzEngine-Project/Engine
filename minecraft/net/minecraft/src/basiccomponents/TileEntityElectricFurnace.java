package net.minecraft.src.basiccomponents;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.forge.ISidedInventory;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.electricity.ElectricityManager;
import net.minecraft.src.universalelectricity.electricity.IElectricUnit;
import net.minecraft.src.universalelectricity.electricity.TileEntityElectricUnit;
import net.minecraft.src.universalelectricity.extend.IRotatable;
import net.minecraft.src.universalelectricity.extend.ItemElectric;
import net.minecraft.src.universalelectricity.network.IPacketReceiver;

public class TileEntityElectricFurnace extends TileEntityElectricUnit implements ITextureProvider, IInventory, ISidedInventory,  IPacketReceiver
{
	//The amount of ticks requried to smelt this item
	public static final int smeltingTimeRequired = 160;
	
	//How many ticks has this item been smelting for?
	public int smeltingTicks = 0;
	
	 /**
     * The ItemStacks that hold the items currently being used in the battery box
     */
    private ItemStack[] containingItems = new ItemStack[3];
    
  	public TileEntityElectricFurnace()
	{
  		BasicComponents.packetManager.registerPacketUser(this);
  		ElectricityManager.registerElectricUnit(this);
	}
  	
    @Override
    public boolean canReceiveElectricity(byte side)
    {
    	return side == UniversalElectricity.getOrientationFromSide((byte)this.getBlockMetadata(), (byte)3) && !this.isDisabled();
    }
  
    @Override
	public void onUpdate(float watts, float voltage, byte side)
	{
    	super.onUpdate(watts, voltage, side);
    	
		if(voltage > this.getVoltage())
    	{
    		 this.worldObj.createExplosion((Entity)null, this.xCoord, this.yCoord, this.zCoord, 0.5F);
    	}
		    	
    	if(!this.isDisabled() && watts > 5*this.getTickInterval())
    	{
    		if(!this.worldObj.isRemote)
	        {
		    	//The bottom slot is for portable batteries
		    	if (this.containingItems[0] != null)
		        {
		            if (this.containingItems[0].getItem() instanceof ItemElectric)
		            {
			           	ItemElectric electricItem = (ItemElectric)this.containingItems[0].getItem();
			           	
		            	if(electricItem.canProduceElectricity())
			           	{
			            	double receivedElectricity = electricItem.onUseElectricity(electricItem.getTransferRate(), this.containingItems[0]);
			            	//this.onUpdate(receivedElectricity, electricItem.getVolts(), (byte)-1);
			            }
		            }
		        }
		    	//The left slot contains the item to be smelted
		    	if(this.containingItems[1] != null && this.canSmelt() && this.smeltingTicks == 0)
		        {
		        	this.smeltingTicks = this.smeltingTimeRequired;
		        }
		    	
		        //Checks if the item can be smelted and if the smelting time left is greater than 0, if so, then smelt the item.
		        if(this.canSmelt() && this.smeltingTicks > 0)
		    	{
		    		//Update some variables.
		    		this.smeltingTicks --;
		    		//When the item is finished smelting
		    		if(this.smeltingTicks == 0)
		    		{
		    			if(this.containingItems[2] == null)
		    			{
		    				this.containingItems[2] = FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1]);
		    			}
		    			else if(this.containingItems[2] == FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1]))
		    			{
		    				this.containingItems[2].stackSize ++;
		    			}
		    			
		    			this.decrStackSize(1, 1);
		    			this.smeltingTicks = 0;
		    		}
		    	}
	        }
    	}
	}
    
    //Check all conditions and see if we can start smelting
    public boolean canSmelt()
    {
    	if(FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1]) == null)
    	{
    		return false;
    	}
    	
    	if(this.containingItems[1] == null)
    	{
    		return false;
    	}
    	
    	if(this.containingItems[2] != null)
    	{
	    	if(!this.containingItems[2].isItemEqual(FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1])))
			{
				return false;
			}
			if(this.containingItems[2].stackSize + 1 > 64)
			{
				return false;
			}
    	}
		
    	return true;
    }
    /**
     * Reads a tile entity from NBT.
     */
    @Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
    	this.smeltingTicks = par1NBTTagCompound.getInteger("smeltingTicks");
    	NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.containingItems = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");
            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }
    /**
     * Writes a tile entity to NBT.
     */
    @Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("smeltingTicks", this.smeltingTicks);
    	NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.containingItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        par1NBTTagCompound.setTag("Items", var2);
    }

	@Override
	public int getStartInventorySide(int side)
	{
		if (side == 0)
        {
            return 1;
        }
        if (side == 1)
        {
            return 0;
        }
        return 2;
	}
	@Override
	public int getSizeInventorySide(int side) { return getSizeInventory(); }
	@Override
	public int getSizeInventory() { return this.containingItems.length; }
	@Override
	public ItemStack getStackInSlot(int par1) { return this.containingItems[par1]; }
	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.containingItems[par1] != null)
        {
            ItemStack var3;
            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);
                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
                }
                return var3;
            }
        }
        else
        {
            return null;
        }
	}
	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.containingItems[par1] != null)
        {
            ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
	}
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.containingItems[par1] = par2ItemStack;
        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
	}
	@Override
	public String getInvName() {
		return "Electric Furnace";
	}
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}
	@Override
	public void openChest() { }
	@Override
	public void closeChest() { }

	@Override
	public String getTextureFile()
	{
		return BasicComponents.blockTextureFile;
	}

	@Override
	public float getVoltage()
	{
		return 120F;
	}

	@Override
	public void onPacketData(NetworkManager network, String channel, byte[] data)
	{
		DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(data));

        try
        {
        	int packetID = dataStream.readInt();
        	this.smeltingTicks = (int)dataStream.readDouble();
        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
	}

	@Override
	public int getPacketID()
	{
		return 3;
	}

	
	@Override
	public int getTickInterval()
	{
		return 2;
	}
}
