package net.minecraft.src.universalelectricity.components;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.forge.ISidedInventory;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.universalelectricity.UEElectricItem;
import net.minecraft.src.universalelectricity.UEIConsumer;
import net.minecraft.src.universalelectricity.UEIRotatable;
import net.minecraft.src.universalelectricity.UniversalElectricity;

public class TileEntityElectricFurnace extends TileEntity implements ITextureProvider, UEIConsumer, IInventory, ISidedInventory, UEIRotatable
{
	public int electricityStored = 0;
	//How many ticks has this item been smelting for?
	public int smeltingTicks = 0;
	public byte facingDirection = 0;
	//Constants
	//The amount of ticks requried to smelt this item
	public final int smeltingTimeRequired = 160;
	 /**
     * The ItemStacks that hold the items currently being used in the battery box
     */
    private ItemStack[] containingItems = new ItemStack[3];
    
    //The ticks in which this tile entity is disabled. -1 = Not disabled
  	private int disableTicks = -1;
    
    /**
	 * onRecieveElectricity is called whenever a Universal Electric conductor sends a packet of electricity to the consumer (which is this block).
	 * @param watts - The amount of watts this block received.
	 * @param voltage - The voltage the tile entity is receiving.
	 * @param side - The side of the block in which the electricity came from.
	 * @return watts - The amount of rejected power to be sent back into the conductor
	 */
    @Override
    public int onReceiveElectricity(int watts, int voltage, byte side)
    {
    	if(voltage > this.getVolts())
    	{
    		 this.worldObj.createExplosion((Entity)null, this.xCoord, this.yCoord, this.zCoord, 0.5F);
    	}
    	
    	//Only accept electricity from the front side
    	if(canReceiveElectricity(side) || side == -1)
		{
	    	int rejectedElectricity = Math.max((this.electricityStored + watts) - this.getElectricityCapacity(), 0);
			this.electricityStored = Math.max(this.electricityStored+watts - rejectedElectricity, 0);
			return rejectedElectricity;
		}
    	return watts;
    }
    
    @Override
    public boolean canReceiveElectricity(byte side)
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
    	if(disableTicks > -1)
    	{
    		this.disableTicks --;
    	}
    	else
    	{
	    	//The bottom slot is for portable batteries
	    	if (this.containingItems[0] != null && this.electricityStored < this.getElectricityCapacity())
	        {
	            if (this.containingItems[0].getItem() instanceof UEElectricItem)
	            {
		           	UEElectricItem electricItem = (UEElectricItem)this.containingItems[0].getItem();
		           	
	            	if(electricItem.canProduceElectricity())
		           	{
		            	int receivedElectricity = electricItem.onUseElectricity(electricItem.getTransferRate(), this.containingItems[0]);
		            	this.onReceiveElectricity(receivedElectricity, electricItem.getVolts(), (byte)-1);
		            }
	            }
	        }
	    	//The left slot contains the item to be smelted
	    	if(this.containingItems[1] != null && this.canSmelt() && this.smeltingTicks == 0)
	        {
		    	//Use all the electricity
	        	this.electricityStored = 0;
	        	this.smeltingTicks = this.smeltingTimeRequired;
	        }
	    	
	        //Checks if the item can be smelted and if the smelting time left is greater than 0, if so, then smelt the item.
	        if(this.canSmelt() && this.smeltingTicks > 0)
	    	{
	    		//Update some variables.
	    		this.smeltingTicks --;
	    		this.electricityStored -= this.getElectricityCapacity()/this.smeltingTimeRequired;
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
    	
		if(this.electricityStored < this.getElectricityCapacity()/this.smeltingTimeRequired)
		{
			return false;
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
    	this.electricityStored = par1NBTTagCompound.getInteger("electricityStored");
    	this.facingDirection = par1NBTTagCompound.getByte("facingDirection");
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
    	par1NBTTagCompound.setInteger("electricityStored", this.electricityStored);
    	par1NBTTagCompound.setByte("facingDirection", this.facingDirection);
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
    /**
	 * @return Return the stored electricity in this consumer. Called by conductors to spread electricity to this unit.
	 */
    @Override
	public int getStoredElectricity()
    {
    	return this.electricityStored;
    }
    @Override
    public int getElectricityCapacity()
	{
		return 1800;
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
}
