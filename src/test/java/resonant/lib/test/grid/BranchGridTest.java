package resonant.lib.test.grid;

import junit.framework.TestCase;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.jmock.Mockery;
import resonant.lib.grid.branch.BranchedGrid;
import resonant.lib.grid.branch.NodeBranchPart;
import resonant.lib.grid.branch.part.Branch;
import resonant.lib.grid.branch.part.Junction;
import resonant.lib.grid.branch.part.Part;
import resonant.lib.test.world.FakeWorld;

/**
 * Created by robert on 11/13/2014.
 */
public class BranchGridTest extends TestCase
{
    private BranchedGrid grid;
    private World world;

    Mockery context;

    public void testWorld()
    {

        Block block = world.getBlock(0, 0, 0);
        System.out.println("Block: " + block);
    }

    public void testForParts()
    {
        assertEquals("Should only be 15 parts, grid contains " + grid.getNodes().size() +" nodes and " + grid.getParts().size() +" parts", 15.0, (double) grid.getParts().size(), 0.0);

        int b = 0;
        int j = 0;
        for (Part part : grid.getParts())
        {
            if (part instanceof Branch)
                b++;
            else if (part instanceof Junction)
                j++;
        }

        assertEquals("Should only be 12 branches", 12.0, (double) b, 0.0);
        assertEquals("Should only be 3 junction", 3.0, (double) j, 0.0);
    }

    @Override
    protected void setUp() throws Exception
    {
        world = new FakeWorld();
        grid = new BranchedGrid();

        NodeBranchPart northNode = createNewNode();
        NodeBranchPart southNode = createNewNode();
        NodeBranchPart eastNode = createNewNode();
        NodeBranchPart westNode = createNewNode();
        NodeBranchPart northNode2 = createNewNode();
        NodeBranchPart southNode2 = createNewNode();
        NodeBranchPart eastNode2 = createNewNode();
        NodeBranchPart westNode2 = createNewNode();
        NodeBranchPart j4 = createNewNode();
        NodeBranchPart ja = createNewNode();
        NodeBranchPart jb = createNewNode();
        NodeBranchPart jc = createNewNode();
        NodeBranchPart jd = createNewNode();
        NodeBranchPart c1 = createNewNode();
        NodeBranchPart c2 = createNewNode();
        NodeBranchPart c3 = createNewNode();
        NodeBranchPart c4 = createNewNode();
        NodeBranchPart c5 = createNewNode();
        NodeBranchPart c6 = createNewNode();
        NodeBranchPart c7 = createNewNode();
        NodeBranchPart c8 = createNewNode();
        //This set of code creates a plus sign wire grid for simple connection testing
        //All connections are preset to avoiding testing connection creationg loops
        //Visual of grid
        //   [j] = junction, | or -- = wires, [m] machine
        // 0
        // 1
        // 2          --[j]--
        // 3             |
        // 4    |        |        |
        // 5   [j]-- -- [j] -- --[j]
        // 6    |        |        |
        // 7             |
        // 8          --[j]--
        // 9
        // 10



        //North wire
        j4.connect(northNode, ForgeDirection.NORTH);
        northNode.connect(j4, ForgeDirection.SOUTH);
        northNode.connect(northNode2, ForgeDirection.NORTH);
        northNode2.connect(northNode, ForgeDirection.SOUTH);
        northNode2.connect(ja, ForgeDirection.NORTH);
        //North machine row wire connection
        ja.connect(northNode2, ForgeDirection.SOUTH);
        c1.connect(ja, ForgeDirection.EAST);
        ja.connect(c1, ForgeDirection.WEST);
        c2.connect(ja, ForgeDirection.WEST);
        ja.connect(c2, ForgeDirection.EAST);

        //East connection
        j4.connect(eastNode, ForgeDirection.EAST);
        eastNode.connect(j4, ForgeDirection.WEST);
        eastNode.connect(eastNode2, ForgeDirection.EAST);
        eastNode2.connect(eastNode, ForgeDirection.WEST);
        eastNode2.connect(jb, ForgeDirection.EAST);
        //East machine row wire connections
        jb.connect(eastNode2, ForgeDirection.WEST);
        c3.connect(jb, ForgeDirection.NORTH);
        jb.connect(c3, ForgeDirection.SOUTH);
        c4.connect(jb, ForgeDirection.SOUTH);
        jb.connect(c4, ForgeDirection.NORTH);

        //South connection
        j4.connect(southNode, ForgeDirection.SOUTH);
        southNode.connect(j4, ForgeDirection.NORTH);
        southNode.connect(southNode2, ForgeDirection.SOUTH);
        southNode2.connect(southNode, ForgeDirection.NORTH);
        southNode2.connect(jc, ForgeDirection.SOUTH);
        //South machine row wire connections
        jc.connect(eastNode2, ForgeDirection.NORTH);
        c3.connect(jb, ForgeDirection.NORTH);
        jb.connect(c3, ForgeDirection.SOUTH);
        c4.connect(jb, ForgeDirection.SOUTH);
        jb.connect(c4, ForgeDirection.NORTH);

        //West connection
        j4.connect(westNode, ForgeDirection.WEST);
        westNode.connect(j4, ForgeDirection.EAST);
        westNode.connect(westNode2, ForgeDirection.WEST);
        westNode2.connect(westNode, ForgeDirection.EAST);
        westNode2.connect(jd, ForgeDirection.WEST);
        jd.connect(westNode2, ForgeDirection.EAST);



        grid.update(0);
    }

    protected NodeBranchPart createNewNode()
    {
        NodeBranchPart node = new NodeBranchPart(null);
        grid.add(node);
        return node;
    }

    @Override
    protected void tearDown() throws Exception
    {
        grid.deconstruct();
        grid = null;
    }
}
