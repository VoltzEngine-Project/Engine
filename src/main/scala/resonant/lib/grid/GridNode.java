package resonant.lib.grid;

import resonant.api.grid.INodeGrid;
import resonant.lib.grid.node.Node;

/**
 * Created by robert on 8/13/2014.
 */
public class GridNode<N extends Node> extends Grid<N>
{
    public GridNode(Class node)
    {
        super(node);
    }

    @Override
    protected void reconstructNode(N node)
    {
        node.reconstruct();
        if(node instanceof INodeGrid)
            ((INodeGrid)node).setGrid(this);
    }

    @Override
    public void deconstruct()
    {
        for(N node : getNodes())
        {
            if(node instanceof INodeGrid)
                ((INodeGrid)node).setGrid(null);
            node.reconstruct();
        }
        super.deconstruct();
    }
}
