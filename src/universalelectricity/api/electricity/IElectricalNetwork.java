package universalelectricity.api.electricity;

import universalelectricity.api.energy.IEnergyNetwork;

/** Extended version of the energy network that implements Voltage and Amperage to understand energy
 * 
 * @author DarkGuardsman */
public interface IElectricalNetwork extends IEnergyNetwork
{
    /** Gets Voltage that the network is at */
    public long getVoltage();

    /** Gets the Amps the network is outputting */
    public long getAmperage();
}
