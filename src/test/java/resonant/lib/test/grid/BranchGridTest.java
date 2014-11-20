package resonant.lib.test.grid;

import junit.framework.TestCase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.util.ForgeDirection;
import org.junit.Test;
import resonant.api.grid.INode;
import resonant.content.prefab.java.TileNode;
import resonant.content.wrapper.BlockDummy;
import resonant.lib.grid.branch.BranchedGrid;
import resonant.lib.grid.branch.NodeBranchPart;
import resonant.lib.grid.branch.part.Branch;
import resonant.lib.grid.branch.part.Junction;
import resonant.lib.grid.branch.part.Part;
import resonant.lib.test.world.FakeWorld;

import java.util.List;

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
    private BranchedGrid grid;
    private FakeWorld world;
    private Block wire;

    @Override
    protected void setUp() throws Exception
    {
        //Create fake world to do actions in
        world = new FakeWorld();

        //Create wire to test with
        wire = new BlockDummy("JUnit", null, new TileWire());
        Block.blockRegistry.addObject(175, "wire", wire);

        //Set up wire grid
        TestMap map = new TestMap(TestMap.simpleTestMap);
        map.addBlock('-', wire);
        map.build(world, 0, 0, 0);

        //Trigger grid to update since we do not have a tick handler
        grid.update(0);
    }

    @Test
    public void testWireExists()
    {
        assertNotNull("Test can't continue without wire being created", wire);
        assertNotNull("Wire is not in the block registry", Block.blockRegistry.getObject("wire"));
    }

    @Test
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

    @Override
    protected void tearDown() throws Exception
    {
        grid.deconstruct();
        grid = null;
        world.clear();
    }


    /**
     * Quick wire class just for this unit test
     * TODO create an action version in RE for future use
     */
    public static class TileWire extends TileNode
    {
        public NodeBranchPart node;

        public TileWire()
        {
            super(Material.sand);
        }

        @Override
        public <N extends INode> N getNode(Class<? extends N> nodeType, ForgeDirection from)
        {
            if (nodeType.isAssignableFrom(NodeBranchPart.class))
                return (N) node;
            return null;
        }

        @Override
        public void getNodes(List<INode> nodes)
        {
            nodes.add(node);
        }

        public NodeBranchPart getNode()
        {
            if (node == null)
            {
                node = new NodeBranchPart(this);
            }
            return node;
        }
    }
}
