package com.builtbroken.test.prefab.blast;

import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import com.builtbroken.mc.lib.world.edit.WorldEditSorter;
import com.builtbroken.mc.prefab.explosive.blast.BlastSimplePath;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/14/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TestBlastSimple extends AbstractTest
{
    private World world;
    private Logger logger = LogManager.getLogger("TestBlastSimple");

    @Override
    public void setUpForTest(String name)
    {
        world = FakeWorld.newWorld(name);
    }

    @Test
    public void testPathNext()
    {
        BlastSimplePath blast = new TestBlastSimplePath(world, 0, 100, 0, 1);
        int[] expectedSizes = {7, 12};
        for (int i = 0; i < 2; i++)
        {
            blast.size = i + 1;
            List<IWorldEdit> edits = blast.getEffectedBlocks();
            if (expectedSizes[i] != edits.size())
            {
                Collections.sort(edits, new WorldEditSorter());
                for (IWorldEdit edit : edits)
                {
                    logger.info(edit + " D: " + blast.center.distance(edit));
                }
                fail();
            }
        }
    }

    @Test
    public void testPathEntire()
    {
        BlastSimplePath blast = new TestBlastSimplePath(world, 0, 100, 0, 1);
        blast.recursive = false;
        List<IWorldEdit> edits = blast.getEffectedBlocks();
        assertEquals(7, edits.size());
    }

    /** Used only for this test class due to the target class being abstract. */
    private class TestBlastSimplePath extends BlastSimplePath
    {
        public TestBlastSimplePath(World world, int x, int y, int z, int size)
        {
            super(world, x, y, z, size);
        }

        @Override
        public BlockEdit changeBlock(Location location)
        {
            return new BlockEdit(location).set(Blocks.coal_block, 0, false, true);
        }
    }


}
