package resonant.lib.test.grid;

import junit.framework.TestCase;
import resonant.lib.grid.branch.part.Branch;
import resonant.lib.grid.branch.part.Junction;

/**
 * Created by robert on 11/13/2014.
 */
public class BranchTest extends TestCase
{
    /**
     * Tests if one branch can merge with another if both have different A connections but no B connection
     */
    public void testBranchToBranchMergeBB()
    {
        Junction j = new Junction();
        Junction j2 = new Junction();

        Branch a = new Branch();
        Branch b = new Branch();

        a.setConnectionA(j);
        b.setConnectionA(j2);

        a.join(b);

        if(!a.getConnectionA().equals(j))
        {
            failNotEquals("BranchA connection A is not junction j", j, a.getConnectionA());
        }
        if(!a.getConnectionB().equals(j2))
        {
            failNotEquals("BranchA connection B is not junction j2", j2, a.getConnectionB());
        }
    }

    /**
     * Tests if one branch can merge with another if both have different A connections but no B connection
     */
    public void testBranchToBranchMergeAA()
    {
        Junction j = new Junction();
        Junction j2 = new Junction();

        Branch a = new Branch();
        Branch b = new Branch();

        a.setConnectionB(j);
        b.setConnectionB(j2);

        a.join(b);

        if(!a.getConnectionA().equals(j2))
        {
            failNotEquals("BranchA connection A is not junction j2", j, a.getConnectionA());
        }
        if(!a.getConnectionB().equals(j))
        {
            failNotEquals("BranchA connection B is not junction j", j2, a.getConnectionB());
        }
    }
}
