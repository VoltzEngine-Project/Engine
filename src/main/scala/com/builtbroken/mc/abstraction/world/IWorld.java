package com.builtbroken.mc.abstraction.world;

import com.builtbroken.mc.abstraction.entity.IEntityData;
import com.builtbroken.mc.abstraction.tile.ITileData;
import com.builtbroken.mc.abstraction.tile.ITilePosition;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public interface IWorld
{
    /**
     * Get information about a tile from the location
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    ITileData getTileData(int x, int y, int z);

    /**
     * Get data about entities near a position
     *
     * @param range - distance to check
     * @return list of entities, or empty list
     */
    List<IEntityData> getEntitiesInRange(double x, double y, double z, double range);

    /**
     * Gets a new tile position near the location
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    ITilePosition getTilePosition(int x, int y, int z);
}
