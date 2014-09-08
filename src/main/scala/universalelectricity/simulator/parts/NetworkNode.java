package universalelectricity.simulator.parts;

import universalelectricity.api.core.grid.IGrid;
import universalelectricity.api.core.grid.IGridNode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.grid.node.NodeConnector;
import universalelectricity.simulator.grid.SimulationGrid;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkNode extends NodeConnector implements IGridNode
{
    protected SimulationGrid network;

    public NetworkNode(INodeProvider parent)
    {
        super(parent);
    }

    @Override
    public void setGrid(IGrid grid)
    {
        if(grid instanceof SimulationGrid)
        {
            network = (SimulationGrid) grid;
        }
    }

    @Override
    public SimulationGrid getGrid()
    {
        if(network == null)
        {
            network = new SimulationGrid(this);
        }
        return network;
    }
}
