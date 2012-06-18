package net.minecraft.src.universalelectricity.components;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.forge.ISidedInventory;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.universalelectricity.UEBlockConductor;
import net.minecraft.src.universalelectricity.UEIPacketReceiver;
import net.minecraft.src.universalelectricity.UEIProducer;
import net.minecraft.src.universalelectricity.UEIRotatable;
import net.minecraft.src.universalelectricity.UETileEntityConductor;
import net.minecraft.src.universalelectricity.UniversalElectricity;

public class TileEntityCoalGenerator extends TileEntity implements ITextureProvider, UEIProducer, IInventory, ISidedInventory, UEIRotatable, UEIPacketReceiver
{
	//Maximum possible generation rate of watts in SECONDS
	public static final int maxGenerateRate = 560;
		
	//The direction in which this tile entity is facing
	public byte facingDirection = 0;
	
	//Current generation rate based on hull heat. In TICKS.
	public float generateRate = 0;
		
	public UETileEntityConductor connectedWire = null;
	 /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    public int itemCookTime = 0;
	 /**
     * The ItemStacks that hold the items currently being used in the battery box
     */
    private ItemStack[] containingItems = new ItemStack[1];
    
    //The ticks in which this tile entity is disabled. -1 = Not disabled
  	private int disableTicks = -1;
  	
  	public TileEntityCoalGenerator()
  	{
  		UniversalComponents.packetManager.registerPacketUser(this);
  	}
  	    
    @Override
	public int onProduceElectricity(int maxWatt, int voltage, byte side)
    {
		//Only produce electricity on the back side.
    	if(canProduceElectricity(side) && maxWatt > 0)
		{
	        return Math.min(maxWatt, (int)generateRate);
		}
    	return 0;
	}
    
    @Override
    public boolean canProduceElectricity(byte side)
    {
    	return side == UniversalElectricity.getOrientationFromSide(this.facingDirection, (byte)2) && !this.isDisabled();
    }
    
    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    @Override
	public void updateEntity()
    {    	
    	//Check nearby blocks and see if the conductor is full. If so, then it is connected
    	TileEntity tileEntity = UEBlockConductor.getUEUnit(this.worldObj, this.xCoord, this.yCoord, this.zCoord, UniversalElectricity.getOrientationFromSide(this.facingDirection, (byte)2));
    	
    	if(tileEntity instanceof UETileEntityConductor)
    	{
    		this.connectedWire = (UETileEntityConductor)tileEntity;
    	}
    	else
    	{
    		this.connectedWire = null;
    	}
    	
    	if(disableTicks > -1)
    	{
    		this.disableTicks --;
    	}
    	else
    	{	
	    	if(!this.worldObj.isRemote)
	        {
		    	//The top slot is for recharging items. Check if the item is a electric item. If so, recharge it.
		    	if (this.containingItems[0] != null && this.connectedWire != null && this.connectedWire.getStoredElectricity() < this.connectedWire.getElectricityCapacity())
		        {
		            if(this.containingItems[0].getItem().shiftedIndex == Item.coal.shiftedIndex)
		            {
		                if(this.itemCookTime <= 0)
		                {
		            		itemCookTime = Math.max(500 - (int)(this.generateRate*20), 200);
		            		this.decrStackSize(0, 1);
		            	}
		            }
		        }
	        }
    	}
    	
    	if(!this.worldObj.isRemote)
        {
	    	//Starts generating electricity if the device is heated up
	    	if (this.itemCookTime > 0)
	        {
	            this.itemCookTime --;
	            
	            if(this.connectedWire != null && this.connectedWire.getStoredElectricity() < this.connectedWire.getElectricityCapacity() && !this.isDisabled())
	            {
	            	this.generateRate = (float)Math.min(this.generateRate+Math.min((this.generateRate)*0.001+0.0015, 0.05F), this.maxGenerateRate/20);
	            }
	        }
	
	    	if(this.connectedWire == null || this.itemCookTime <= 0)
	    	{
	        	this.generateRate = (float)Math.max(this.generateRate-0.05, 0);
	        }
        }
    }
    /**
     * Reads a tile entity from NBT.
     */
    @Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
    	this.itemCookTime = par1NBTTagCompound.getInteger("itemCookTime");
    	this.generateRate = par1NBTTagCompound.getFloat("generateRate");
    	this.facingDirection = par1NBTTagCompound.getByte("facingDirection");
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
    	par1NBTTagCompound.setInteger("itemCookTime", this.itemCookTime);
    	par1NBTTagCompound.setFloat("generateRate", (int)this.generateRate);
    	par1NBTTagCompound.setByte("facingDirection", this.facingDirection);
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
	public String getInvName() { return "Coal Generator"; }
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
	public byte getDirection()
	{
		return this.facingDirection;
	}
	
	@Override
	public void setDirection(byte facingDirection)
	{
		this.facingDirection = facingDirection;
	}
	
	@Override
	public String getTextureFile()
	{
		return UCBlock.textureFile;
	}

	@Override
	public int getVolts()
	{
		return 120;
	}
	
	@Override
	public void onDisable(int duration)
	{
		this.disableTicks = duration;
	}


	@Override
	public boolean isDisabled()
	{
		return this.disableTicks > -1;
	}
	
	@Override
	public void onPacketData(NetworkManager network, String channel, byte[] data)
	{		
		DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(data));

        try
        {
        	int packetID = dataStream.readInt();
        	this.facingDirection = (byte)dataStream.readDouble();
        	this.generateRate = (float)dataStream.readDouble();
        	this.disableTicks = (int)dataStream.readDouble();
        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
	}

	@Override
	public int getPacketID()
	{
		return 1;
	}
}
