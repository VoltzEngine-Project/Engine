package universalelectricity.core.asm.template;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.Compatibility.CompatibilityType;
import universalelectricity.api.energy.IEnergyInterface;

/**
 * An ASM template used to transform other @UniversalClass classes to have specific compatibility.
 * 
 * @author Calclavia
 * 
 */
public abstract class IndustrialCraftTemplate extends TileEntity implements IEnergySink, IEnergySource, IEnergyInterface
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
		return StaticForwarder.onExtractEnergy(this, ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false) * CompatibilityType.INDUSTRIALCRAFT.ratio;
	}

	@Override
	public void drawEnergy(double amount)
	{
		this.onExtractEnergy(ForgeDirection.UNKNOWN, (int) (amount * CompatibilityType.INDUSTRIALCRAFT.ratio), true);
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
			if (CompatibilityType.INDUSTRIALCRAFT.isLoaded())
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
			if (CompatibilityType.INDUSTRIALCRAFT.isLoaded())
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}

			this.isAddedToEnergyNet = false;
		}
	}

	@Override
	public double demandedEnergyUnits()
	{
		return StaticForwarder.onReceiveEnergy(this, ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false) * CompatibilityType.INDUSTRIALCRAFT.ratio;
	}

	@Override
	public double injectEnergyUnits(ForgeDirection direction, double amount)
	{
		int toSend = (int) (amount * CompatibilityType.INDUSTRIALCRAFT.ratio);

		if (this.onReceiveEnergy(direction, toSend, false) > 0)
		{
			int receive = StaticForwarder.onReceiveEnergy(this, direction, toSend, true);

			/*
			 * Return the difference, since injectEnergy returns left over energy, and
			 * receiveElectricity returns energy used.
			 */
			return Math.round(amount - (receive * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio));
		}

		return amount;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
	{
		return receiver instanceof IEnergyTile && StaticForwarder.onExtractEnergy(this, direction, 1, false) > 0;
	}

	@Override
	public int getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}
}
