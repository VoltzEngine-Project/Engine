package com.builtbroken.mc.framework.multiblock;

import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.api.tile.provider.ITankProvider;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.handler.TileTaskTickHandler;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidTank;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Basic implementation of a multi block. Is extended to provide more complex implementation as needed.
 * <p>
 * Includes handling for rendering a standard block, faking rendering of another block, or render a complex cube.
 * <p>
 * Has call backs for several functions, including icons and interaction.
 * <p>
 * Tracks connected blocks and will auto decay if host is removed incorrectly.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/9/2015.
 */
public class TileMulti extends TileEntity implements IMultiTile, IPacketIDReceiver
{
    private WeakReference<IMultiTileHost> hostWeakReference;

    /** Enables default rendering of the block, overridden by {@link #renderID} and {@link #renderState} */
    public boolean shouldRenderBlock = false;

    //Box data
    public Cube overrideRenderBounds;
    public Cube collisionBounds;

    /** Render ID to use to pull RenderData from {@link com.builtbroken.mc.client.json.ClientDataHandler#getRenderData(String)} */
    protected String renderID;
    /** Render state to pull from RenderData */
    protected String renderState;
    /** Block ID to render as an imitation (e.g. minecraft:sand) */
    protected String blockIDToFake;
    /** Actual block instance of {@link #blockToFake} to fake rendering */
    protected Block blockToFake;

    public int ticks = 0;

    /** Map of connected tiles */
    public HashMap<ForgeDirection, Block> connectedBlocks = new HashMap();

    public void setRenderState(String id, String state)
    {
        renderID = id;
        renderState = state;
        Engine.packetHandler.sendToAllAround(getDescPacket(), this); //TODO reduce packet to math changed data
    }

    public void setBlockToFake(Block block)
    {
        blockIDToFake = block == null ? null : InventoryUtility.getRegistryName(block);
        blockToFake = block;
        Engine.packetHandler.sendToAllAround(getDescPacket(), this); //TODO reduce packet to math changed data
    }

    public void setBlockToFake(String id)
    {
        blockToFake = null;
        blockIDToFake = id;
        Engine.packetHandler.sendToAllAround(getDescPacket(), this); //TODO reduce packet to math changed data
    }

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

    public Block getBlockToRender()
    {
        if (blockToFake == null && blockIDToFake != null)
        {
            blockToFake = InventoryUtility.getBlock(blockIDToFake);
        }
        return blockToFake;
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
            Engine.packetHandler.sendToAllAround(getDescPacket(), this);
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
            Pos pos = new Pos((TileEntity) this).add(dir);
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

                shouldRenderBlock = buf.readBoolean();

                //Load render data
                String render = ByteBufUtils.readUTF8String(buf);
                String state = ByteBufUtils.readUTF8String(buf);
                String blockRenderID = ByteBufUtils.readUTF8String(buf);

                renderID = render.isEmpty() || render.equalsIgnoreCase("-") ? null : render;
                renderState = state.isEmpty() || state.equalsIgnoreCase("-") ? null : state;
                blockIDToFake = blockRenderID.isEmpty() || blockRenderID.equalsIgnoreCase("-") ? null : blockRenderID;

                worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
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

        String render = renderID != null && !renderID.isEmpty() ? renderID : "-";
        String state = renderState != null && !renderState.isEmpty() ? renderState : "-";
        String renderBlockID = blockIDToFake != null && !blockIDToFake.isEmpty() ? blockIDToFake : "-";

        return new PacketTile(this, 1, pos, shouldRenderBlock, render, state, renderBlockID);
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[ DIM@" + (worldObj != null && worldObj.provider != null ? worldObj.provider.dimensionId + " " : "null ") + xCoord + "x " + yCoord + "y " + zCoord + "z " + "]@" + hashCode();
    }

    public IFluidTank getTank(Fluid fluid)
    {
        IMultiTileHost host = getHost();
        if (host != null)
        {
            if (host instanceof ITileNodeHost && ((ITileNodeHost) host).getTileNode() instanceof ITankProvider)
            {
                return ((ITankProvider) ((ITileNodeHost) host).getTileNode()).getTankForFluid(fluid);
            }
            else if (host instanceof ITankProvider)
            {
                return ((ITankProvider) host).getTankForFluid(fluid);
            }
        }
        return null;
    }

    @Override
    public World oldWorld()
    {
        return worldObj;
    }

    @Override
    public double z()
    {
        return zCoord;
    }

    @Override
    public double x()
    {
        return xCoord;
    }

    @Override
    public double y()
    {
        return yCoord;
    }
}
