package universalelectricity.core.grid.node;

import universalelectricity.api.core.grid.IGrid;
import universalelectricity.api.core.grid.IGridNode;
import universalelectricity.api.core.grid.INodeProvider;

/**
 * Simple node designed to act as a pathway in a grid.
 * @author Darkgurdsman
 */
@Deprecated
public class NodeConductor extends NodeConnector implements IGridNode
{
    protected IGrid grid = null;

    public NodeConductor(INodeProvider parent)
    {
        super(parent);
    }
    @Override
    public void deconstruct()
    {
        super.deconstruct();
        if(grid != null)
        {
            grid.remove(this);
        }
    }

    @Override
    public void reconstruct()
    {
        super.reconstruct();
        if(grid != null)
        {
            grid.add(this);
        }
    }

    @Override
    public void setGrid(IGrid grid)
    {
        this.grid = grid;
    }

    @Override
    public IGrid getGrid()
    {
        return grid;
    }
}
