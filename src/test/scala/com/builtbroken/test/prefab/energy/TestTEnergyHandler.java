package com.builtbroken.test.prefab.energy;

import com.builtbroken.mc.mods.rf.RFEnergyHandler;
import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.ModRegistry;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * Created by Dark on 8/15/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TestTEnergyHandler extends AbstractTest
{
    RFEnergyHandler handler;
    FakeWorld world;
    Block block;

    @Override
    public void setUpForEntireClass()
    {
        world = FakeWorld.newWorld("TEnergyHandlerTest");
        block = new BlockTEnergyHandler();
        ModRegistry.registerBlock(block, "test:TEnergyHandler");
        GameRegistry.registerTileEntity(TileTEnergyHandler.class, "test:TEnergyHandler");
        handler = RFEnergyHandler.INSTANCE; //Never change conversion ratio for this test as it will cause all tests to fail in this class
    }

    public void testPlacement()
    {
        world.setBlock(0, 10, 0, block);
        Assert.assertTrue("Block should have been placed", world.getBlock(0, 10, 0) != null || world.getBlock(0, 10, 0) != Blocks.air);
        Assert.assertTrue("Block should be our block", world.getBlock(0, 10, 0) == block);

        Assert.assertTrue("There should be a tile", world.getTileEntity(0, 10, 0) != null);
        Assert.assertTrue("Tile should be our block", world.getTileEntity(0, 10, 0) instanceof TileTEnergyHandler);

        world.setBlockToAir(0, 10, 0);
        Assert.assertTrue("Block should be air", world.getBlock(0, 10, 0) == Blocks.air);
        Assert.assertTrue("There should be a null", world.getTileEntity(0, 10, 0) == null);
    }

    public class BlockTEnergyHandler extends BlockContainer
    {
        protected BlockTEnergyHandler()
        {
            super(Material.rock);
        }

        @Override
        public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
        {
            return new TileTEnergyHandler();
        }
    }
}
