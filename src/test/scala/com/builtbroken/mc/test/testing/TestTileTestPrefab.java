package com.builtbroken.mc.test.testing;

import com.builtbroken.mc.prefab.tile.BlockTile;
import com.builtbroken.mc.prefab.tile.Tile;
import com.builtbroken.mc.testing.junit.ModRegistry;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.tile.AbstractTileTest;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import org.junit.runner.RunWith;

/**
 * JUnit test for {@link Tile}
 * Created by robert on 1/6/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TestTileTestPrefab extends AbstractTileTest<TestTileTestPrefab.TileTestTest, BlockTile>
{
    public TestTileTestPrefab()
    {
        this.tileClazz = TileTestTest.class;
        Tile tile = new TileTestTest();
        BlockTile block = new BlockTile(tile, "TestTileTestPrefab", CreativeTabs.tabAllSearch);
        tile.setBlock(block);
        this.block = ModRegistry.registerBlock(block, "TestTileTest");
        TileEntity.addMapping(tileClazz, "TestTileTestPrefab");
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
