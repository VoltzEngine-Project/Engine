package resonant.lib.grid.branch.part;

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

	public boolean hasMinimalConnections()
	{
		return connectionA != null && connectionB != null;
	}

}
