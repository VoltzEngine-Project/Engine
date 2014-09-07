package universalelectricity.simulator.parts;

import universalelectricity.api.core.grid.IGrid;
import universalelectricity.api.core.grid.IGridNode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.grid.node.NodeConnector;
import universalelectricity.simulator.SimulatedGrid;

/**
 * Created by robert on 8/16/2014.
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
        }
        return network;
    }
}
