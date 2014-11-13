package resonant.lib.grid.branch.part;

import resonant.lib.grid.branch.NodeBranchPart;

/**
 * Wraps one or more nodes into a simple object that is used in BranchedGrid
 *
 * @author DarkCow
 */
public class Branch extends Part
{
	Object connectionA = null;
	Object connectionB = null;

	public void setConnectionA(Object part)
	{
		this.connectionB = part;
	}

	public void setConnectionB(Object part)
	{
		this.connectionA = part;
	}

    public void add(NodeBranchPart node)
    {
        super.add(node);
        node.setBranch(this);
    }

    public void remove(NodeBranchPart node)
    {
        super.remove(node);
        node.setBranch(null);
    }

	public boolean hasMinimalConnections()
	{
		return connectionA != null && connectionB != null;
	}

}
