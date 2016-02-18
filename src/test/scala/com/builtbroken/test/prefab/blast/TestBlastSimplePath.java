package com.builtbroken.test.prefab.blast;

import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.lib.transform.vector.Location;
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

    private final List<IWorldEdit> checkBlast(World world, int x, int y, int z, int size, int air)
    {
        List<IWorldEdit> edits = popBlast(world, x, y, z, size);
        checkValues(edits, air);
        return edits;
    }

    private final List<IWorldEdit> popBlast(World world, int x, int y, int z, int size)
    {
        TestBlastPath blast = new TestBlastPath();
        blast.setLocation(world, x, y, z);
        blast.setYield(size);
        List<IWorldEdit> edits = new ArrayList();
        blast.getEffectedBlocks(edits);
        return edits;
    }

    private final void checkValues(List<IWorldEdit> edits, int expectedAir)
    {
        assertEquals(expectedAir, edits.size());
        int air = 0;
        for (IWorldEdit edit : edits)
        {
            if (edit.getNewBlock() == Blocks.gold_block)
            {
                air++;
            }
        }
        assertSame(expectedAir, air);
    }

    private class TestBlastPath extends BlastSimplePath<TestBlastPath>
    {
        @Override
        public IWorldEdit changeBlock(Location location)
        {
            return new BlockEdit(location).set(Blocks.gold_block);
        }

        @Override
        protected boolean shouldKillAction()
        {
            return killExplosion || world == null;
        }
    }
}
