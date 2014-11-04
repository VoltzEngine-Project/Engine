package resonant.api.grid;

/**
 * Any node that is part of a grid system such as a power network
 */
public interface IGridNode extends INode, INodeConnector
{
    /** Sets the grid reference */
    public void setGrid(IGrid grid);

    /** Gets the grid reference */
    public IGrid getGrid();
}
