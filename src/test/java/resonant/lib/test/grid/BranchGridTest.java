package resonant.lib.test.grid;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import resonant.content.wrapper.BlockDummy;
import resonant.lib.grid.branch.BranchedGrid;
import resonant.lib.grid.branch.part.Branch;
import resonant.lib.grid.branch.part.Junction;
import resonant.lib.grid.branch.part.Part;
import resonant.lib.prefab.TileConductor;
import resonant.lib.test.world.FakeWorld;

/**
 * Creates a plus sign wire grid for simple connection testing
 * <p/>
 * Created by robert on 11/13/2014.
 */
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
public class BranchGridTest extends TestCase
{
    private static FakeWorld world;
    private static Block wire;
    private static BranchedGrid grid;

    public void testWireExists()
    {
        assertNotNull("Test can't continue without wire being created", wire);
        assertNotNull("Wire is not in the block registry", Block.blockRegistry.getObject("wire"));
    }

    public void testThatMapHasTiles()
    {
        assertEquals(21, world.tiles.size());
    }

    public void testForNodes()
    {
        for(TileEntity tile : world.tiles)
        {
            if(tile instanceof TileConductor)
            {
                if(((TileConductor) tile).getNode() == null)
                {
                    fail("TileWire has no node " + tile);
                }
                else if(((TileConductor) tile).getNode().getConnections().size() == 0)
                {
                    fail("Branch node failed to make connections. At least one connection was expected");
                }
            }
            else
            {
                fail("Tile was placed that is not an instanceof TileConductor");
            }
        }
    }

    public void testForParts()
    {
        assertEquals("Should only be 15 parts, grid contains " + grid.getNodes().size() + " nodes and " + grid.getParts().size() + " parts", 15.0, (double) grid.getParts().size(), 0.0);

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

    public static Test suite()
    {
        TestSetup setup = new TestSetup(new TestSuite(BranchGridTest.class))
        {
            @Override
            protected void setUp() throws Exception
            {
                //Create fake world to do actions in
                world = new FakeWorld();

                //Create wire to test with
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

                //Set up wire grid
                TestMap map = new TestMap(TestMap.simpleTestMap);
                map.addBlock('-', wire);
                map.build(world, 0, 0, 0);

                world.updateEntities();

                TileEntity tile = world.getTileEntity(5, 0, 6);
                if(tile instanceof TileConductor)
                {
                    grid = ((TileConductor)tile).getNode().getGrid();
                }

                //Trigger grid to update since we do not have a tick handler
                grid.update(0);
            }

            @Override
            protected void tearDown() throws Exception
            {
                grid.deconstruct();
                grid = null;
                world.clear();
                world = null;
            }
        };
        return setup;
    }
}
