package universalelectricity.api.energy;

import universalelectricity.api.net.INetwork;

/** The Electrical Network in interface form.
 * 
 * @author Calclavia */
public interface IEnergyNetwork extends INetwork<IEnergyNetwork, IConductor, Object>
{
    /** Reconstructs the energy network. */
    public void reconstruct();

    /** Produces power to the energy network.
     * 
     * @param receive - The amount that is produced.
     * @return - The amount that was accepted by the network. */
    public long produce(long receive);

    /** @return The last buffer in the network that was sent to all energy handlers. */
    public long getLastBuffer();

    /** Gets an estimated value of what the network inputs */
    public long getRequest();

    /** Gets a value that represents the amount of energy lost in the network */
    public long getEnergyLoss();
}
