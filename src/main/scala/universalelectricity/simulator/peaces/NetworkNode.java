package universalelectricity.simulator.peaces;

import universalelectricity.api.core.grid.IGrid;
import universalelectricity.api.core.grid.IGridNode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.grid.node.NodeConnector;
import universalelectricity.simulator.Network;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkNode extends NodeConnector implements IGridNode
{
    protected Network network;

    public NetworkNode(INodeProvider parent)
    {
        super(parent);
    }

    @Override
    public void setGrid(IGrid grid)
    {
        if(grid instanceof Network)
        {
            network = (Network) grid;
        }
    }

    @Override
    public Network getGrid()
    {
        if(network == null)
        {
            network = new Network(this);
        }
        return network;
    }
}
