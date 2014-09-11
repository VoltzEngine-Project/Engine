package universalelectricity.simulator.grid.component;

import universalelectricity.api.core.grid.IGrid;
import universalelectricity.api.core.grid.IGridNode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.grid.node.NodeConnector;
import universalelectricity.simulator.grid.SimulatedGrid;

/**
 * @uthor DarkCow
 */
public class NetworkNode extends NodeConnector implements IGridNode
{
    protected SimulatedGrid network;

    public NetworkNode(INodeProvider parent)
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
}
