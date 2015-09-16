package com.builtbroken.test.prefab.tile;

import com.builtbroken.mc.prefab.tile.BlockTile;
import com.builtbroken.mc.prefab.tile.Tile;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.ModRegistry;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * JUnit test for {@link Tile}
 * Created by robert on 1/6/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TestTile extends AbstractTest
{
    public static Block testBlock;

    /* Use this to generate methods for a class for unit testing, saves so much time
    public void testDel()
    {
        Logger logger = LogManager.getLogger("");
        Method[] methods = Tile.class.getDeclaredMethods();
        for (Method method : methods)
        {
            logger.info("@Test");
            logger.info("Public void test" + LanguageUtility.capitalizeFirst(method.getName()) + "()");
            logger.info("{");
            logger.info("\tFakeWorld world = FakeWorld.newWorld(\"Test" + LanguageUtility.capitalizeFirst(method.getName()) + "\");");
            logger.info("\tworld.setBlock(0, 0, 0, block);");
            logger.info("}");
            logger.info("");
        }
        fail();
    } */

    @Test
    public void testCreation()
    {
        assertNotNull("Test block returned null", getBlock());
        assertTrue("Test block is not an instance of BlockTile", getBlock() instanceof BlockTile);
        assertNotNull("Test block's static tile returned null", ((BlockTile) getBlock()).staticTile);
    }

    public void testPlacement()
    {
        FakeWorld world = FakeWorld.newWorld("TileTest");
        world.setBlock(0, 0, 0, getBlock());

        Block block = world.getBlock(0, 0, 0);
        TileEntity tile = world.getTileEntity(0, 0, 0);

        assertNotNull("Test block failed to place", block);
        assertTrue("Test block did not place as an instance of BlockTile", block instanceof BlockTile);
        assertNotNull("Test block did not place with a tile", tile);
        assertTrue("Test block's tile is not an instance of Tile", tile instanceof Tile);
    }

    public static Block getBlock()
    {
        if (testBlock == null)
        {
            if (Block.getBlockFromName("testTileBlock") == null)
            {
                ModRegistry.registerBlock(new BlockTile(new TileTest(), "JUnit", null), "testTileBlock");
            }
            testBlock = Block.getBlockFromName("testTileBlock");
        }
        return testBlock;
    }

    public static class TileTest extends Tile
    {
        public TileTest()
        {
            super("testTile", Material.rock);
        }

        @Override
        public TileTest newTile()
        {
            return new TileTest();
        }
    }
}
