package universalelectricity.simulator.grid.component;

import universalelectricity.api.core.grid.IGrid;
import universalelectricity.api.core.grid.IGridNode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.grid.node.NodeConnector;
import universalelectricity.simulator.grid.LinkedGrid;

/**
 * @uthor DarkCow
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
			network.add(this);
        }
        return network;
    }
}
