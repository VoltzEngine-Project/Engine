package resonant.lib.test.grid;

import junit.framework.TestCase;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.lib.grid.branch.BranchedGrid;
import resonant.lib.grid.branch.NodeBranchPart;
import resonant.lib.grid.branch.part.Branch;
import resonant.lib.grid.branch.part.Junction;
import resonant.lib.grid.branch.part.Part;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 11/13/2014.
 */
public class BranchGridTest extends TestCase
{
    private List<NodeBranchPart> nodes;
    private BranchedGrid grid;

    public void testForParts()
    {
        assertEquals("Should only be 5 parts, grid contains " + grid.getNodes().size() +" nodes and " + grid.getParts().size() +" parts", 5.0, (double) grid.getParts().size(), 0.0);

        int b = 0;
        int j = 0;
        for (Part part : grid.getParts())
        {
            if (part instanceof Branch)
                b++;
            else if (part instanceof Junction)
                j++;
        }

        assertEquals("Should only be 4 branches", 4.0, (double) b, 0.0);
        assertEquals("Should only be 1 junction", 1.0, (double) j, 0.0);
    }

    public void testForJunctionConnections()
    {
        Junction j = null;
    }

    @Override
    protected void setUp() throws Exception
    {
        nodes = new ArrayList<NodeBranchPart>();
        grid = new BranchedGrid();

        NodeBranchPart northNode = new NodeBranchPart(null);
        NodeBranchPart southNode = new NodeBranchPart(null);
        NodeBranchPart eastNode = new NodeBranchPart(null);
        NodeBranchPart westNode = new NodeBranchPart(null);
        NodeBranchPart northNode2 = new NodeBranchPart(null);
        NodeBranchPart southNode2 = new NodeBranchPart(null);
        NodeBranchPart eastNode2 = new NodeBranchPart(null);
        NodeBranchPart westNode2 = new NodeBranchPart(null);
        NodeBranchPart j4 = new NodeBranchPart(null);

        nodes.add(northNode);
        nodes.add(southNode);
        nodes.add(eastNode);
        nodes.add(westNode);
        nodes.add(northNode2);
        nodes.add(southNode2);
        nodes.add(eastNode2);
        nodes.add(westNode2);
        nodes.add(j4);

        for (NodeBranchPart node : nodes)
            grid.add(node);

        //North connection
        j4.connect(northNode, ForgeDirection.NORTH);
        northNode.connect(j4, ForgeDirection.SOUTH);
        northNode.connect(northNode2, ForgeDirection.NORTH);
        northNode2.connect(northNode, ForgeDirection.SOUTH);

        //South connection
        j4.connect(southNode, ForgeDirection.SOUTH);
        southNode.connect(j4, ForgeDirection.NORTH);
        southNode.connect(southNode2, ForgeDirection.SOUTH);
        southNode2.connect(southNode, ForgeDirection.NORTH);

        //West connection
        j4.connect(westNode, ForgeDirection.WEST);
        westNode.connect(j4, ForgeDirection.EAST);
        westNode.connect(westNode2, ForgeDirection.WEST);
        westNode2.connect(westNode, ForgeDirection.EAST);

        //East connection
        j4.connect(eastNode, ForgeDirection.EAST);
        eastNode.connect(j4, ForgeDirection.WEST);
        eastNode.connect(eastNode2, ForgeDirection.EAST);
        eastNode2.connect(eastNode, ForgeDirection.WEST);

        grid.update(0);
    }

    @Override
    protected void tearDown() throws Exception
    {
        grid.deconstruct();
        nodes.clear();
        grid = null;
    }
}
