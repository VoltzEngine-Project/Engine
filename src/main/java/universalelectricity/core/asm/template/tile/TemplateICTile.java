package universalelectricity.core.asm.template.tile;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
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
public abstract class TemplateICTile implements IEnergySink, IEnergySource, IEnergyInterface
{
	/**
	 * Deobfuscated environment
	 */
	public void validate()
	{
		StaticTileForwarder.validateTile(this);
		StaticTileForwarder.loadIC(this);
	}

	public void invalidate()
	{
		StaticTileForwarder.unloadIC(this);
		StaticTileForwarder.invalidateTile(this);
	}

	public void onChunkUnload()
	{
		StaticTileForwarder.unloadIC(this);
	}

	/**
	 * Obfuscated Environment
	 */
	public void func_70312_q()
	{
		StaticTileForwarder.validateTile(this);
		StaticTileForwarder.loadIC(this);
	}

	public void func_70313_j()
	{
		StaticTileForwarder.unloadIC(this);
		StaticTileForwarder.invalidateTile(this);
	}

	public void func_76631_c()
	{
		StaticTileForwarder.unloadIC(this);
	}

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
		StaticTileForwarder.onExtractEnergy(this, ForgeDirection.UNKNOWN, (long) (amount * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio), true);
	}

	@Override
	public double demandedEnergyUnits()
	{
		return StaticTileForwarder.onReceiveEnergy(this, ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false) * CompatibilityType.INDUSTRIALCRAFT.ratio;
	}

	@Override
	public double injectEnergyUnits(ForgeDirection direction, double amount)
	{
		long energyToInject = (long) (amount * CompatibilityType.INDUSTRIALCRAFT.reciprocal_ratio);

		if (StaticTileForwarder.onReceiveEnergy(this, direction, energyToInject, false) > 0)
		{
			long receive = StaticTileForwarder.onReceiveEnergy(this, direction, energyToInject, true);

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
		return StaticTileForwarder.onExtractEnergy(this, direction, 1, false) > 0;
	}

	@Override
	public int getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}
}
