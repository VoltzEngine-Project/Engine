package com.builtbroken.mc.test.grid;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import net.minecraft.tileentity.TileEntity;
import com.builtbroken.mc.lib.grid.branch.BranchedGrid;
import com.builtbroken.mc.lib.grid.branch.part.Branch;
import com.builtbroken.mc.lib.grid.branch.part.Junction;
import com.builtbroken.mc.lib.grid.branch.part.Part;
import com.builtbroken.mc.prefab.tile.TileConductor;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import com.builtbroken.mc.lib.transform.vector.Pos;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Creates a plus sign wire grid for simple connection testing
 * <p/>
 * Created by robert on 11/13/2014.
 */
//Visual of grid
//   [j] = junction, | or -- = wires, [m] machine
// 0
// 1
// 2       -- --[j]-- --
// 3    |        |        |
// 4    |        |        |
// 5   [j]-- -- [j] -- --[j]
// 6    |        |        |
// 7    |        |        |
// 8       -- --[j]-- --
// 9
// 10
@RunWith(VoltzTestRunner.class)
public class BranchGridTest extends AbstractTest
{
    private static FakeWorld world;
    private static BranchedGrid grid;


    @Test
    public void testNodes()
    {
        for (TileEntity tile : world.tiles)
        {
            if (tile instanceof TileConductor)
            {
                if (((TileConductor) tile).getNode() == null)
                {
                    fail("TileWire has no node " + tile);
                }
                else if (((TileConductor) tile).getNode().getConnections().size() == 0)
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

        int b = 0;
        int j = 0;
        for (Part part : grid.getParts())
        {
            if (part instanceof Branch)
                b++;
            else if (part instanceof Junction)
                j++;
        }

        assertEquals("Should only be " + WireMap.WireTests.JUNCTION_FIVE.numberOfBranches + " branches", WireMap.WireTests.JUNCTION_FIVE.numberOfBranches, b, 0);
        assertEquals("Should only be " + WireMap.WireTests.JUNCTION_FIVE.numberOfJunctions + " junction", WireMap.WireTests.JUNCTION_FIVE.numberOfJunctions, j, 0);
    }

    @Override
    public void setUpForTest(String name)
    {
        //Create fake world to do actions in
        world = new FakeWorld();
        world.genFlatData();

        //Set up wire grid
        WireMap.WireTests.JUNCTION_FIVE.build(world, 0, 12, 0);

        if(world.tiles.size() == 0)
        {
            world.printLevel(12);
            fail("No tiles were placed into the world");

        }

        world.updateEntities();


        TileEntity tile = world.tiles.get(0);
        if (tile instanceof TileConductor)
        {
            grid = ((TileConductor) tile).getNode().getGrid();
        }
        else
        {
            System.out.println("Something went wrong building " + name + ".\n No tile was found to get grid from.");
            for(TileEntity t : world.tiles)
            {
                System.out.println("Tile: " + t + "   Vec: " + new Pos(t));
            }
            fail();
        }

        //Trigger grid to update since we do not have a tick handler
        grid.update(0);
    }

    @Override
    public void tearDownForTest(String name)
    {
        grid.deconstruct();
        grid = null;
        world.clear();
        world = null;
    }
}
