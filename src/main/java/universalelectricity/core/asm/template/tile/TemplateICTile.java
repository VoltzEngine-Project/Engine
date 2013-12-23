package universalelectricity.core.asm.template.tile;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.CompatibilityType;
import universalelectricity.api.energy.IEnergyInterface;

/**
 * An ASM template used to transform other @UniversalClass classes to have specific compatibility.
 * 
 * @author Calclavia
 * 
 */
public abstract class TemplateICTile extends TileEntity implements IEnergySink, IEnergySource, IEnergyInterface
{
	/**
	 * IC2 Methods
	 */
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
	{
		return StaticTileForwarder.canConnect(this, direction);
	}

	@Override
	public double getOfferedEnergy()
	{
		return StaticTileForwarder.onExtractEnergy(this, ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false) * CompatibilityType.INDUSTRIALCRAFT.ratio;
	}

	@Override
	public void drawEnergy(double amount)
	{
		StaticTileForwarder.onExtractEnergy(this, ForgeDirection.UNKNOWN, (int) (amount * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio), true);
	}

	@Override
	public void validate()
	{
		StaticTileForwarder.validateTile(this);
		StaticTileForwarder.loadIC(this);
	}

	@Override
	public void invalidate()
	{
		StaticTileForwarder.unloadIC(this);
		StaticTileForwarder.invalidateTile(this);
	}

	@Override
	public void onChunkUnload()
	{
		StaticTileForwarder.unloadIC(this);
	}

	@Override
	public double demandedEnergyUnits()
	{
		return StaticTileForwarder.onReceiveEnergy(this, ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false) * CompatibilityType.INDUSTRIALCRAFT.ratio;
	}

	@Override
	public double injectEnergyUnits(ForgeDirection direction, double amount)
	{
		int toSend = (int) (amount * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);

		if (StaticTileForwarder.onReceiveEnergy(this, direction, toSend, false) > 0)
		{
			long receive = StaticTileForwarder.onReceiveEnergy(this, direction, toSend, true);

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
		return receiver instanceof IEnergyTile && StaticTileForwarder.onExtractEnergy(this, direction, 1, false) > 0;
	}

	@Override
	public int getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}
}
