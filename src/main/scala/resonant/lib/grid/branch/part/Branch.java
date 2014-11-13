package resonant.lib.grid.branch.part;

import resonant.lib.grid.branch.NodeBranchPart;

/**
 * Wraps one or more nodes into a simple object that is used in BranchedGrid
 *
 * @author DarkCow
 */
public class Branch extends Part
{
	Part connectionA = null;
	Part connectionB = null;

	public void setConnectionA(Part part)
	{
		this.connectionB = part;
	}

    public Part getConnectionA()
    {
        return connectionA;
    }

	public void setConnectionB(Part part)
	{
		this.connectionA = part;
	}

    public Part getConnectionB()
    {
        return connectionB;
    }

	public boolean hasMinimalConnections()
	{
		return getConnectionA() != null && getConnectionB() != null;
	}

    @Override
    public Branch join(Part part)
    {
        if(part != this & part instanceof Branch)
        {
            // A connection overlap
            if(((Branch) part).getConnectionA() == null && getConnectionA() == null || getConnectionA() == ((Branch) part).getConnectionA())
            {
                setConnectionA(((Branch) part).getConnectionB());
            }
            // B connection overlap
            else if(((Branch) part).getConnectionB() == null && getConnectionB() == null || getConnectionB() == ((Branch) part).getConnectionB())
            {
                setConnectionB(((Branch) part).getConnectionA());
            }
            // B1 overlap A2
            else if(((Branch) part).getConnectionA() == null && getConnectionB() == null || ((Branch) part).getConnectionA() == getConnectionB())
            {
                setConnectionB(((Branch) part).getConnectionB());
            }
            // A1 overlap B2
            else if(((Branch) part).getConnectionB() == null && getConnectionA() == null || ((Branch) part).getConnectionB() == getConnectionA())
            {
                setConnectionA(((Branch) part).getConnectionA());
            }

            //Cleanup
            ((Branch) part).setConnectionA(null);
            ((Branch) part).setConnectionB(null);
        }
        return this;
    }

}
