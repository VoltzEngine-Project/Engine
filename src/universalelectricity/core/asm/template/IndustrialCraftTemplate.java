package universalelectricity.core.asm.template;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.IEnergyInterfacer;
import universalelectricity.compatibility.Compatibility;
import universalelectricity.core.electricity.ElectricityPack;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;

/**
 * An ASM template used to transform other @UniversalClass classes to have specific compatibility.
 * 
 * @author Calclavia
 * 
 */
public abstract class IndustrialCraftTemplate extends TileEntity implements IEnergySink, IEnergySource, IEnergyInterfacer
{
	protected boolean isAddedToEnergyNet;

	/**
	 * IC2 Methods
	 */
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
	{
		return this.canConnect(direction);
	}

	@Override
	public double getOfferedEnergy()
	{
		return this.getProvide(ForgeDirection.UNKNOWN) * Compatibility.TO_IC2_RATIO;
	}

	@Override
	public void drawEnergy(double amount)
	{
		this.onExtractEnergy(ForgeDirection.UNKNOWN, (int) (amount * Compatibility.IC2_RATIO), true);
	}

	@Override
	public void validate()
	{
		this.initIC();
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

	protected void initIC()
	{
		if (!this.isAddedToEnergyNet)
		{
			if (Compatibility.isIndustrialCraft2Loaded())
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			}

			this.isAddedToEnergyNet = true;
		}
	}

	private void unloadTileIC2()
	{
		if (this.isAddedToEnergyNet && this.worldObj != null)
		{
			if (Compatibility.isIndustrialCraft2Loaded())
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}

			this.isAddedToEnergyNet = false;
		}
	}

	@Override
	public double demandedEnergyUnits()
	{
		return this.getRequest(ForgeDirection.UNKNOWN) * Compatibility.TO_IC2_RATIO;
	}

	@Override
	public double injectEnergyUnits(ForgeDirection direction, double amount)
	{
		int toSend = (int) (amount * Compatibility.IC2_RATIO);

		if (this.onReceiveEnergy(direction, toSend, false) > 0)
		{
			float receive = this.onReceiveEnergy(direction, toSend, true);

			/*
			 * Return the difference, since injectEnergy returns left over energy, and
			 * receiveElectricity returns energy used.
			 */
			return Math.round(amount - (receive * Compatibility.TO_IC2_RATIO));
		}

		return amount;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
	{
		return receiver instanceof IEnergyTile && this.onExtractEnergy(direction, 1, false) > 0;
	}

	@Override
	public int getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}
}
