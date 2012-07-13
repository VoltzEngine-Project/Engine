package net.minecraft.src.basiccomponents;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.forge.ISidedInventory;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.Vector3;
import net.minecraft.src.universalelectricity.electricity.ElectricityManager;
import net.minecraft.src.universalelectricity.electricity.TileEntityElectricUnit;
import net.minecraft.src.universalelectricity.extend.IRedstoneProvider;
import net.minecraft.src.universalelectricity.extend.ISlotInput;
import net.minecraft.src.universalelectricity.extend.ISlotOuput;
import net.minecraft.src.universalelectricity.extend.ItemElectric;
import net.minecraft.src.universalelectricity.extend.TileEntityConductor;
import net.minecraft.src.universalelectricity.network.IPacketReceiver;

public class TileEntityBatteryBox extends TileEntityElectricUnit implements ISlotInput, ISlotOuput, IPacketReceiver, IRedstoneProvider, ITextureProvider, IInventory, ISidedInventory
{
    public float electricityStored = 0;

    /**
    * The ItemStacks that hold the items currently being used in the battery box
    */
    private ItemStack[] containingItems = new ItemStack[2];

    private boolean isFull = false;

    public TileEntityBatteryBox()
    {
        BasicComponents.packetManager.registerPacketUser(this);
        ElectricityManager.registerElectricUnit(this);
    }

    @Override
    public float electricityRequest()
    {
        if (!this.isDisabled())
        {
            return this.getElectricityCapacity() - this.electricityStored;
        }

        return 0;
    }

    @Override
    public boolean canReceiveFromSide(byte side)
    {
        return side == this.getBlockMetadata();
    }

    @Override
    public boolean canConnect(byte side)
    {
        return canReceiveFromSide(side) || side == UniversalElectricity.getOrientationFromSide((byte)this.getBlockMetadata(), (byte)2);
    }

    @Override
    public void onUpdate(float watts, float voltage, byte side)
    {
        if (!this.worldObj.isRemote)
        {
            super.onUpdate(watts, voltage, side);

            if (!this.isDisabled())
            {
                if (electricityRequest() > 0 && canConnect(side))
                {
                    float rejectedElectricity = (float) Math.max((this.electricityStored + watts) - this.getElectricityCapacity(), 0.0);
                    this.electricityStored = (float) Math.max(this.electricityStored + watts - rejectedElectricity, 0.0);
                }

                //The top slot is for recharging items. Check if the item is a electric item. If so, recharge it.
                if (this.containingItems[0] != null && this.electricityStored > 0)
                {
                    if (this.containingItems[0].getItem() instanceof ItemElectric)
                    {
                        ItemElectric electricItem = (ItemElectric)this.containingItems[0].getItem();
                        float rejectedElectricity = electricItem.onReceiveElectricity(electricItem.getTransferRate(), this.containingItems[0]);
                        this.electricityStored -= electricItem.getTransferRate() - rejectedElectricity;
                    }
                }

                //The bottom slot is for decharging. Check if the item is a electric item. If so, decharge it.
                if (this.containingItems[1] != null && this.electricityStored < this.getElectricityCapacity())
                {
                    if (this.containingItems[1].getItem() instanceof ItemElectric)
                    {
                        ItemElectric electricItem = (ItemElectric)this.containingItems[1].getItem();

                        if (electricItem.canProduceElectricity())
                        {
                            float receivedElectricity = electricItem.onUseElectricity(electricItem.getTransferRate(), this.containingItems[1]);
                            this.electricityStored = Math.max(this.electricityStored + receivedElectricity, 0);
                        }
                    }
                }

                boolean isFullThisCheck = false;

                if (this.electricityStored >= this.getElectricityCapacity())
                {
                    isFullThisCheck = true;
                }

                if (this.isFull != isFullThisCheck)
                {
                    this.isFull = isFullThisCheck;
                    this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
                }

                if (this.electricityStored > 0)
                {
                    TileEntity tileEntity = UniversalElectricity.getUEUnitFromSide(this.worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), UniversalElectricity.getOrientationFromSide((byte)this.getBlockMetadata(), (byte)2));

                    if (tileEntity != null)
                    {
                        if (tileEntity instanceof TileEntityConductor)
                        {
                            float electricityNeeded = ElectricityManager.electricityRequired(((TileEntityConductor)tileEntity).connectionID);
                            float transferElectricity = Math.min(100, Math.min(this.electricityStored, electricityNeeded));
                            ElectricityManager.produceElectricity((TileEntityConductor)tileEntity, transferElectricity, this.getVoltage());
                            this.electricityStored -= transferElectricity;
                        }
                    }
                }
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
        this.electricityStored = par1NBTTagCompound.getFloat("electricityStored");
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
        par1NBTTagCompound.setFloat("electricityStored", this.electricityStored);
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

    public float getElectricityCapacity()
    {
        return 100000;
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
    public int getSizeInventorySide(int side)
    {
        return getSizeInventory();
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
    public String getTextureFile()
    {
        return BasicComponents.blockTextureFile;
    }

    @Override
    public void onPacketData(NetworkManager network, String channel, DataInputStream dataStream)
    {
        try
        {
            this.electricityStored = (float)dataStream.readDouble();
            this.disabledTicks = (int)dataStream.readDouble();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getPacketID()
    {
        return 1;
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
    public int getTickInterval()
    {
        return 1;
    }

    @Override
    public int[] getSlotOutputs(byte side)
    {
        return new int[] {0, 1};
    }

    @Override
    public int[] getSlotInputs(ItemStack item, byte side)
    {
        if (item.getItem() instanceof ItemElectric)
        {
        	if(((ItemElectric)item.getItem()).canProduceElectricity())
        	{
        		return new int[] {1};
        	}
        	else
        	{
        		return new int[] {0};
        	}
        }

        return null;
    }
}
