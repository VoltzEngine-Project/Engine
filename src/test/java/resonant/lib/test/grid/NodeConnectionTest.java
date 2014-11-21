package resonant.lib.test.grid;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.content.wrapper.BlockDummy;
import resonant.lib.grid.branch.NodeBranchPart;
import resonant.lib.prefab.TileConductor;
import resonant.lib.test.world.FakeWorld;
import resonant.lib.transform.vector.VectorWorld;

import java.util.Map;

/**
 * Created by robert on 11/20/2014.
 */
public class NodeConnectionTest extends TestCase
{
    private static FakeWorld world;
    private static Block wire;

    public void testWireExists()
    {
        assertNotNull("Test can't continue without wire being created", wire);
        assertNotNull("Wire is not in the block registry", Block.blockRegistry.getObject("wire"));
    }

    public void testNorth()
    {
        checkForConnection(ForgeDirection.NORTH);
    }

    public void testSouth()
    {
        checkForConnection(ForgeDirection.SOUTH);
    }

    public void testEast()
    {
        checkForConnection(ForgeDirection.EAST);
    }

    public void testWest()
    {
        checkForConnection(ForgeDirection.WEST);
    }

    public void testUp()
    {
        checkForConnection(ForgeDirection.UP);
    }

    public void testDown()
    {
        checkForConnection(ForgeDirection.DOWN);
    }

    public void checkForConnection(ForgeDirection side)
    {
        TileEntity tile = new VectorWorld(world, 8, 8, 8).getTileEntity();
        assertNotNull("There should be a tile at Vec(8,8,8)", tile);
        if(tile instanceof TileConductor)
        {
            NodeBranchPart node = ((TileConductor) tile).getNode();
            assertNotNull("There should be a node at Vec(8,8,8)", node);
            if(node.getConnections().size() == 0)
                fail("Should be at least one connection");

            boolean found = false;
            for(Map.Entry<NodeBranchPart, ForgeDirection> entry : node.getConnections().entrySet())
            {
                if(entry.getValue() == side)
                {
                    found = true;
                }
            }
            if(!found)
                fail("No " + side + " connection found");
        }
    }

    public static Test suite()
    {
        TestSetup setup = new TestSetup(new TestSuite(NodeConnectionTest.class))
        {
            @Override
            protected void setUp() throws Exception
            {
                //Create fake world to do actions in
                world = new FakeWorld();

                //Create wire to test with
                if(Block.blockRegistry.getObject("wire") == null)
                {
                    wire = new BlockDummy("JUnit", null, new TileConductor());
                    Block.blockRegistry.addObject(175, "wire", wire);
                }
                else
                {
                    wire = (Block) Block.blockRegistry.getObject("wire");
                }
                VectorWorld center = new VectorWorld(world, 8, 8, 8);
                center.setBlock(wire);
                for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                {
                    VectorWorld vec = center.add(dir);
                    vec.setBlock(wire);
                }
                TileEntity tile = center.getTileEntity();
                if(tile instanceof TileConductor)
                {
                    ((TileConductor) tile).getNode().buildConnections();
                }
            }

            @Override
            protected void tearDown() throws Exception
            {
                world.clear();
                world = null;
            }
        };
        return setup;
    }
}
