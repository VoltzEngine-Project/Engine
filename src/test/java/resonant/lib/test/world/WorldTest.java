package resonant.lib.test.world;

import cpw.mods.fml.common.registry.GameRegistry;
import javafx.scene.effect.Reflection;
import junit.framework.TestCase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import resonant.lib.prefab.block.BlockAdvanced;
import resonant.lib.utility.ReflectionUtility;

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
        ReflectionUtility.setMCField(Block.class, null, "blockRegistry" , new FakeRegistryNamespaced());
        Block.registerBlocks();
    }

    public void testBlockRegistry()
    {
        Object block = Block.blockRegistry.getObject("sand");
        assertNotNull(block);
        assertEquals(block.toString(), "sand");
        assertEquals(Block.getIdFromBlock((Block)block), 12);
    }

    public void testCreation()
    {
        assertNotNull("Failed to create world", world);
    }

    public void testBlockPlacement()
    {
        if(Blocks.sand != null)
        {
            world.setBlock(0, 0, 0, Blocks.sand);
            Block block = world.getBlock(0, 0, 0);
            assertEquals("World.getBlock() failed ", Blocks.sand, block);
        }
        else
        {
            fail("Blocks.sand is null");
        }
    }

    public void testTilePlacement()
    {
        if(Blocks.chest != null)
        {
            world.setBlock(0, 0, 0, Blocks.chest);
            Block block = world.getBlock(0, 0, 0);
            assertEquals("World.getBlock() failed ", Blocks.chest, block);
            if (!(world.getTileEntity(0, 0, 0) instanceof TileEntityChest))
            {
                fail("world.getTileEntity() returned the wrong tile\n" + world.getTileEntity(0, 0, 0) + "  should equal TileEntityChest");
            }
        }
        else
        {
            fail("Blocks.chest is null");
        }
    }
}
