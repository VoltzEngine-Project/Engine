package universalelectricity.core.net;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.electricity.IElectricalNetwork;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.electricity.IVoltageOutput;
import universalelectricity.api.energy.IConductor;

/** @author DarkGuardsman */
public class ElectricalNetwork extends EnergyNetwork implements IElectricalNetwork
{
	/** Voltage the network is producing at */
	private long voltage = 0, newVoltage = 0;
	private long currentCapacity = 0;

	@Override
	public void update()
	{
		this.newVoltage = 0;
		super.update();
		this.voltage = newVoltage;
	}

	@Override
	public void reconstruct()
	{
		this.voltage = 0;
		currentCapacity = Long.MAX_VALUE;
		super.reconstruct();
		if (this.voltage <= 0)
		{
			this.voltage = UniversalElectricity.DEFAULT_VOLTAGE;
		}
	}

	@Override
	protected void reconstructConnector(IConductor conductor)
	{
		super.reconstructConnector(conductor);
		if (conductor.getCurrentCapacity() < this.currentCapacity)
		{
			// TODO simulate this better using a path finder cache of sources to acceptors
			this.currentCapacity = conductor.getCurrentCapacity();
		}
	}

	/** Segmented out call so overriding can be done when machines are reconstructed. */
	@Override
	protected void reconstructHandler(Object obj, ForgeDirection side)
	{
		if (obj instanceof IVoltageOutput && !(obj instanceof IConductor))
		{
			if (((IVoltageOutput) obj).getVoltageOutput(side) > voltage)
			{
				// TODO create a mod compatibility handler to get voltage of machines
				this.voltage = ((IVoltageOutput) obj).getVoltageOutput(side);
			}
		}
	}

	@Override
	public long addEnergyToHandler(Object handler, ForgeDirection side, long energy, boolean doPower)
	{
		if (handler instanceof IVoltageOutput)
		{
			if (((IVoltageOutput) handler).getVoltageOutput(side) > this.newVoltage)
			{
				this.newVoltage = ((IVoltageOutput) handler).getVoltageOutput(side);
			}
		}
		if (handler instanceof IVoltageInput)
		{
			if (((IVoltageInput) handler).getVoltageInput(side) != this.getVoltage())
			{
				((IVoltageInput) handler).onWrongVoltage(side, this.voltage);
			}
		}
		return super.addEnergyToHandler(handler, side, energy, doPower);
	}

	@Override
	public long getVoltage()
	{
		return voltage;
	}

	// TODO: FIX THIS!
	@Override
	public long produce(IConductor conductor, ForgeDirection from, long amount, boolean doReceive)
	{
		if (conductor instanceof IVoltageOutput && !(conductor instanceof IConductor))
		{
			if (((IVoltageOutput) conductor).getVoltageOutput(from) >= voltage)
			{
				return super.produce(conductor, from, amount, doReceive);
			}
			return 0;
		}
		return super.produce(conductor, from, amount, doReceive);
	}
}
