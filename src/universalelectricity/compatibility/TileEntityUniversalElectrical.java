package universalelectricity.compatibility;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.electricity.ElectricalEventHandler;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityElectrical;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

/**
 * A universal electricity tile used for tiles that consume or produce electricity.
 * 
 * Extend this class or use as a reference for your own implementation of compatible electrical
 * tiles.
 * 
 * @author micdoodle8
 * 
 */
public abstract class TileEntityUniversalElectrical extends TileEntityElectrical implements IEnergySink, IEnergySource, IPowerReceptor
{
	protected boolean addedToEnergyNet;
	public PowerHandler bcPowerHandler;

	public float energyStored = 0;
	public float maxEnergyStored = 0;

	public TileEntityUniversalElectrical(float maxEnergy)
	{
		this(0, maxEnergy);
	}

	public TileEntityUniversalElectrical(float initialEnergy, float maxEnergy)
	{
		this.energyStored = initialEnergy;
		this.maxEnergyStored = maxEnergy;
		this.bcPowerHandler = new PowerHandler(this, Type.MACHINE);
		this.bcPowerHandler.configure(0, 100, 0, (int) Math.ceil(maxEnergy * Compatibility.BC3_RATIO));
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote && !this.addedToEnergyNet)
		{
			if (Compatibility.isIndustrialCraft2Loaded())
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			}

