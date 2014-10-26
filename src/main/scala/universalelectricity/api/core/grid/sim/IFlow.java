package universalelectricity.api.core.grid.sim;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Applied to any object that wants to restrict the rate by which the input flows into the machine.
 * Flow rate does not denote only fluid movement as it can be used for items, energy, entities, and current.
 * @author Darkguardsman
 */
public interface IFlow
{
    /**
     * Gets the rate by which the input can flow into this object.
     *
     * @param type - simulator type for checking some internal connection logic
     * @param side - side of the machine
     * @return neg numbers are ignored as flow rate is bi-directional.
     * Zero will result in no flow rate on this side, and any pos will result in a restricted flow rate.
     */
    public double getFlow(SimType type, ForgeDirection side);
}
