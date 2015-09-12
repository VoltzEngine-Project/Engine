package com.builtbroken.mc.test.testing;

import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.tile.AbstractTileEntityTest;
import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityFurnace;
import org.junit.runner.RunWith;

/**
 * Test for {@link AbstractTileEntityTest} using {@link TileEntityFurnace} and {@link BlockFurnace} as the input data.
 * Doesn't actually test the functionality of the furnace but just the basic method calls
 * Created by Dark on 9/12/2015.
 */
@RunWith(VoltzTestRunner.class)
public class TileEntityTest extends AbstractTileEntityTest<TileEntityFurnace, BlockFurnace>
{
    public TileEntityTest()
    {
        super((BlockFurnace) Blocks.furnace, TileEntityFurnace.class);
    }

    @Override
    protected TileEntityFurnace newTile()
    {
        return new TileEntityFurnace();
    }
}
