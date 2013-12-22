package universalelectricity.core.net;

import universalelectricity.api.electricity.IElectricalNetwork;

/** @author DarkGuardsman */
public class ElectricalNetwork extends EnergyNetwork implements IElectricalNetwork
{
    /** Voltage the network is producing at */
    private long voltage = 0;

    @Override
    public long getVoltage()
    {
        return voltage;
    }

}
