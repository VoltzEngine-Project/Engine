package com.builtbroken.test.prefab.energy;

import com.builtbroken.mc.lib.mod.compat.rf.RFEnergyHandler;
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
import net.minecraftforge.common.util.ForgeDirection;
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
        handler = new RFEnergyHandler(2); //Never change conversion ratio for this test as it will cause all tests to fail in this class
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

    public void testFill()
    {
        world.setBlock(0, 10, 0, block);

        TileTEnergyHandler tile = (TileTEnergyHandler) world.getTileEntity(0, 10, 0);

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            tile.buffer().removeEnergyFromStorage(tile.buffer().getEnergyStored(), true);
            int filled = tile.receiveEnergy(dir, 10, true);
            Assert.assertTrue("Buffer should have returned 10, instead it returned " + filled, filled == 10);
            Assert.assertTrue("Buffer should still be empty as we faked energy transfer, returned " + tile.getEnergyStored(dir), tile.getEnergyStored(dir) == 0);

            filled = tile.receiveEnergy(dir, 10, false);
            Assert.assertTrue("Buffer should have returned 10, instead returned " + filled, filled == 10);
            Assert.assertTrue("Buffer should have 10 energy units stored, stored " + tile.getEnergyStored(dir), tile.getEnergyStored(dir) == 10);

            filled = tile.receiveEnergy(dir, -10, false);
            Assert.assertTrue("Buffer should have returned 0, instead returned" + filled, filled == 0);
            Assert.assertTrue("Buffer should have 10 energy units stored, instead has " + tile.getEnergyStored(dir), tile.getEnergyStored(dir) == 10);
        }

        world.setBlockToAir(0, 10, 0);
    }

    public void testDrain()
    {
        world.setBlock(0, 10, 0, block);

        TileTEnergyHandler tile = (TileTEnergyHandler) world.getTileEntity(0, 10, 0);

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            tile.buffer().addEnergyToStorage(tile.buffer().getMaxBufferSize(), true);
            int drained = tile.extractEnergy(dir, 10, true);
            Assert.assertTrue("Buffer should have returned 10, instead it returned " + drained, drained == 10);
            Assert.assertTrue("Buffer should still be full as we faked energy transfer, returned " + tile.getEnergyStored(dir), tile.getEnergyStored(dir) == 50);

            drained = tile.extractEnergy(dir, 10, false);
            Assert.assertTrue("Buffer should have returned 10, instead returned " + drained, drained == 10);
            Assert.assertTrue("Buffer should have 10 energy units stored, stored " + tile.getEnergyStored(dir), tile.getEnergyStored(dir) == 40);

            drained = tile.extractEnergy(dir, -10, false);
            Assert.assertTrue("Buffer should have returned 0, instead returned" + drained, drained == 0);
            Assert.assertTrue("Buffer should have 10 energy units stored, instead has " + tile.getEnergyStored(dir), tile.getEnergyStored(dir) == 40);
        }

        world.setBlockToAir(0, 10, 0);
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