			this.addedToEnergyNet = true;
		}

		float provide = this.getProvide(this.getOutputDirection());

		if (!this.worldObj.isRemote && provide > 0)
		{
			TileEntity outputTile = VectorHelper.getConnectorFromSide(this.worldObj, new Vector3(this), this.getOutputDirection());
			IElectricityNetwork outputNetwork = ElectricalEventHandler.getNetworkFromTileEntity(outputTile, this.getOutputDirection());

			if (outputNetwork != null)
			{
				ElectricityPack powerRequest = outputNetwork.getRequest(this);

				if (powerRequest.getWatts() > 0)
				{
					ElectricityPack sendPack = ElectricityPack.min(ElectricityPack.getFromWatts(this.getEnergyStored(), this.getVoltage()), ElectricityPack.getFromWatts(provide, this.getVoltage()));
					float rejectedPower = outputNetwork.produce(sendPack, this);
					this.setEnergyStored(this.getEnergyStored() - (sendPack.getWatts() - rejectedPower));
				}
			}
			else if (Compatibility.isIndustrialCraft2Loaded())
			{
				int ic2Provide = (int) Math.floor(provide * Compatibility.TO_IC2_RATIO);

				if (this.getEnergyStored() >= provide)
				{
					EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, ic2Provide);
					MinecraftForge.EVENT_BUS.post(event);

					this.setEnergyStored(this.getEnergyStored() - ((ic2Provide * Compatibility.IC2_RATIO) - (event.amount * Compatibility.IC2_RATIO)));
				}
			}
		}
	}

	@Override
	public float getRequest(ForgeDirection direction)
	{
		return 0;
	}

	@Override
	public float getProvide(ForgeDirection direction)
	{
		return 0;
	}

	/**
	 * The electrical input direction.
	 * 
	 * @return The direction that electricity is entered into the tile. Return null for no input.
	 */
	public abstract ForgeDirection getInputDirection();

	/**
	 * The electrical output direction.
	 * 
	 * @return The direction that electricity is output from the tile. Return null for no output.
	 */
	public abstract ForgeDirection getOutputDirection();

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		if (direction == null || direction.equals(ForgeDirection.UNKNOWN))
		{
			return false;
		}

		return direction.equals(this.getInputDirection()) || direction.equals(this.getOutputDirection());
	}

	/**
	 * IC2 Methods
	 */
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		return this.canConnect(direction.toForgeDirection());
	}

	@Override
	public boolean isAddedToEnergyNet()
	{
		return this.addedToEnergyNet;
	}

	@Override
	public void invalidate()
	{
		this.unloadTileIC2();
		super.invalidate();
	}

	@Override
	public void onChunkUnload()
	{
		this.unloadTileIC2();
		super.onChunkUnload();
	}

	private void unloadTileIC2()
	{
		if (this.addedToEnergyNet && this.worldObj != null)
		{
			if (Compatibility.isIndustrialCraft2Loaded())
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}

			this.addedToEnergyNet = false;
		}
	}

	@Override
	public int demandsEnergy()
	{
		return (int) Math.floor(this.getRequest(this.getInputDirection()) * Compatibility.TO_IC2_RATIO);
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount)
	{
		if (!directionFrom.toForgeDirection().equals(this.getInputDirection()))
		{
			return amount;
		}

		float convertedEnergy = amount * Compatibility.IC2_RATIO;

		ElectricityPack toSend = ElectricityPack.getFromWatts(convertedEnergy, this.getVoltage());

		int receive = (int) Math.floor(this.receiveElectricity(directionFrom.toForgeDirection(), toSend, true));

		// Return the difference, since injectEnergy returns leftover energy, and receiveElectricity
		// returns energy used.
		return (int) Math.floor(amount - receive * Compatibility.TO_IC2_RATIO);
	}

	@Override
	public int getMaxEnergyOutput()
	{
		return (int) Math.floor(this.getProvide(this.getOutputDirection()));
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
	{
		return receiver instanceof IEnergyTile && direction.toForgeDirection().equals(this.getOutputDirection());
	}

	@Override
	public int getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive)
	{
		if (!from.equals(this.getInputDirection()))
		{
			return 0.0F;
		}

		if (receive != null)
		{
			float prevEnergyStored = this.getEnergyStored();
			float newStoredEnergy = Math.min(this.getEnergyStored() + receive.getWatts(), this.getMaxEnergyStored());

			if (doReceive)
			{
				this.setEnergyStored(newStoredEnergy);
			}

			return Math.max(newStoredEnergy - prevEnergyStored, 0);
		}

		return 0.0F;
	}

	@Override
	public ElectricityPack provideElectricity(ForgeDirection from, ElectricityPack request, boolean doProvide)
	{
		if (!from.equals(this.getOutputDirection()))
		{
			return new ElectricityPack();
		}

		if (request != null)
		{
			float requestedEnergy = Math.min(request.getWatts(), this.energyStored);

			if (doProvide)
			{
				this.setEnergyStored(this.energyStored - requestedEnergy);
			}

			return ElectricityPack.getFromWatts(requestedEnergy, this.getVoltage());
		}

		return new ElectricityPack();
	}

	@Override
	public float getVoltage()
	{
		return 120;
	}

	@Override
	public void setEnergyStored(float energy)
	{
		this.energyStored = Math.max(0, Math.min(this.getMaxEnergyStored(), energy));
	}

	@Override
	public float getEnergyStored()
	{
		return this.energyStored;
	}

	@Override
	public float getMaxEnergyStored()
	{
		return this.maxEnergyStored;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.energyStored = nbt.getFloat("energyStored");
		this.maxEnergyStored = nbt.getFloat("maxEnergyStored");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("energyStored", this.energyStored);
		nbt.setFloat("maxEnergyStored", this.maxEnergyStored);
	}

	/*
	 * public class PowerHandlerCompat extends PowerHandler { public IElectrical theTile;
	 * 
	 * public PowerHandlerCompat(IPowerReceptor receptor, Type type) { super(receptor, type);
	 * 
	 * this.theTile = (IElectrical) receptor; }
	 * 
	 * @Override public float useEnergy(float min, float max, boolean doUse) { float result = 0;
	 * 
	 * if (this.theTile.getEnergyStored() * Compatibility.TO_BC_RATIO >= min) { if
	 * (this.theTile.getEnergyStored() * Compatibility.TO_BC_RATIO <= max) { result =
	 * this.theTile.getEnergyStored() * Compatibility.TO_BC_RATIO;
	 * 
	 * if (doUse) { this.theTile.setEnergyStored(0.0F); } } else { result = max;
	 * 
	 * if (doUse) { this.theTile.setEnergyStored(this.theTile.getEnergyStored() - max *
	 * Compatibility.BC3_RATIO); } } }
	 * 
	 * return result; }
	 * 
	 * @Override public void receiveEnergy(float quantity, ForgeDirection from) {
	 * this.theTile.receiveElectricity(from, quantity * Compatibility.BC3_RATIO, true); }
	 * 
	 * @Override public boolean update(IPowerReceptor receptor) { return true; } }
	 */

	/**
	 * BuildCraft power support
	 */
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return null;
	}

	@Override
	public void doWork(PowerHandler workProvider)
	{

	}

	@Override
	public World getWorld()
	{
		return this.getWorldObj();
	}
}
