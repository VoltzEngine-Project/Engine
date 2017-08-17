package com.builtbroken.mc.framework.multiblock;

import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.handler.TileTaskTickHandler;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.imp.transform.vector.Pos;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

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

    public HashMap<ForgeDirection, Block> connectedBlocks = new HashMap();

    @Override
    public IMultiTileHost getHost()
    {
        return hostWeakReference != null ? hostWeakReference.get() : null;
    }

    @Override
    public boolean canUpdate()
    {
        return DimensionManager.getWorld(0) != null;
    }

    @Override
    public void setHost(IMultiTileHost host)
    {
        this.hostWeakReference = new WeakReference(host);
        if (host == null && worldObj != null && !worldObj.loadedTileEntityList.contains(this))
        {
            worldObj.addTileEntity(this);
        }
        if (!worldObj.isRemote)
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

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (ticks == 0)
        {
            updateConnections();
        }
        if (!worldObj.isRemote && ticks % 20 == 0)
        {
            if (getHost() == null)
            {
                getWorldObj().setBlockToAir(xCoord, yCoord, zCoord);
            }
            else if (getWorldObj().loadedTileEntityList.contains(this))
            {
                TileTaskTickHandler.INSTANCE.addTileToBeRemoved(this);
            }
        }
        ticks++;
    }

    public void updateConnections()
    {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Pos pos = new Pos(this).add(dir);
            Block b = pos.getBlock(getWorldObj());
            if (connectedBlocks.containsKey(dir))
            {
                //TODO notify that a block has changed
                if (connectedBlocks.get(dir) != b)
                {
                    connectedBlocks.remove(dir);
                }
            }
            if (b != null && !b.isAir(getWorldObj(), xCoord, yCoord, zCoord))
            {
                connectedBlocks.put(dir, b);
            }
        }
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if (worldObj.isRemote)
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
                    TileEntity tile = pos.getTileEntity(worldObj);
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
                    worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return Engine.packetHandler.toMCPacket(getDescPacket());
    }

    public PacketTile getDescPacket()
    {
        Pos pos = getHost() != null ? new Pos(getHost()) : new Pos();
        if (overrideRenderBounds != null)
        {
            return new PacketTile(this, 1, pos, shouldRenderBlock, true, overrideRenderBounds);
        }
        else
        {
            return new PacketTile(this, 1, pos, shouldRenderBlock, false);
        }
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[ DIM@" + (worldObj != null && worldObj.provider != null ? worldObj.provider.dimensionId + " " : "null ") + xCoord + "x " + yCoord + "y " + zCoord + "z " + "]@" + hashCode();
    }
}
