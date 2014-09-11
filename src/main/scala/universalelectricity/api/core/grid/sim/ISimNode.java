package universalelectricity.api.core.grid.sim;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.IGridNode;
import universalelectricity.core.transform.vector.IVectorWorld;

/**
 * Version of the node designed to be used with the simulated grid
 * @author Darkguardsman
 */
public interface ISimNode extends IGridNode, IVectorWorld
{
    /**
     * Can the simulator path data(Energy, Fluid, Items) from one side to the other.
     * By default canConnect, and connections will be checked so you don't need to recheck.
     * This is designed to be used for thinks like diodes that force flow in one direction
     *
     * @param type - simulator type for checking some internal connection logic
     * @param from - direction the data is coming from
     * @param two - direction teh data is going to
     * @return true if the data can pass without fail, fail if the data is blocked.
     */
    public boolean canPassToSide(SimType type, ForgeDirection from, ForgeDirection two);
}
