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

    /** Produces power to the energy network with no care if energy is actually fully used
     * 
     * @param source - machine that is producing the energy
     * @param voltage - voltage the energy is emitted at
     * @param amps - amps the energy is emitted at
     * @return amount of energy that was accepted by the network */
    public long produce(Object source, long voltage, long amps, boolean doProduce);
}
