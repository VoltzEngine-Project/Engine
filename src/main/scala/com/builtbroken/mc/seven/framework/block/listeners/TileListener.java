package com.builtbroken.mc.seven.framework.block.listeners;

import com.builtbroken.mc.api.IModObject;
import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public class TileListener implements ITileEventListener
{
    private IWorld world;
    protected IBlockAccess blockAccess;
    public BlockPos pos;

    @JsonProcessorData("contentUseID")
    protected String contentUseID = null;

    @Override
    public IWorld world()
    {
        return world;
    }

    @Override
    public double x()
    {
        return pos.getX();
    }

    @Override
    public double y()
    {
        return pos.getY();
    }

    @Override
    public double z()
    {
        return pos.getZ();
    }

    @Override
    public int xi()
    {
        return pos.getX();
    }

    @Override
    public int yi()
    {
        return pos.getY();
    }

    @Override
    public int zi()
    {
        return pos.getZ();
    }

    public void inject(IWorld world, BlockPos pos)
    {
        this.world = world;
        this.pos = pos;
    }

    public void inject(IBlockAccess world, BlockPos pos)
    {
        this.blockAccess = world;
        if (world instanceof World)
        {
            this.world = Engine.minecraft.getWorld(((World) world).provider.getDimension());
        }
        else
        {
            this.world = null;
        }
        this.pos = pos;
    }

    protected TileEntity getTileEntity()
    {
        if (world != null)
        {
            return world.unwrap().getTileEntity(pos);
        }
        else if (blockAccess != null)
        {
            return blockAccess.getTileEntity(pos);
        }
        return null;
    }

    protected IBlockState getBlockState()
    {
        return getBlockState(pos);
    }

    protected IBlockState getBlockState(BlockPos pos)
    {
        if (world != null)
        {
            return world.unwrap().getBlockState(pos);
        }
        else if (blockAccess != null)
        {
            return blockAccess.getBlockState(pos);
        }
        return null;
    }

    protected IBlockAccess getBlockAccess()
    {
        return world() != null ? world().unwrap() : blockAccess;
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
            return true;
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
