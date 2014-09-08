package universalelectricity.simulator.grid.parts;

import universalelectricity.api.core.grid.IGrid;
import universalelectricity.api.core.grid.IGridNode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.grid.node.NodeConnector;
import universalelectricity.simulator.grid.LinkedGrid;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkNode extends NodeConnector implements IGridNode
{
    protected LinkedGrid network;

    public NetworkNode(INodeProvider parent)
    {
        super(parent);
    }

    @Override
    public void setGrid(IGrid grid)
    {
        if(grid instanceof LinkedGrid)
        {
            network = (LinkedGrid) grid;
        }
    }

    @Override
    public LinkedGrid getGrid()
    {
        if(network == null)
        {
            network = new LinkedGrid(this);
        }
        return network;
    }
}
