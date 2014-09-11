package universalelectricity.simulator.grid.component;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.IGrid;
import universalelectricity.api.core.grid.IGridNode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.api.core.grid.sim.ISimNode;
import universalelectricity.api.core.grid.sim.SimType;
import universalelectricity.core.grid.node.NodeConnector;
import universalelectricity.simulator.grid.SimulatedGrid;

/**
 * Node prefab for the simulator grid
 * @author Darkguardsman
 */
public class SimNode extends NodeConnector implements ISimNode
{
    protected SimulatedGrid network;

    public SimNode(INodeProvider parent)
    {
        super(parent);
    }

    @Override
    public void setGrid(IGrid grid)
    {
        if(grid instanceof SimulatedGrid)
        {
            network = (SimulatedGrid) grid;
        }
    }

    @Override
    public SimulatedGrid getGrid()
    {
        if(network == null)
        {
            network = new SimulatedGrid(this);
			network.add(this);
        }
        return network;
    }

    @Override
    public boolean canPassToSide(SimType type, ForgeDirection from, ForgeDirection two)
    {
        return true;
    }
}
