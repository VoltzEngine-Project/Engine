package com.builtbroken.mc.seven.framework.logic;

import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.api.data.IPacket;
import com.builtbroken.mc.api.event.tile.TileEvent;
import com.builtbroken.mc.api.tile.ITile;
import com.builtbroken.mc.api.tile.node.ITileNode;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.framework.block.imp.*;
import com.builtbroken.mc.framework.logic.imp.ITileDesc;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.*;

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
public class TileEntityWrapper extends TileEntity implements ITileNodeHost, ITileWithListeners, IPacketIDReceiver, ITile
{
    /** Object that controls all logic for the tile and some logic of the block */
    protected final ITileNode tile;

    /** TILE, Current tick count, starts when the tile is placed */
    public long ticks = 0L;
    /** TILE, Next tick when cleanup code will be called to check the sanity of the tile */
    protected int nextCleanupTick = 200;

    /** TILE, Set of player's with this tile's interface open, mainly used for GUI packet updates */
    protected final Set<EntityPlayer> playersUsing = new HashSet();

    protected final HashMap<String, List<ITileEventListener>> listeners = new HashMap();

    private AxisAlignedBB renderBoundCache;

    private IWorld _worldCache;

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
        //Ticks listeners that require updates
        for (List<ITileEventListener> list : new List[]{getListeners("multiblock"), ((BlockBase) getBlockType()).listeners.get("multiblock")})
        {
            if (list != null && !list.isEmpty())
            {
                for (ITileEventListener listener : list)
                {
                    if (listener instanceof IUpdateListener)
                    {
                        try
                        {
                            if (listener instanceof IBlockListener)
                            {
                                ((IBlockListener) listener).inject(world(), xi(), yi(), zi());
                            }
                            ((IUpdateListener) listener).update(ticks);
                        }
                        catch (Exception e)
                        {
                            Engine.logger().error("Unexpected exception while calling first tick on " + tile + "\nWrapper:" + this, e);
                        }
                    }
                }
            }
        }

        //Ticks node
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
    public boolean canUpdate()
    {
        return getTileNode().requiresPerTickUpdate();
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
    public boolean isHostValid()
    {
        return !isInvalid();
    }


    //=============================================
    //================= data ======================
    //=============================================

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if (getTileNode() instanceof IPacketIDReceiver)
        {
            return ((IPacketIDReceiver) getTileNode()).read(buf, id, player, type);
        }
        return false;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        if (tile instanceof ITileDesc)
        {
            return ((ITileDesc) tile).canHandlePackets() ? Engine.packetHandler.toMCPacket(((ITileDesc) tile).getDescPacket()) : null;
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        tile.load(nbt.hasKey("tileData") ? nbt.getCompoundTag("tileData") : nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setTag("tileData", tile.save(new NBTTagCompound()));
    }

    @Override
    public PacketTile getPacketForData(Object... data)
    {
        return new PacketTile(this, data);
    }

    @Override
    public void sendPacketToClient(IPacket packet, double range)
    {

    }

    @Override
    public void sendPacketToServer(IPacket packet)
    {

    }

    //=============================================
    //============== wrapper calls ================
    //=============================================

    @Override
    public ITileNode getTileNode()
    {
        return tile;
    }

    @Override
    public BlockBase getHostBlock()
    {
        return (BlockBase) getBlockType();
    }

    @Override
    public int getHostMeta()
    {
        return getBlockMetadata();
    }

    @Override
    public boolean canAccessWorld()
    {
        return true;
    }

    @Override
    public List<ITileEventListener> getListeners(String key)
    {
        if (listeners.containsKey(key))
        {
            return listeners.get(key);
        }
        return null;
    }

    /**
     * Called to add a listener
     *
     * @param listener
     */
    public void addListener(ITileEventListener listener)
    {
        List<String> keys = listener.getListenerKeys();
        if (keys != null && !keys.isEmpty())
        {
            for (String key : keys)
            {
                List<ITileEventListener> list = listeners.get(key);
                if (list == null)
                {
                    list = new ArrayList();
                }
                list.add(listener);
                listeners.put(key, list);
            }
        }
    }

    @Override
    public String uniqueContentID()
    {
        return getTileNode().uniqueContentID();
    }

    @Override
    public String contentType()
    {
        return getTileNode().contentType();
    }

    @Override
    public String modID()
    {
        return getTileNode().modID();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        for (List<ITileEventListener> list : getMultiTileListeners("renderBounds"))
        {
            if (list != null && !list.isEmpty())
            {
                for (ITileEventListener listener : list)
                {
                    if (listener instanceof IRenderBoundsListener)
                    {
                        if (listener instanceof IBlockListener)
                        {
                            ((IBlockListener) listener).inject(world(), xi(), yi(), zi());
                        }
                        if (listener.isValidForTile())
                        {
                            final Cube cube = ((IRenderBoundsListener) listener).getRenderBounds();
                            if (cube != null && cube.isValid())
                            {
                                return cube.clone().add(xi(), yi(), zi()).toAABB();
                            }
                        }
                    }
                }
            }
        }
        if (renderBoundCache == null)
        {
            renderBoundCache = getHostBlock().data.getRenderBounds().clone().add(xi(), yi(), zi()).toAABB();
        }
        return renderBoundCache;
    }

    /**
     * Gets list of listeners that apply to this block
     *
     * @param key - listener key
     * @return list of lists
     */
    protected List[] getMultiTileListeners(String key)
    {
        if (!(getBlockType() instanceof BlockBase))
        {
            return new List[]{getListeners(key)};
        }
        return new List[]{getListeners(key), ((BlockBase) getBlockType()).listeners.get(key)};
    }

    //=============================================
    //============== to string ====================
    //=============================================

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
        if (world() != null)
        {
            //Out client or server
            if (world().isClient())
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
            builder.append("dim@");
            builder.append(world().getDimID());
            builder.append(", ");
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
    public IWorld world()
    {
        if(_worldCache == null)
        {
            _worldCache = Engine.minecraft.getWorld(worldObj.provider.dimensionId);
        }
        return _worldCache;
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
