package com.builtbroken.mc.lib.world.map.block;

import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.lib.world.map.data.ChunkMapManager;
import net.minecraft.world.World;

/**
 * Handles updating radiation data for the game world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public class ExtendedBlockDataManager extends ChunkMapManager<ExtendedBlockDataMap>
{
    public static final ExtendedBlockDataManager INSTANCE = new ExtendedBlockDataManager();

    protected ExtendedBlockDataManager()
    {
        super("extendedBlockData", "extendedBlockData");
        SaveManager.registerClass("extendedBlockDataMap", ExtendedBlockDataMap.class);
    }

    @Override
    protected ExtendedBlockDataMap createNewMap(int dim)
    {
        return new ExtendedBlockDataMap(this, dim);
    }

    /**
     * Gets the stored value at the location in the world
     *
     * @param x - location in the world
     * @param y - location in the world
     * @param z - location in the world
     * @return value shorted, do not assume 0 means a stored value
     */
    public short getValue(World world, int x, int y, int z)
    {
        ExtendedBlockDataMap map = getMap(world, false);
        if (map != null)
        {
            return map.getValue(x, y, z);
        }
        return 0;
    }

    public short setValue(World world, int x, int y, int z, int value)
    {
        return setValue(world, x, y, z, (short) value);
    }

    /**
     * Sets the extended data value
     *
     * @param x     - location in the world
     * @param y     - location in the world
     * @param z     - location in the world
     * @param value - new data
     * @return old data
     */
    public short setValue(World world, int x, int y, int z, short value)
    {
        ExtendedBlockDataMap map = getMap(world, true);
        if (map != null)
        {
            return map.setValue(x, y, z, value);
        }
        return 0;
    }
}
