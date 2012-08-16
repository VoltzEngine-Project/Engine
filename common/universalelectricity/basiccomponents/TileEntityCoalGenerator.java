package universalelectricity.basiccomponents;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
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
import universalelectricity.extend.TileEntityConductor;
import universalelectricity.network.IPacketReceiver;
import universalelectricity.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityCoalGenerator extends TileEntityElectricUnit implements IInventory, ISidedInventory, IPacketReceiver
{
    //Maximum possible generation rate of watts in SECONDS
    public static final int maxGenerateRate = 550;

    //Current generation rate based on hull heat. In TICKS.
    public float generateWatts = 0;

    public TileEntityConductor connectedElectricUnit = null;
    /**
    * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
    */
    public int itemCookTime = 0;
    /**
    * The ItemStacks that hold the items currently being used in the battery box
    */
    private ItemStack[] containingItems = new ItemStack[1];

    public TileEntityCoalGenerator()
    {
        ElectricityManager.registerElectricUnit(this);
    }

    public boolean canProduceElectricity(ForgeDirection side)
    {
        return canConnect(side) && !this.isDisabled();
    }

    @Override
    public boolean canReceiveFromSide(ForgeDirection side)
    {
        return false;
    }

    @Override
    public boolean canConnect(ForgeDirection side)
    {
        return side == ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    }

    @Override
    public void onUpdate(float watts, float voltage, ForgeDirection side)
    {
        if (!this.worldObj.isRemote)
        {
            super.onUpdate(watts, voltage, side);
            //Check nearby blocks and see if the conductor is full. If so, then it is connected
            TileEntity tileEntity = Vector3.getUEUnitFromSide(this.worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite());

            if (tileEntity instanceof TileEntityConductor)
            {
                if (ElectricityManager.electricityRequired(((TileEntityConductor)tileEntity).connectionID) > 0)
                {
                    this.connectedElectricUnit = (TileEntityConductor)tileEntity;
                }
                else
                {
                    this.connectedElectricUnit = null;
                }
            }
            else
            {
                this.connectedElectricUnit = null;
            }

            if (!this.isDisabled())
            {
                //Coal Geneator
                if (this.containingItems[0] != null && this.connectedElectricUnit != null)
                {
                    if (this.containingItems[0].getItem().shiftedIndex == Item.coal.shiftedIndex)
                    {
                        if (this.itemCookTime <= 0)
                        {
                            itemCookTime = Math.max(500 - (int)(this.generateWatts * 20), 200);
                            this.decrStackSize(0, 1);
                        }
                    }
                }

                //Starts generating electricity if the device is heated up
                if (this.itemCookTime > 0)
                {
                    this.itemCookTime --;

                    if (this.connectedElectricUnit != null)
                    {
                        this.generateWatts = (float)Math.min(this.generateWatts + Math.min((this.generateWatts) * 0.0005 + 0.0009, 0.04F), this.maxGenerateRate / 20);
                    }
                }

                if (this.connectedElectricUnit == null || this.itemCookTime <= 0)
                {
                    this.generateWatts = (float)Math.max(this.generateWatts - 0.05, 0);
                }

                if (this.generateWatts > 1)
                {
                    ElectricityManager.produceElectricity(this.connectedElectricUnit, this.generateWatts * this.getTickInterval(), this.getVoltage());
                }
            }
            
            PacketManager.sendTileEntityPacket(this, "BasicComponents", this.generateWatts, this.disabledTicks);
        }
    }
    
    @Override
	public void handlePacketData(NetworkManager network, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
        {
            this.generateWatts = dataStream.readFloat();
            this.disabledTicks = dataStream.readInt();
        }
        catch (Exception e)
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
        this.itemCookTime = par1NBTTagCompound.getInteger("itemCookTime");
        this.generateWatts = par1NBTTagCompound.getFloat("generateRate");
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
        par1NBTTagCompound.setFloat("generateRate", (int)this.generateWatts);
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
        return 0;
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
        return "Coal Generator";
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
    public float getVoltage()
    {
        return 120;
    }

    @Override
    public int getTickInterval()
    {
        return 1;
    }

    @Override
    public float electricityRequest()
    {
        return 0;
    }
}
