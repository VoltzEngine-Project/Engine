package universalelectricity.core.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.electricity.IElectricalNetwork;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.electricity.IVoltageOutput;
import universalelectricity.api.energy.IConductor;
import universalelectricity.api.vector.Vector3;

/** Modified version of energy network that takes voltage into account. Use of voltage causes energy
 * lose but also allows for faster energy transfers.
 * 
 * @author DarkGuardsman */
public class ElectricalNetwork extends EnergyNetwork implements IElectricalNetwork
{
    /** Voltage the network is producing at */
    private long voltage = 0, newVoltage = 0;
    private long currentCapacity = 0;

    @Override
    public void update()
    {
        newVoltage = 0;
        super.update();
        voltage = newVoltage;
        if (voltage <= 0)
            voltage = UniversalElectricity.DEFAULT_VOLTAGE;
    }

    @Override
    public void reconstruct()
    {
        voltage = 0;
        currentCapacity = Long.MAX_VALUE;

        super.reconstruct();

        if (voltage <= 0)
            voltage = UniversalElectricity.DEFAULT_VOLTAGE;
    }

    @Override
    protected void reconstructConnector(IConductor conductor)
    {
        super.reconstructConnector(conductor);
        currentCapacity = Math.min(currentCapacity, conductor.getCurrentCapacity());
    }

    /** Segmented out call so overriding can be done when machines are reconstructed. */
    @Override
    protected void reconstructHandler(IConductor conductor, Object obj, ForgeDirection side)
    {
        super.reconstructHandler(conductor, obj, side);

        if (obj instanceof IVoltageOutput && !(obj instanceof IConductor))
            voltage = Math.max(voltage, ((IVoltageOutput) obj).getVoltageOutput(side));
    }

    @Override
    public long addEnergyToHandler(Object handler, ForgeDirection side, long energy, boolean doPower)
    {
        if (handler instanceof IVoltageOutput)
            newVoltage = Math.max(newVoltage, ((IVoltageOutput) handler).getVoltageOutput(side));

        if (handler instanceof IVoltageInput)
        {
            if (((IVoltageInput) handler).getVoltageInput(side) != getVoltage())
                ((IVoltageInput) handler).onWrongVoltage(side, voltage);
        }

        return super.addEnergyToHandler(handler, side, energy, doPower);
    }

    @Override
    public long getVoltage()
    {
        return voltage <= 0 ? UniversalElectricity.DEFAULT_VOLTAGE : voltage;
    }

    @Override
    public long produce(IConductor conductor, ForgeDirection from, long amount, boolean doReceive)
    {
        TileEntity tile = null;
        if (conductor instanceof TileEntity)
        {
            tile = new Vector3((TileEntity) conductor).translate(from).getTileEntity(((TileEntity) conductor).worldObj);
        }
        //Don't accept power from under powered generators
        if (tile instanceof IVoltageOutput && !(tile instanceof IConductor))
        {
            if (((IVoltageOutput) tile).getVoltageOutput(from) < voltage)
            {
                return 0;
            }
        }
        return super.produce(conductor, from, amount, doReceive);
    }
}
