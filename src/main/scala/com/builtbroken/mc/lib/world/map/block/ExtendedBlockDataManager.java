package com.builtbroken.mc.lib.world.map.block;

import com.builtbroken.mc.lib.world.map.data.ChunkMapManager;

/**
 * Handles updating radiation data for the game world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public class ExtendedBlockDataManager extends ChunkMapManager<ExtendedBlockDataMap>
{
    public ExtendedBlockDataManager()
    {
        super("extendedBlockData", "extendedBlockData");
    }

    @Override
    protected ExtendedBlockDataMap createNewMap(int dim)
    {
        return new ExtendedBlockDataMap(this, dim);
    }
}
