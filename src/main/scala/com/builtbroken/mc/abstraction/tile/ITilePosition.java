package com.builtbroken.mc.abstraction.tile;

import com.builtbroken.mc.abstraction.entity.IEntityData;
import com.builtbroken.mc.abstraction.world.IWorld;

import java.util.List;

/**
 * Wrapper object for a tile position in the world. Used to access the world with location data
 * for a smoother development process.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public interface ITilePosition
{
    //=====================================================
    //================ Location data ======================
    //=====================================================

    IWorld getWorld();

    int xCoord();

    int yCoord();

    int zCoord();

    //=====================================================
    //====== Helper / Wrapper methods =====================
    //=====================================================

    default ITile getTileData()
    {
        return getWorld().getTile(xCoord(), yCoord(), zCoord());
    }

    default List<IEntityData> getEntitiesInRange(double range)
    {
        return getWorld().getEntitiesInRange(xCoord() + 0.5, yCoord() + 0.5, zCoord() + 0.5, range);
    }

    default ITilePosition getPosition(int x, int y, int z)
    {
        return getWorld().getTilePosition(x, y, z);
    }
}
