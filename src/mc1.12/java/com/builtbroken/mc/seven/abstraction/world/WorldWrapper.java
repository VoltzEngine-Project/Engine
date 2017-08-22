package com.builtbroken.mc.seven.abstraction.world;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.abstraction.EffectInstance;
import com.builtbroken.mc.api.abstraction.data.ITileData;
import com.builtbroken.mc.api.abstraction.entity.IEntityData;
import com.builtbroken.mc.api.abstraction.tile.ITile;
import com.builtbroken.mc.api.abstraction.tile.ITilePosition;
import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketSpawnParticle;
import com.builtbroken.mc.core.network.packet.callback.PacketAudio;
import com.builtbroken.mc.seven.abstraction.entity.EntityData;
import com.builtbroken.mc.seven.abstraction.tile.TileInstance;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class WorldWrapper implements IWorld
{
    private World world;
    private int dim;

    public WorldWrapper(World world)
    {
        dim = world.provider.getDimension();
        this.world = world;
    }

    public World getWorld()
    {
        return world;
    }

    public boolean isValid()
    {
        return world != null; //TODO add is loaded check
    }

    @Override
    public ITile getTile(int x, int y, int z)
    {
        return getTile(getTilePosition(x, y, z));
    }

    @Override
    public ITile getTile(ITilePosition position)
    {
        if (position instanceof TilePosition)
        {
            return new TileInstance((TilePosition) position);
        }
        else
        {
            return getTile(position.xCoord(), position.yCoord(), position.zCoord());
        }
    }

    @Override
    public boolean setTile(String key, int x, int y, int z)
    {
        return setTile(Engine.minecraft.getTileData(key), x, y, z);
    }

    @Override
    public boolean setTile(ITileData data, int x, int y, int z)
    {
        if (data != null)
        {
            return getWorld().setBlockState(new BlockPos(x, y, z), data.unwrap().getDefaultState());
        }
        return false;
    }

    @Override
    public boolean isLocationLoaded(int x, int y, int z)
    {
        if (getWorld() instanceof WorldServer)
        {
            return ((WorldServer) getWorld()).getChunkProvider().chunkExists(x >> 4, z >> 4) && getWorld().getChunkFromBlockCoords(new BlockPos(x, y, z)).isLoaded();
        }
        return getWorld().getChunkProvider().isChunkGeneratedAt(x >> 4, z >> 4) && getWorld().getChunkFromBlockCoords(new BlockPos(x, y, z)).isLoaded();
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
    public void spawnParticle(EnumParticleTypes type, double x, double y, double z, double xx, double yy, double zz, int... params)
    {
        if (isServer())
        {
            PacketSpawnParticle packet = new PacketSpawnParticle("MC_" + type.getParticleName(), getWorld(), x, y, z, xx, yy, zz);
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

    @Override
    public int getDimID()
    {
        return dim;
    }

    @Override
    public World unwrap()
    {
        return getWorld();
    }
}
