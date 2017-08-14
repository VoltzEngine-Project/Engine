package com.builtbroken.mc.seven.framework.block.listeners;

import com.builtbroken.mc.api.IModObject;
import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public abstract class TileListener implements ITileEventListener
{
    private World world;
    protected IBlockAccess blockAccess;
    private int x, y, z;

    @JsonProcessorData("contentUseID")
    protected String contentUseID = null;

    @JsonProcessorData(value = "metadata", type = "int")
    protected int metaCheck = -1;

    @Override
    public World oldWorld()
    {
        return world;
    }

    @Override
    public double x()
    {
        return x;
    }

    @Override
    public double y()
    {
        return y;
    }

    @Override
    public double z()
    {
        return z;
    }

    @Override
    public int xi()
    {
        return x;
    }

    @Override
    public int yi()
    {
        return y;
    }

    @Override
    public int zi()
    {
        return z;
    }

    public void inject(World world, int x, int y, int z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void inject(IBlockAccess world, int x, int y, int z)
    {
        this.blockAccess = world;
        if (world instanceof World)
        {
            this.world = (World) world;
        }
        else
        {
            this.world = null;
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected TileEntity getTileEntity()
    {
        if (world != null)
        {
            return world.getTileEntity(x, y, z);
        }
        else if (blockAccess != null)
        {
            return blockAccess.getTileEntity(x, y, z);
        }
        return null;
    }

    protected Block getBlock()
    {
        return getBlock(x, y, z);
    }

    protected Block getBlock(int x, int y, int z)
    {
        if (world != null)
        {
            return world.getBlock(x, y, z);
        }
        else if (blockAccess != null)
        {
            return blockAccess.getBlock(x, y, z);
        }
        return null;
    }

    protected int getBlockMeta()
    {
        if (world != null)
        {
            return world.getBlockMetadata(x, y, z);
        }
        else if (blockAccess != null)
        {
            return blockAccess.getBlockMetadata(x, y, z);
        }
        return 0;
    }

    protected boolean setMeta(int meta, int flag)
    {
        if (world != null)
        {
            return world.setBlockMetadataWithNotify(x, y, z, meta, flag);
        }
        return false;
    }

    protected IBlockAccess getBlockAccess()
    {
        return oldWorld() != null ? oldWorld() : blockAccess;
    }

    protected boolean isServer()
    {
        return world != null && !world.isRemote;
    }

    @Override
    public String getListenerKey()
    {
        return "";
    }

    @Override
    public boolean isValidForTile()
    {
        return isValidTileAtLocation();
    }

    /**
     * Separated to allow ignoring normal checks but still having the code
     * to check if needed.
     *
     * @return
     */
    protected boolean isValidTileAtLocation()
    {
        if (isValidForRuntime())
        {
            if (contentUseID != null)
            {
                TileEntity tile = getTileEntity();
                if (tile instanceof IModObject)
                {
                    String id = ((IModObject) tile).uniqueContentID();
                    if (id.equalsIgnoreCase(contentUseID))
                    {
                        return true;
                    }
                }
                else if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() != null)
                {
                    String id = ((ITileNodeHost) tile).getTileNode().uniqueContentID();
                    if (id.equalsIgnoreCase(contentUseID))
                    {
                        return true;
                    }
                }
                return false;
            }
            return metaCheck == -1 || getBlockMeta() == metaCheck;
        }
        return false;
    }

    /**
     * Checks if the listener should function for
     * the runtime conditions. This includes
     * server client checks, configs, etc.
     *
     * @return true if valid
     */
    protected boolean isValidForRuntime()
    {
        return true;
    }
}
