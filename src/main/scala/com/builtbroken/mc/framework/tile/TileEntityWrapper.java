package com.builtbroken.mc.framework.tile;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.framework.tile.api.ITileHost;
import com.builtbroken.mc.prefab.tile.entity.TileEntityBase;
import com.builtbroken.mc.prefab.tile.interfaces.tile.ITile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Wrapper object for redirecting calls to the {@Tile} instance
 * <p>
 * Created by Robert(DarkGuardsman) on 4/11/2016
 */
public final class TileEntityWrapper extends TileEntityBase implements ITileHost, IPacketIDReceiver
{
    /** Number of ticks that have passed since this object was created */
    public long ticks = 0L;

    public long lastTick;

    /** Tile object this is wrappered around */
    public ITile tile;

    /** Default method for forge to use when loading this tile */
    public TileEntityWrapper()
    {
    }

    /** Constructor to use when creating new instances of this object */
    public TileEntityWrapper(ITile tile)
    {
        this.tile = tile;
    }

    /**
     * TILE, Called by the world to update the tile. Never
     * call this from your owner code. Use Update() method
     * as this is set final to ensure base functionality.
     */
    @Override
    public final void updateEntity()
    {
        if (tile == null)
        {
            tile = TileFramework.getTileFor(this);
        }
        if (tile != null)
        {
            if (ticks == 0)
            {
                tile.firstTick();
            }
            else
            {
                tile.update(true, ticks, lastTick == 0 ? 1 : (System.currentTimeMillis() - lastTick) / 50);
            }

            //Increase tick
            if (ticks >= Long.MAX_VALUE - 5)
            {
                ticks = 1;
            }
            ticks += 1;
            lastTick = System.currentTimeMillis();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if(tile instanceof ISave)
        {
            ((ISave) tile).load(nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if(tile instanceof ISave)
        {
            ((ISave) tile).save(nbt);
        }
    }

    //==============================
    //==== Location ================
    //==============================
    @Override
    public double z()
    {
        return zCoord;
    }

    public int zi()
    {
        return zCoord;
    }

    @Override
    public double x()
    {
        return xCoord;
    }

    public int xi()
    {
        return xCoord;
    }

    @Override
    public double y()
    {
        return yCoord;
    }

    public int yi()
    {
        return yCoord;
    }

    @Override
    public World world()
    {
        return getWorldObj();
    }

    @Override
    public ITile getTile()
    {
        return getTile();
    }

    @Override
    public boolean isBlock()
    {
        return true;
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if(tile instanceof IPacketIDReceiver)
        {
            return ((IPacketIDReceiver) tile).read(buf, id, player, type);
        }
        return false;
    }
}
