package universalelectricity.basiccomponents;

import ic2.api.Direction;
import ic2.api.IEnergyAcceptor;
import ic2.api.IEnergyEmitter;
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
import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.electricity.TileEntityMachine;
import universalelectricity.implement.IElectricityStorage;
import universalelectricity.implement.IItemElectric;
import universalelectricity.implement.IRedstoneProvider;
import universalelectricity.network.IPacketReceiver;
import universalelectricity.network.PacketManager;
import universalelectricity.prefab.TileEntityConductor;
import universalelectricity.prefab.Vector3;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.power.PowerProvider;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityBatteryBox extends TileEntityMachine implements IEnergyEmitter, IEnergyAcceptor, IPowerReceptor, IElectricityStorage, IPacketReceiver, IRedstoneProvider, IInventory, ISidedInventory
{	
	private double wattHourStored = 0;

    private ItemStack[] containingItems = new ItemStack[2];

    private boolean isFull = false;
    
	private int playersUsing = 0;
	
	public PowerProvider powerProvider;

    public TileEntityBatteryBox()
    {
    	super();
    }
    
    @Override
	public void setPowerProvider(IPowerProvider provider)
    {
    	if(PowerFramework.currentFramework != null)
    	{
    		powerProvider = (PowerProvider) PowerFramework.currentFramework.createPowerProvider();
    		powerProvider.configure(20, 25, 25, 25, (int)ElectricInfo.getWatts(getMaxWattHours()));
    	}
	}

	@Override
	public IPowerProvider getPowerProvider()
	{
		return powerProvider;
	}

	@Override
	public void doWork() { }

	@Override
	public int powerRequest()
	{
		return powerProvider.getMaxEnergyReceived();
	}
	
	@Override
	public boolean isAddedToEnergyNet()
	{
		return false;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		return this.canReceiveFromSide(direction.toForgeDirection());
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
	{
		return false;
	}

    @Override
    public double wattRequest()
    {
        if (!this.isDisabled())
        {
            return ElectricInfo.getWatts(this.getMaxWattHours()) - ElectricInfo.getWatts(this.wattHourStored);
        }

        return 0;
    }

    @Override
    public boolean canReceiveFromSide(ForgeDirection side)
    {
        return side == ForgeDirection.getOrientation(this.getBlockMetadata() - BlockBasicMachine.BATTERY_BOX_METADATA + 2).getOpposite();
    }
    
    

    @Override
    public boolean canConnect(ForgeDirection side)
    {
        return canReceiveFromSide(side) || side == ForgeDirection.getOrientation(this.getBlockMetadata() - BlockBasicMachine.BATTERY_BOX_METADATA + 2);
    }

    @Override
    public void onReceive(TileEntity sender, double amps, double voltage, ForgeDirection side)
    {
        super.onReceive(sender, amps, voltage, side);
        
        if (voltage > this.getVoltage())
        {
            this.worldObj.createExplosion((Entity)null, this.xCoord, this.yCoord, this.zCoord, 1F);
            return;
        }
        
        if(!this.isDisabled())
        {
        	this.setWattHours(this.wattHourStored+ElectricInfo.getWattHours(amps, voltage));
        	
        	if(this.powerProvider != null)
        	{
        		this.setWattHours(this.wattHourStored+this.powerProvider.useEnergy(25, 25, true));
        	}
        }
    }
    
    @Override
    public void updateEntity() 
    {
        if(!this.isDisabled())
        {            
            //The top slot is for recharging items. Check if the item is a electric item. If so, recharge it.
            if (this.containingItems[0] != null && this.wattHourStored > 0)
            {
                if (this.containingItems[0].getItem() instanceof IItemElectric)
                {
                    IItemElectric electricItem = (IItemElectric)this.containingItems[0].getItem();
                    double rejectedElectricity = electricItem.onReceiveElectricity(electricItem.getTransferRate(), this.containingItems[0]);
                    this.setWattHours(this.wattHourStored - (electricItem.getTransferRate() - rejectedElectricity));
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
                        double wattHourReceived = electricItem.onUseElectricity(electricItem.getTransferRate(), this.containingItems[1]);
                        this.setWattHours(this.wattHourStored + wattHourReceived);
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
                TileEntity tileEntity = Vector3.getUEUnitFromSide(this.worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockBasicMachine.BATTERY_BOX_METADATA + 2));

                if (tileEntity != null)
                {
                    if (tileEntity instanceof TileEntityConductor)
                    {
                        double wattsNeeded = ElectricityManager.instance.getElectricityRequired(((TileEntityConductor)tileEntity).connectionID);
                        double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(wattsNeeded, this.getVoltage()), ElectricInfo.getAmpsFromWattHours(this.wattHourStored, this.getVoltage()) ), 10000), 0);                        
                        ElectricityManager.instance.produceElectricity(this, (TileEntityConductor)tileEntity, transferAmps, this.getVoltage());
                        this.setWattHours(this.wattHourStored - ElectricInfo.getWattHours(transferAmps, this.getVoltage()));
                    }
                }
            }
        }
        
        if(!this.worldObj.isRemote)
        {
	        if(ElectricityManager.inGameTicks % 40 == 0 && this.playersUsing > 0)
	        {
	        	PacketManager.sendTileEntityPacketWithRange(this, "BasicComponents", 15, this.wattHourStored, this.disabledTicks);
	        }
        }
    }
    
    @Override
	public void handlePacketData(NetworkManager network, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
        {
			this.wattHourStored = dataStream.readDouble();
	        this.disabledTicks = dataStream.readInt();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}
    
    @Override
    public void openChest()
    {
    	if(!this.worldObj.isRemote)
        {
    		PacketManager.sendTileEntityPacketWithRange(this, "BasicComponents", 15, this.wattHourStored, this.disabledTicks);
        }
    	
    	this.playersUsing  ++;
    }
    
    @Override
    public void closeChest()
    {
    	this.playersUsing --;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.wattHourStored = par1NBTTagCompound.getDouble("electricityStored");
        
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
        par1NBTTagCompound.setDouble("electricityStored", this.wattHourStored);
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
    public double getWattHours(Object... data)
    {
    	return this.wattHourStored;
    }

	@Override
	public void setWattHours(double wattHours, Object... data)
	{
		this.wattHourStored = Math.max(Math.min(wattHours, this.getMaxWattHours()), 0);
	}
	
	@Override
	public double getMaxWattHours()
	{
		return 1000;
	}
}
