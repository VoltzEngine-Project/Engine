package com.builtbroken.test.prefab.blast;

import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.imp.transform.vector.BlockPos;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import com.builtbroken.mc.prefab.explosive.blast.BlastSimplePath;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/17/2016.
 */
@RunWith(VoltzTestRunner.class)
public class TestBlastSimplePath extends AbstractTest
{
    @Test
    public void testBlast()
    {
        World world = FakeWorld.newWorld("testBlastSimple");

        //Test Size 1
        checkBlast(world, 0, 30, 0, 1, 7);
        checkBlast(world, 0, 10, 0, 1, 7);
        //Test Size 2
        checkBlast(world, 0, 30, 0, 2, 33);
        checkBlast(world, 0, 10, 0, 2, 33);
        //Test Size 2
        checkBlast(world, 0, 30, 0, 3, 123);
        checkBlast(world, 0, 10, 0, 3, 123);
    }

    @Test
    public void testBlastPathEntire()
    {
        World world = FakeWorld.newWorld("testBlastSimple");
        reset(world);

        TestBlastPath blast = new TestBlastPath(null);
        blast.setLocation(world, 0, 30, 0);
        blast.setYield(3);

        //First loop
        List<IWorldEdit> edits = new ArrayList();
        blast.pathEntire(new BlockPos(0, 30, 0), edits, 1);
        assertEquals(7, edits.size()); //Should edit 7 blocks
        assertEquals(6, blast.stack.size()); //Should map 6 new starting points
        //TODO check each entry in stack

        edits.clear();

        //TODO path one at a time looking at each entry
    }

    private final void pathNext(TestBlastPath blast, int x, int y, int z)
    {
        List<IWorldEdit> edits  = new ArrayList();
        blast.continuePathEntire(edits, 1);
        assertEquals(1, edits.size());
    }

    private final void reset(World world)
    {
        for (int x = -10; x <= 10; x++)
        {
            for (int z = -10; z <= 10; z++)
            {
                for (int y = 0; y <= 30; y++)
                {
                    world.setBlock(x, y, z, Blocks.dirt);
                }
            }
        }
    }

    private final List<IWorldEdit> checkBlast(World world, int x, int y, int z, double size, int blocks)
    {
        //Reset map
        reset(world);

        //Trigger blast
        TestBlastPath blast = new TestBlastPath(null);
        blast.setLocation(world, x, y, z);
        blast.setYield(size);
        List<IWorldEdit> edits = new ArrayList();
        blast.getEffectedBlocks(edits);

        //Test results
        checkValues(edits, blast, blocks);
        return edits;
    }

    private final void checkValues(List<IWorldEdit> edits, TestBlastPath blast, int blocks)
    {
        //Edit checks
        int blocksFound = 0;
        for (IWorldEdit edit : edits)
        {
            if (edit.getNewBlock() == Blocks.gold_block)
            {
                blocksFound++;
            }
        }

        //Distance check
        for (IWorldEdit edit : edits)
        {
            double d = blast.blockCenter.distance(edit.xi() + 0.5, edit.yi() + 0.5, edit.zi() + 0.5);
            if (d > blast.size)
            {
                fail("Edit is outside size of blast, " + edit + "    " + d + " > " + blast.size);
            }
        }

        //Checks if the edit list contains the same values
        for (IWorldEdit edit : edits)
        {
            for (IWorldEdit edit2 : edits)
            {
                if (edit != edit2 && edit.xi() == edit2.xi() && edit.yi() == edit2.yi() && edit.zi() == edit2.zi())
                {
                    fail("Edit values contain duplication");
                }
            }
        }

        assertSame(blocks, blocksFound);
        assertEquals(blocks, edits.size());
    }

    private class TestBlastPath extends BlastSimplePath<TestBlastPath>
    {
        public TestBlastPath(IExplosiveHandler handler)
        {
            super(handler);
        }

        @Override
        public IWorldEdit changeBlock(BlockPos location)
        {
            return new BlockEdit(world, location).set(Blocks.gold_block);
        }

        @Override
        protected boolean shouldKillAction()
        {
            return killExplosion || world == null;
        }
    }
}
