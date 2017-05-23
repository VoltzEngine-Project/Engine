package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataManager;
import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Modified version of placement data to work with {@link ExtendedBlockDataManager}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/23/2017.
 */
public class PlacementDataExtended extends PlacementData
{
    public final short extraData;

    public PlacementDataExtended(Block block, int meta, int extraData)
    {
        this(block, meta, (short) extraData);
    }

    public PlacementDataExtended(Block block, int meta, short extraData)
    {
        super(block, meta);
        this.extraData = extraData;
    }

    @Override
    protected boolean doPlace(final World world, int x, int y, int z)
    {
        if (world.setBlock(x, y, z, block(), meta(), 2))
        {
            ExtendedBlockDataManager.INSTANCE.setValue(world, x, y, z, extraData);
            return true;
        }
        return false;
    }
}
