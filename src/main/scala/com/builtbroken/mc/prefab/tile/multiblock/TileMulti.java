package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.lib.transform.region.Cube;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

/**
 * Created by Dark on 8/9/2015.
 */
public class TileMulti extends TileEntity implements IMultiTile, IPacketIDReceiver
{
    private IMultiTileHost host;
    public boolean shouldRenderBlock = false;
    public Cube overrideRenderBounds;
    public Cube collisionBounds;

    public HashMap<ForgeDirection, Block> connectedBlocks = new HashMap();

    @Override
    public IMultiTileHost getHost()
    {
        return host;
    }

    @Override
    public void setHost(IMultiTileHost host)
    {
        this.host = host;
    }

    @Override
    public void invalidate()
    {
        if (host != null)
        {
            host.onTileInvalidate(this);
        }
        super.invalidate();
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if (worldObj.isRemote)
        {
            if (id == 1)
            {
                boolean prev = shouldRenderBlock;
                shouldRenderBlock = buf.readBoolean();
                if (buf.readBoolean())
                    overrideRenderBounds = new Cube(buf);

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
        if (overrideRenderBounds != null)
            return Engine.instance.packetHandler.toMCPacket(new PacketTile(this, 1, shouldRenderBlock, true, overrideRenderBounds));
        else
            return Engine.instance.packetHandler.toMCPacket(new PacketTile(this, 1, shouldRenderBlock, false));
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[ DIM@" + (worldObj != null && worldObj.provider != null ? worldObj.provider.dimensionId : "null") + xCoord + "x " + yCoord + "y " + zCoord + "z " + "]";
    }
}
