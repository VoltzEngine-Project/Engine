package resonant.api.grid;

/** Simple point in a grid that can link to or represent a tile/multipart/entity/block */
public interface INode
{
    //TODO add methods for (onAdded() - noting when added to the world, onRemoved() - noting when removed from world)
	/** Called when the grid rebuilds */
	public void reconstruct();

	/** Called before the node is removed from a grid */
	public void deconstruct();

    /** The object that houses this node */
    public INodeProvider getParent();

}
