package com.builtbroken.mc.framework.logic;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.event.tile.TileEvent;
import com.builtbroken.mc.core.Engine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Wrapper class for {@link ITileController}
 * <p>
 * Keep in mind this is designed to be a template for automatic code generation to extend. It should not be
 * used as an actual parent class unless testing.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/30/2017.
 */
public abstract class TileEntityWrapper extends TileEntity
{
    public static final HashMap<String, Class<? extends ITileController>> nameToClass = new HashMap();

    public final ITileController tile;

    /** TILE, Current tick count, starts when the tile is placed */
    public long ticks = 0L;
    /** TILE, Next tick when cleanup code will be called to check the sanity of the tile */
    protected int nextCleanupTick = 200;

    /** TILE, Set of player's with this tile's interface open, mainly used for GUI packet updates */
    protected final Set<EntityPlayer> playersUsing = new HashSet();

    public TileEntityWrapper(ITileController controller)
    {
        tile = controller;
    }

    /**
     * TILE, Called by the world to update the tile. Never
     * call this from your owner code. Use Update() method
     * as this is set final to ensure base functionality.
     */
    @Override
    public final void updateEntity()
    {
        if (ticks == 0)
        {
            tile.firstTick();
        }
        else
        {
            tile.update(ticks);
        }

        //Increase tick
        if (ticks >= Long.MAX_VALUE)
        {
            ticks = 0;
        }
        ticks += 1;
        if (ticks % nextCleanupTick == 0)
        {
            tile.doCleanupCheck();
            nextCleanupTick = tile.getNextCleanupTick();
        }
        if (playersUsing.size() > 0)
        {
            //tile.doUpdateGuiUsers();
        }

    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        this.tileEntityInvalid = true;
        if (!worldObj.isRemote)
        {
            TileEvent.onUnLoad(this);
        }
        tile.destroy();
    }

    @Override
    public void validate()
    {
        super.validate();
        this.tileEntityInvalid = false;
        if (!worldObj.isRemote)
        {
            TileEvent.onLoad(this);
        }
    }


    @Override
    public Packet getDescriptionPacket()
    {
        if (tile instanceof ITileDesc)
        {
            return ((ITileDesc) tile).canHandlePackets() ? Engine.instance.packetHandler.toMCPacket(((ITileDesc) tile).getDescPacket()) : null;
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (tile instanceof ISave)
        {
            ((ISave) tile).load(nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (tile instanceof ISave)
        {
            ((ISave) tile).save(nbt);
        }
        nbt.setString("tileID", tile.getUniqueID());
    }
}
