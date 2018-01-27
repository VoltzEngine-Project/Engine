package com.builtbroken.mc.framework.multiblock;

import com.builtbroken.mc.api.data.IPacket;
import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.handler.TileTaskTickHandler;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.data.Direction;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.imp.transform.vector.Pos;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Basic implementation of a multi block
 * Created by Dark on 8/9/2015.
 */
public class TileMulti extends TileEntity implements IMultiTile, IPacketIDReceiver
{
    private WeakReference<IMultiTileHost> hostWeakReference;
    public boolean shouldRenderBlock = false;
    public Cube overrideRenderBounds;
    public Cube collisionBounds;

    public int ticks = 0;

    public HashMap<Direction, Block> connectedBlocks = new HashMap();

    @Override
    public IMultiTileHost getHost()
    {
        return hostWeakReference != null ? hostWeakReference.get() : null;
    }

    @Override
    public void setHost(IMultiTileHost host)
    {
        this.hostWeakReference = new WeakReference(host);
        if (host == null && world != null && !world.loadedTileEntityList.contains(this))
        {
            world.addTileEntity(this);
        }
        if (!world.isRemote)
        {
            Engine.packetHandler.sendToAllAround(getDescPacket(), this);
        }
    }

    @Override
    public void invalidate()
    {
        if (getHost() != null)
        {
            getHost().onTileInvalidate(this);
        }
        super.invalidate();
    }

    public void update() //TODO trigger from tick handler
    {
        if (ticks == 0)
        {
            updateConnections();
        }
        if (!world.isRemote && ticks % 20 == 0)
        {
            if (getHost() == null)
            {
                world.setBlockToAir(pos);
            }
            else if (world.loadedTileEntityList.contains(this))
            {
                TileTaskTickHandler.INSTANCE.addTileToBeRemoved(this);
            }
        }
        ticks++;
    }

    public void updateConnections()
    {
        for (Direction dir : Direction.DIRECTIONS)
        {
            Pos pos = new Pos(this).add(dir);
            Block b = pos.getBlock(world);
            if (connectedBlocks.containsKey(dir))
            {
                //TODO notify that a block has changed
                if (connectedBlocks.get(dir) != b)
                {
                    connectedBlocks.remove(dir);
                }
            }
            if (!pos.isAirBlock(world))
            {
                connectedBlocks.put(dir, b);
            }
        }
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, IPacket type)
    {
        if (world.isRemote)
        {
            if (id == 1)
            {
                //Update host data for client use
                Pos pos = new Pos(buf);
                if (pos.isZero())
                {
                    this.setHost(null);
                }
                else
                {
                    TileEntity tile = pos.getTileEntity(world);
                    if (tile instanceof IMultiTileHost)
                    {
                        this.setHost((IMultiTileHost) tile);
                    }
                }
                //Update should render
                boolean prev = shouldRenderBlock;
                shouldRenderBlock = buf.readBoolean();

                //Update render bounds
                if (buf.readBoolean())
                {
                    overrideRenderBounds = new Cube(buf);
                }
                else
                {
                    overrideRenderBounds = new Cube(0, 0, 0, 1, 1, 1);
                }

                if (prev != shouldRenderBlock)
                {
                    world.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(), getPos().getY(), getPos().getZ());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return Engine.packetHandler.toMCPacket(getDescPacket());
    }

    public PacketTile getDescPacket()
    {
        Pos pos = getHost() != null ? new Pos(getHost()) : new Pos();
        if (overrideRenderBounds != null)
        {
            return new PacketTile("desc", 1, this).addData(pos, shouldRenderBlock, true, overrideRenderBounds);
        }
        else
        {
            return new PacketTile("desc2", 1, this).addData(pos, shouldRenderBlock, false);
        }
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[ DIM@" + (world != null && world.provider != null ? world.provider.getDimension() + " " : "null ") + getPos().getX() + "x " + getPos().getY() + "y " + getPos().getZ() + "z " + "]@" + hashCode();
    }
}
