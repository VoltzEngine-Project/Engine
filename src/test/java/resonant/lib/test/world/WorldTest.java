package resonant.lib.test.world;

import junit.framework.TestCase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * Created by robert on 11/13/2014.
 */
public class WorldTest extends TestCase
{
    World world = null;

    @Override
    protected void setUp() throws Exception
    {
        world = new FakeWorld();
    }

    public void testCreation()
    {
        assertNotNull("Failed to create world: " + world);
    }

    public void testBlockPlacement()
    {
        world.setBlock(0, 0, 0, Blocks.sand);
        Block block = world.getBlock(0, 0, 0);
        assertEquals(Blocks.sand, block);
    }
}
