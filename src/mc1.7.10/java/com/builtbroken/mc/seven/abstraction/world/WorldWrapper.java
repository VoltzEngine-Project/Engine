package com.builtbroken.mc.seven.abstraction.world;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.abstraction.EffectInstance;
import com.builtbroken.mc.abstraction.entity.IEntityData;
import com.builtbroken.mc.abstraction.tile.ITileData;
import com.builtbroken.mc.abstraction.tile.ITilePosition;
import com.builtbroken.mc.abstraction.world.IWorld;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketSpawnParticle;
import com.builtbroken.mc.core.network.packet.callback.PacketAudio;
import com.builtbroken.mc.seven.abstraction.entity.EntityData;
import com.builtbroken.mc.seven.abstraction.tile.TileData;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public EffectInstance newEffect(String key, IPos3D pos)
    {
        return new EffectInstance(this, key, pos);
    }

    @Override
    public void runEffect(EffectInstance effectInstance)
    {
        if (isServer())
        {
            PacketSpawnParticle packet = new PacketSpawnParticle("JSON_" + effectInstance.key,
                    dim,
                    effectInstance.x, effectInstance.y, effectInstance.z,
                    effectInstance.mx, effectInstance.my, effectInstance.mz);
            if (effectInstance.data != null && !effectInstance.data.isEmpty())
            {
                packet.otherData = new NBTTagCompound();

                //TODO maybe move to helper
                for (Map.Entry<String, Object> entry : effectInstance.data.entrySet())
                {
                    Object value = entry.getValue();
                    if (value instanceof Integer)
                    {
                        packet.otherData.setInteger(entry.getKey(), (int) value);
                    }
                    else if (value instanceof Float)
                    {
                        packet.otherData.setFloat(entry.getKey(), (float) value);
                    }
                    else if (value instanceof String)
                    {
                        packet.otherData.setString(entry.getKey(), (String) value);
                    }
                    else if (value instanceof Double)
                    {
                        packet.otherData.setDouble(entry.getKey(), (double) value);
                    }
                    //TODO add more types
                }
            }
            packet.endPoint = effectInstance.endPoint;
            Engine.packetHandler.sendToAllAround(packet, getWorld(), effectInstance.x, effectInstance.y, effectInstance.z, 100); //TODO be selective about players (use player settings to reduce bandwidth usage)
        }
    }

    @Override
    public void spawnParticle(String name, double x, double y, double z, double xx, double yy, double zz)
    {
        if (isServer())
        {
            PacketSpawnParticle packet = new PacketSpawnParticle(name, getWorld(), x, y, z, xx, yy, zz);
            Engine.packetHandler.sendToAllAround(packet, getWorld(), x, y, z, 100); //TODO be selective about players (use player settings to reduce bandwidth usage)
        }
    }

    @Override
    public void playAudio(String audioKey, double x, double y, double z, float pitch, float volume)
    {
        if (isServer())
        {
            PacketAudio packetAudio = new PacketAudio(getWorld(), audioKey, x, y, z, pitch, volume);
            Engine.packetHandler.sendToAllAround(packetAudio, getWorld(), x, y, z, 100);
        }
    }

    public boolean isClient()
    {
        return getWorld().isRemote;
    }

    public boolean isServer()
    {
        return !getWorld().isRemote;
    }
}
