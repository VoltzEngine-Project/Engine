package com.builtbroken.mc.seven.abstraction.world;

import com.builtbroken.mc.abstraction.entity.IEntityData;
import com.builtbroken.mc.abstraction.tile.ITileData;
import com.builtbroken.mc.abstraction.tile.ITilePosition;
import com.builtbroken.mc.abstraction.world.IWorld;
import com.builtbroken.mc.seven.abstraction.entity.EntityData;
import com.builtbroken.mc.seven.abstraction.tile.TileData;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class WorldWrapper implements IWorld
{
    private WeakReference<World> _worldCache;
    private int dim;

    public WorldWrapper(World world)
    {
        dim = world.provider.dimensionId;
        _worldCache = new WeakReference<World>(world);
    }

    public World getWorld()
    {
        World world = _worldCache != null ? _worldCache.get() : null;

        if (world == null)
        {
            world = DimensionManager.getWorld(dim);
            if (world != null)
            {
                _worldCache = new WeakReference<World>(world);
            }
            else if (_worldCache != null)
            {
                _worldCache.clear();
                _worldCache = null;
            }
        }
        return world;
    }

    public boolean isValid()
    {
        return _worldCache != null && getWorld() != null;
    }

    @Override
    public ITileData getTileData(int x, int y, int z)
    {
        return getTileData(getTilePosition(x, y, z));
    }

    @Override
    public ITileData getTileData(ITilePosition position)
    {
        if (position instanceof TilePosition)
        {
            return new TileData((TilePosition) position);
        }
        else
        {
            return getTileData(position.xCoord(), position.yCoord(), position.zCoord());
        }
    }

    @Override
    public List<IEntityData> getEntitiesInRange(double x, double y, double z, double range)
    {
        return new ArrayList();
    }

    @Override
    public ITilePosition getTilePosition(int x, int y, int z)
    {
        return new TilePosition(this, x, y, z);
    }

    @Override
    public IEntityData getEntityData(int id)
    {
        Entity entity = getWorld().getEntityByID(id);
        if (entity != null)
        {
            return new EntityData(entity);
        }
        return null;
    }
}
