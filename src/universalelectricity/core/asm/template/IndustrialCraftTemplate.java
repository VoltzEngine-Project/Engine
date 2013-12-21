package universalelectricity.core.asm.template;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
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
		return StaticForwarder.canConnect(this, direction);
	}

	@Override
	public double getOfferedEnergy()
	{
		return StaticForwarder.onExtractEnergy(this, ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false) * CompatibilityType.INDUSTRIALCRAFT.ratio;
	}

	@Override
	public void drawEnergy(double amount)
	{
		StaticForwarder.onExtractEnergy(this, ForgeDirection.UNKNOWN, (int) (amount * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio), true);
	}

	@Override
	public void validate()
	{
		StaticForwarder.validateTile(this);
		StaticForwarder.loadIC(this);
	}

	@Override
	public void invalidate()
	{
		StaticForwarder.unloadIC(this);
		StaticForwarder.invalidateTile(this);
	}

	@Override
	public void onChunkUnload()
	{
		StaticForwarder.unloadIC(this);
	}

	@Override
	public double demandedEnergyUnits()
	{
		return StaticForwarder.onReceiveEnergy(this, ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false) * CompatibilityType.INDUSTRIALCRAFT.ratio;
	}

	@Override
	public double injectEnergyUnits(ForgeDirection direction, double amount)
	{
		int toSend = (int) (amount * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);

		if (StaticForwarder.onReceiveEnergy(this, direction, toSend, false) > 0)
		{
			long receive = StaticForwarder.onReceiveEnergy(this, direction, toSend, true);

			/*
			 * Return the difference, since injectEnergy returns left over energy, and
			 * receiveElectricity returns energy used.
			 */
			return Math.round(amount - (receive * CompatibilityType.INDUSTRIALCRAFT.ratio));
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
