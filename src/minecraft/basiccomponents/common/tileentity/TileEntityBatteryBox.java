package basiccomponents.common.tileentity;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import basiccomponents.common.BCLoader;
import basiccomponents.common.BasicComponents;
import basiccomponents.common.block.BlockBasicMachine;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileEntityBatteryBox extends TileEntityElectricityReceiver implements IJouleStorage, IPacketReceiver, IRedstoneProvider, IInventory, ISidedInventory, IPeripheral
{
	private double joules = 0;

	private ItemStack[] containingItems = new ItemStack[2];

	private boolean isFull = false;

	private int playersUsing = 0;

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - BlockBasicMachine.BATTERY_BOX_METADATA + 2), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockBasicMachine.BATTERY_BOX_METADATA + 2).getOpposite()));
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, BasicComponents.blockMachine.blockID);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.isDisabled())
		{
			if (!this.worldObj.isRemote)
			{
				ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockBasicMachine.BATTERY_BOX_METADATA + 2).getOpposite();
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

				if (inputTile != null)
				{
					if (inputTile instanceof IConductor)
					{
						if (this.joules >= this.getMaxJoules())
						{
							((IConductor) inputTile).getNetwork().stopRequesting(this);
						}
						else
						{
							((IConductor) inputTile).getNetwork().startRequesting(this, (this.getMaxJoules() - this.getJoules()) / this.getVoltage(), this.getVoltage());
							this.setJoules(this.joules + ((IConductor) inputTile).getNetwork().consumeElectricity(this).getWatts());
						}
					}
				}
			}

			/*
			 * The top slot is for recharging items. Check if the item is a electric item. If so,
			 * recharge it.
			 */
			if (this.containingItems[0] != null && this.joules > 0)
			{
				if (this.containingItems[0].getItem() instanceof IItemElectric)
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[0].getItem();
					double ampsToGive = Math.min(ElectricInfo.getAmps(electricItem.getMaxJoules(this.containingItems[0]) * 0.005, this.getVoltage()), this.joules);
					double joules = electricItem.onReceive(ampsToGive, this.getVoltage(), this.containingItems[0]);
					this.setJoules(this.joules - (ElectricInfo.getJoules(ampsToGive, this.getVoltage(), 1) - joules));
				}
			}

			// Power redstone if the battery box is full
			boolean isFullThisCheck = false;

			if (this.joules >= this.getMaxJoules())
			{
				isFullThisCheck = true;
			}

			if (this.isFull != isFullThisCheck)
			{
				this.isFull = isFullThisCheck;
				this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, BasicComponents.blockMachine.blockID);
			}

			/**
			 * Output Electricity
			 */

			/**
			 * The bottom slot is for decharging. Check if the item is a electric item. If so,
			 * decharge it.
			 */
			if (this.containingItems[1] != null && this.joules < this.getMaxJoules())
			{
				if (this.containingItems[1].getItem() instanceof IItemElectric)
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[1].getItem();

					if (electricItem.canProduceElectricity())
					{
						double joulesReceived = electricItem.onUse(electricItem.getMaxJoules(this.containingItems[1]) * 0.005, this.containingItems[1]);
						this.setJoules(this.joules + joulesReceived);
					}
				}
			}

			if (!this.worldObj.isRemote)
			{
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockBasicMachine.BATTERY_BOX_METADATA + 2);
				TileEntity tileEntity = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

				if (tileEntity != null)
				{
					TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, new Vector3(this), outputDirection);

					// Output UE electricity
					if (connector instanceof IConductor)
					{
						double outputWatts = Math.min(((IConductor) connector).getNetwork().getRequest().getWatts(), Math.min(this.getJoules(), 50000));

						if (this.getJoules() > 0 && outputWatts > 0)
						{
							((IConductor) connector).getNetwork().startProducing(this, outputWatts / this.getVoltage(), this.getVoltage());
							this.setJoules(this.joules - outputWatts);
						}
						else
						{
							((IConductor) connector).getNetwork().stopProducing(this);
						}

					}
				}
			}
		}

		// Energy Loss
		this.setJoules(this.joules - 0.0005);

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(BCLoader.CHANNEL, this, this.joules, this.disabledTicks);
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.joules = dataStream.readDouble();
			this.disabledTicks = dataStream.readInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void openChest()
	{
		this.playersUsing++;
	}

	@Override
	public void closeChest()
	{
		this.playersUsing--;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.joules = par1NBTTagCompound.getDouble("electricityStored");

		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
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
		par1NBTTagCompound.setDouble("electricityStored", this.joules);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3)
		{
			if (this.containingItems[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

	@Override
	public int getStartInventorySide(ForgeDirection side)
	{
		if (side == side.DOWN) { return 1; }

		if (side == side.UP) { return 1; }

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
		return LanguageRegistry.instance().getStringLocalization("tile.bcMachine.1.name");
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
	public boolean isPoweringTo(ForgeDirection side)
	{
		return this.isFull;
	}

	@Override
	public boolean isIndirectlyPoweringTo(ForgeDirection side)
	{
		return isPoweringTo(side);
	}

	@Override
	public double getJoules(Object... data)
	{
		return this.joules;
	}

	@Override
	public void setJoules(double joules, Object... data)
	{
		this.joules = Math.max(Math.min(joules, this.getMaxJoules()), 0);
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		return 4000000;
	}

	/**
	 * COMPUTERCRAFT FUNCTIONS
	 */

	@Override
	public String getType()
	{
		return "BatteryBox";
	}

	@Override
	public String[] getMethodNames()
	{
		return new String[] { "getVoltage", "getJoules", "isFull" };
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
	{
		switch (method)
		{
			case 0:
				return new Object[] { this.getVoltage() };
			case 1:
				return new Object[] { this.getJoules() };
			case 2:
				return new Object[] { this.isFull };
			default:
				throw new Exception("Function unimplemented");
		}
	}

	@Override
	public boolean canAttachToSide(int side)
	{
		return true;
	}

	@Override
	public void attach(IComputerAccess computer, String computerSide)
	{
	}

	@Override
	public void detach(IComputerAccess computer)
	{
	}

}
