package universalelectricity.api.energy;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.net.INetwork;

/** The Energy Network in interface form.
 *
 * @author Calclavia */
public interface IEnergyNetwork extends INetwork<IEnergyNetwork, IConductor, Object>
{
    /** Reconstructs the energy network. */
    public void reconstruct();

    /** Produces power to the energy network.
     *
     * @param source - The source that is producing the energy. NOT the conductor!
     * @param side - The direction the source is producing out towards.
     * @param receive - The amount that is produced.
     * @return The amount that was accepted by the network. */
    public long produce(Object source, ForgeDirection side, long amount, boolean doProduce);

    /** @return The last buffer in the network that was sent to all energy handlers. */
    public long getLastBuffer();

    /** @return The maximum buffer that the network can handle. */
    public long getBufferCapacity();

    /** Gets an estimated value of what the network wants for energy */
    public long getRequest();

    public long getLastAmperageBuffer();

    /** Gets a value that represents the amount of energy lost in the network */
    public float getResistance();

    public void saveBuffer();

    public void loadBuffer();

}
