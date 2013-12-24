package universalelectricity.core.net;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.electricity.IElectricalNetwork;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.electricity.IVoltageOutput;
import universalelectricity.api.energy.IConductor;

/** @author DarkGuardsman */
public class ElectricalNetwork extends EnergyNetwork implements IElectricalNetwork
{
	/** Voltage the network is producing at */
	private long voltage = 0;

	/** The average current capacity of the network. */
	private long currentCapacity;

	@Override
	public void reconstruct()
	{
		if (this.getConnectors().size() > 0)
		{
			this.voltage = 0;
			super.reconstruct();
			this.currentCapacity /= this.getConnectors().size();

			// Override the energy buffer.
			this.energyBufferCapacity = this.currentCapacity * this.voltage;
		}
	}

	@Override
	protected void reconstructConductor(IConductor conductor)
	{
		super.reconstructConductor(conductor);
		this.currentCapacity += conductor.getCurrentCapacity();
	}

	/** Segmented out call so overriding can be done when machines are reconstructed. */
	@Override
	protected void reconstructHandler(Object obj, ForgeDirection side)
	{
		if (obj instanceof IVoltageOutput && !(obj instanceof IConductor))
		{
			if (((IVoltageOutput) obj).getVoltageOutput(side) > voltage)
			{
				this.voltage = ((IVoltageOutput) obj).getVoltageOutput(side);
			}
		}
	}

	@Override
	public long applyPowerToHandler(Object handler, ForgeDirection side, long energy, boolean doPower)
	{
		if (handler instanceof IVoltageInput)
		{
			if (((IVoltageInput) handler).getVoltageInput(side) != this.getVoltage())
			{
				((IVoltageInput) handler).onWrongVoltage(side, this.voltage);
			}
		}
		return super.applyPowerToHandler(handler, side, energy, doPower);
	}

	@Override
	public long getVoltage()
	{
		return voltage;
	}

	@Override
	public long produce(Object source, ForgeDirection side, long amount, boolean doReceive)
	{
		if (source instanceof IVoltageOutput && !(source instanceof IConductor))
		{
			if (((IVoltageOutput) source).getVoltageOutput(side) >= voltage)
			{
				return super.produce(source, side, amount, doReceive);
			}
			return 0;
		}
		return super.produce(source, side, amount, doReceive);
	}
}
