package com.builtbroken.mc.test.grid;

import junit.framework.TestCase;
import com.builtbroken.mc.lib.grid.branch.part.Branch;
import com.builtbroken.mc.lib.grid.branch.part.Junction;
import com.builtbroken.mc.lib.grid.branch.part.Part;

/** Unit test for Branch connections and merging
 * Created by robert on 11/13/2014.
 */
public class BranchTest extends TestCase
{
    /**
     * Test a merge of a branch and junction which should fail
     */
    public void testBranchToJunctionMerge()
    {
        Junction j2 = new Junction();
        Branch a = new Branch();

        Part part = a.join(j2);

        if(part != null)
        {
            fail("Branch to junction merge should return null");
        }
    }

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
     * Tests if one branch can merge with another if both have different B connections but no A connection
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

    /**
     * Tests if one branch can merge with another if Branch A has an B Connection and Branch B has a A connection
     */
    public void testBranchToBranchMergeAB()
    {
        Junction j = new Junction();
        Junction j2 = new Junction();

        Branch a = new Branch();
        Branch b = new Branch();

        a.setConnectionB(j);
        b.setConnectionA(j2);

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

    /**
     * Tests if one branch can merge with another if Branch A has an A Connection and Branch B has a B connection
     */
    public void testBranchToBranchMergeBA()
    {
        Junction j = new Junction();
        Junction j2 = new Junction();

        Branch a = new Branch();
        Branch b = new Branch();

        a.setConnectionA(j);
        b.setConnectionB(j2);

        a.join(b);

        if(!a.getConnectionB().equals(j2))
        {
            failNotEquals("BranchA connection B is not junction j2", j, a.getConnectionA());
        }
        if(!a.getConnectionA().equals(j))
        {
            failNotEquals("BranchA connection A is not junction j", j2, a.getConnectionB());
        }
    }
}
