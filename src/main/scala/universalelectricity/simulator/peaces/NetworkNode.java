package universalelectricity.simulator.peaces;

import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.core.grid.node.NodeConnector;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkNode extends NodeConnector
{
    public NetworkNode(INodeProvider parent)
    {
        super(parent);
    }
}
