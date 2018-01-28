package com.builtbroken.test.testing;

import com.builtbroken.mc.prefab.tile.Tile;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.tile.AbstractTileTest;
import net.minecraft.block.material.Material;
import org.junit.runner.RunWith;

/**
 * JUnit test for {@link Tile}
 * Created by robert on 1/6/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TestTileTestPrefab extends AbstractTileTest<TestTileTestPrefab.TileTestTest>
{
    public TestTileTestPrefab() throws InstantiationException, IllegalAccessException
    {
        super("TestTileTest", TileTestTest.class);
    }

    @Override
    protected TileTestTest newTile()
    {
        return new TileTestTest();
    }

    public static class TileTestTest extends Tile
    {
        public TileTestTest()
        {
            super("TileTestTest", Material.cloth);
            this.textureName = "stone";
        }

        @Override
        public Tile newTile()
        {
            return new TileTestTest();
        }
    }
}
