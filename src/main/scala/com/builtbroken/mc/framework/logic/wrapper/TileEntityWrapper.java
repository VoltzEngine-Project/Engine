package com.builtbroken.mc.framework.logic.wrapper;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.event.tile.TileEvent;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.logic.ITileNode;
import com.builtbroken.mc.framework.logic.imp.ITileDesc;
import com.builtbroken.mc.framework.logic.imp.ITileNodeHost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * Wrapper class for {@link ITileNode}
 * <p>
 * DISCLAIMER: THIS CLASS IS DESIGNED TO BE EXTENDED BY TEMPLATES AND GENERATED CODE. IT IS NOT DESIGNED TO BE
 * EXTENDED BY CONVENTIONAL TILES AND PREFABS. THUS EXTENDING THIS COULD HAVE UNEXPECTED RESULTS AND RANDOM
 * TRANSITIONS IN CODE LOGIC.
 * <p>
 * IN ORDER TO ENSURE INTERACTION WORKS CORRECTLY ALWAYS USE INTERFACES TO ACCESS DATA. AS INTERFACES WILL MORE THAN
 * LIKE MAINTAIN THE SAME LOGIC OVER THE LIFETIME OF THE PROJECT WITH MINIMAL TRANSITION BETWEEN VERSIONS.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/30/2017.
 */
public class TileEntityWrapper extends TileEntity implements ITileNodeHost
{
    /** Object that controls all logic for the tile and some logic of the block */
    protected final ITileNode tile;

    /** TILE, Current tick count, starts when the tile is placed */
    public long ticks = 0L;
    /** TILE, Next tick when cleanup code will be called to check the sanity of the tile */
    protected int nextCleanupTick = 200;

    /** TILE, Set of player's with this tile's interface open, mainly used for GUI packet updates */
    protected final Set<EntityPlayer> playersUsing = new HashSet();

    /**
     * @param controller - tile, passed in by child class wrapper
     */
    public TileEntityWrapper(ITileNode controller)
    {
        tile = controller;
        tile.setHost(this);
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
            try
            {
                tile.firstTick();
            }
            catch (Exception e)
            {
                Engine.logger().error("Unexpected exception while calling first tick on " + tile + "\nWrapper:" + this, e);
            }
        }
        else
        {
            try
            {
                tile.update(ticks);
            }
            catch (Exception e)
            {
                Engine.logger().error("Unexpected exception while ticking " + tile + "\nTick:" + ticks + "\nWrapper:" + this, e);
            }
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
    }

    @Override
    public ITileNode getTileNode()
    {
        return tile;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getClassDisplayName());
        builder.append("(");
        toStringData(builder);
        builder.append(")@");
        builder.append(hashCode());
        return builder.toString();
    }

    /**
     * Gets the debug display name of the class.
     * Normally this is just the class name
     * but is designed to be overridden in
     * the code generator for simplicity
     *
     * @return name
     */
    protected String getClassDisplayName()
    {
        return "TileEntityWrapper";
    }

    /**
     * Called to build data about the class.
     * By default this outputs if the
     * tile is on the server or client,
     * the world,
     * the position,
     * and the tile being hosted
     *
     * @param builder - builder to append data to
     */
    protected void toStringData(StringBuilder builder)
    {
        if(world() != null)
        {
            //Out client or server
            if (world().isRemote)
            {
                builder.append("client, ");
            }
            else
            {
                builder.append("server, ");
            }
        }

        //Out world
        builder.append("world = ");
        if (world() != null)
        {
            if (world().provider != null)
            {
                builder.append("dim@");
                builder.append(world().provider.dimensionId);
                builder.append(", ");
            }
            else
            {
                builder.append(world());
                builder.append(", ");
            }
        }
        else
        {
            builder.append("null, ");
        }
        //Out position
        builder.append(xi());
        builder.append("x, ");

        builder.append(yi());
        builder.append("y, ");

        builder.append(zi());
        builder.append("z, ");

        //Out tile
        builder.append("tile = ");
        builder.append(getTileNode());
    }

    //=============================================
    //========== Position data ====================
    //=============================================

    @Override
    public World world()
    {
        return worldObj;
    }

    @Override
    public double x()
    {
        return xCoord + 0.5;
    }

    @Override
    public double y()
    {
        return yCoord + 0.5;
    }

    @Override
    public double z()
    {
        return zCoord + 0.5;
    }

    @Override
    public int xi()
    {
        return xCoord;
    }

    @Override
    public int yi()
    {
        return yCoord;
    }

    @Override
    public int zi()
    {
        return zCoord;
    }

    @Override
    public float xf()
    {
        return xCoord + 0.5f;
    }

    @Override
    public float yf()
    {
        return yCoord + 0.5f;
    }

    @Override
    public float zf()
    {
        return zCoord + 0.5f;
    }
}
