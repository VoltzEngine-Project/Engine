package universalelectricity.basiccomponents;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.Vector3;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.electricity.TileEntityElectricUnit;
import universalelectricity.extend.IElectricityStorage;
import universalelectricity.extend.IItemElectric;
import universalelectricity.extend.IRedstoneProvider;
import universalelectricity.extend.TileEntityConductor;
import universalelectricity.network.IPacketReceiver;
import universalelectricity.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityBatteryBox extends TileEntityElectricUnit implements IElectricityStorage, IPacketReceiver, IRedstoneProvider, IInventory, ISidedInventory
{	
	private float wattHourStored = 0;

    private ItemStack[] containingItems = new ItemStack[2];

    private boolean isFull = false;
    
    private boolean isGUIOpen = false;

    public TileEntityBatteryBox()
    {
    	super();
    }

    @Override
    public float ampRequest()
    {
        if (!this.isDisabled())
        {
            return this.getMaxWattHours() - this.wattHourStored;
        }

        return 0;
    }

    @Override
    public boolean canReceiveFromSide(ForgeDirection side)
    {
        return side == ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    }

    @Override
    public boolean canConnect(ForgeDirection side)
    {
        return canReceiveFromSide(side) || side.ordinal() == this.getBlockMetadata();
    }

    @Override
    public void onUpdate(float amps, float voltage, ForgeDirection side)
    {
        super.onUpdate(amps, voltage, side);
        
        if (voltage > this.getVoltage())
        {
            this.worldObj.createExplosion((Entity)null, this.xCoord, this.yCoord, this.zCoord, 1F);
        }

        if (!this.isDisabled())
        {
        	this.setWattHours(this.wattHourStored+(amps*voltage));
            
            //The top slot is for recharging items. Check if the item is a electric item. If so, recharge it.
            if (this.containingItems[0] != null && this.wattHourStored > 0)
            {
                if (this.containingItems[0].getItem() instanceof IItemElectric)
                {
                    IItemElectric electricItem = (IItemElectric)this.containingItems[0].getItem();
                    float rejectedElectricity = electricItem.onReceiveElectricity(electricItem.getTransferRate(), this.containingItems[0]);
                    this.wattHourStored -= electricItem.getTransferRate() - rejectedElectricity;
                }
            }

            //The bottom slot is for decharging. Check if the item is a electric item. If so, decharge it.
            if (this.containingItems[1] != null && this.wattHourStored < this.getMaxWattHours())
            {
                if (this.containingItems[1].getItem() instanceof IItemElectric)
                {
                    IItemElectric electricItem = (IItemElectric)this.containingItems[1].getItem();

                    if (electricItem.canProduceElectricity())
                    {
                        float receivedElectricity = electricItem.onUseElectricity(electricItem.getTransferRate(), this.containingItems[1]);
                        this.wattHourStored = Math.max(this.wattHourStored + receivedElectricity, 0);
                    }
                }
            }

            boolean isFullThisCheck = false;

            if (this.wattHourStored >= this.getMaxWattHours())
            {
                isFullThisCheck = true;
            }

            if (this.isFull != isFullThisCheck)
            {
                this.isFull = isFullThisCheck;
                this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
            }

            if (this.wattHourStored > 0)
            {
                TileEntity tileEntity = Vector3.getUEUnitFromSide(this.worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), ForgeDirection.getOrientation(this.getBlockMetadata()));

                if (tileEntity != null)
                {
                    if (tileEntity instanceof TileEntityConductor)
                    {
                        float electricityNeeded = ElectricityManager.instance.electricityRequired(((TileEntityConductor)tileEntity).connectionID);
                        float transferAmps = Math.min(this.getVoltage(), Math.min(this.wattHourStored, electricityNeeded));
                        ElectricityManager.instance.produceElectricity((TileEntityConductor)tileEntity, transferAmps, this.getVoltage());
                        this.wattHourStored -= transferAmps;
                    }
                }
            }
        }
        
        if(this.isGUIOpen)
        {
        	PacketManager.sendTileEntityPacketWithRange(this, "BasicComponents", 15, (int)1, this.wattHourStored, this.disabledTicks);
        }
    }
    
    @Override
	public void handlePacketData(NetworkManager network, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
        {
			int ID = dataStream.readInt();
			
			if(ID == -1)
			{
				this.isGUIOpen = dataStream.readBoolean();
			}
			else if(ID == 1)
			{
	            this.wattHourStored = dataStream.readFloat();
	            this.disabledTicks = dataStream.readInt();
			}
			
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.wattHourStored = par1NBTTagCompound.getFloat("electricityStored");
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
        par1NBTTagCompound.setFloat("electricityStored", this.wattHourStored);
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
    public int getStartInventorySide(ForgeDirection side)
    {
        if(side == side.DOWN)
        {
            return 1;
        }

        if(side == side.UP)
        {
            return 0;
        }

        return 2;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        return 1;
    }
    
    @Override
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }
    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }
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
    public String getInvName()
    {
        return "Battery Box";
    }
    @Override
    public int getInventoryStackLimit()
    {
        return 1;
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
    public boolean isPoweringTo(byte side)
    {
        return isFull;
    }

    @Override
    public boolean isIndirectlyPoweringTo(byte side)
    {
        return isPoweringTo(side);
    }
    
    @Override
    public float getWattHours()
    {
    	return this.wattHourStored;
    }

	@Override
	public void setWattHours(float wattHours)
	{
		this.wattHourStored = Math.max(Math.min(wattHours, this.getMaxWattHours()), 0);
	}
	
	@Override
	public float getMaxWattHours()
	{
		return 100000;
	}
	
	@Override
    public int getTickInterval()
    {
    	if(!this.worldObj.isRemote)
    	{
            return 1;
    	}
    	
        return 0;
    }
}
