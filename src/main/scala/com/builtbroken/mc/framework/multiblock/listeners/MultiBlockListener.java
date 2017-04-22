package com.builtbroken.mc.framework.multiblock.listeners;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.tile.access.IRotation;
import com.builtbroken.mc.api.tile.listeners.*;
import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.api.tile.node.ITileNode;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.multiblock.MultiBlockHelper;
import com.builtbroken.mc.framework.multiblock.structure.MultiBlockLayoutHandler;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.json.loading.JsonProcessorData;
import com.builtbroken.mc.prefab.tile.listeners.TileListener;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/17/2017.
 */
public class MultiBlockListener extends TileListener implements IBlockListener, IPlacementListener, IDestroyedListener, IUpdateListener, IMultiTileHost
{
    private boolean _destroyingStructure = false;

    @JsonProcessorData("layoutKey")
    protected String layoutKey;

    @JsonProcessorData("doRotation")
    protected boolean doRotation = false;

    @Override
    public List<String> getListenerKeys()
    {
        List<String> list = new ArrayList();
        list.add("placement");
        list.add("break");
        list.add("break");
        list.add("update");
        list.add("multiblock");
        return list;
    }

    @Override
    public void update(long ticks)
    {
        if (ticks == 0)
        {
            layoutKey = layoutKey != null ? layoutKey.toLowerCase() : "";
            if (MultiBlockHelper.canBuild(world(), getMultiTileHost(), true))
            {
                MultiBlockHelper.buildMultiBlock(world(), getMultiTileHost(), true, true);
            }
            else
            {
                Engine.logger().error("Can not build multiblock structure at location " + new Location(this) + " for " + getMultiTileHost());
            }
        }
    }

    protected IMultiTileHost getMultiTileHost()
    {
        TileEntity tileEntity = getTileEntity();

        if (tileEntity instanceof IMultiTileHost)
        {
            return (IMultiTileHost) tileEntity;
        }
        else if (tileEntity instanceof ITileNodeHost)
        {
            ITileNode node = ((ITileNodeHost) tileEntity).getTileNode();
            if (node instanceof IMultiTileHost)
            {
                return (IMultiTileHost) node;
            }
        }
        return null;
    }

    @Override
    public void onMultiTileAdded(IMultiTile tileMulti)
    {
        if (tileMulti instanceof TileEntity)
        {
            if (getLayoutOfMultiBlock().containsKey(new Pos(this).sub(new Pos((TileEntity) tileMulti))))
            {
                tileMulti.setHost(getMultiTileHost());
            }
        }
    }

    @Override
    public boolean onMultiTileBroken(IMultiTile tileMulti, Object source, boolean harvest)
    {
        if (!_destroyingStructure && tileMulti instanceof TileEntity)
        {
            Pos pos = new Pos((TileEntity) tileMulti).sub(new Pos(this));

            if (getLayoutOfMultiBlock().containsKey(pos))
            {
                MultiBlockHelper.destroyMultiBlockStructure(getMultiTileHost(), harvest, true, true);
                _destroyingStructure = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTileInvalidate(IMultiTile tileMulti)
    {

    }

    @Override
    public boolean onMultiTileActivated(IMultiTile tile, EntityPlayer player, int side, IPos3D hit)
    {
        Object tileEntity = getMultiTileHost();
        if (tileEntity instanceof IActivationListener)
        {
            return ((IActivationListener) tileEntity).onPlayerActivated(player, side, hit.xf(), hit.yf(), hit.zf());
        }
        return false;
    }

    @Override
    public void onMultiTileClicked(IMultiTile tile, EntityPlayer player)
    {
        Object tileEntity = getMultiTileHost();
        if (tileEntity instanceof IActivationListener)
        {
            ((IActivationListener) tileEntity).onPlayerClicked(player);
        }
    }

    @Override
    public HashMap<IPos3D, String> getLayoutOfMultiBlock()
    {
        if (doRotation)
        {
            TileEntity tileEntity = getTileEntity();
            ForgeDirection dir = null;
            if (tileEntity instanceof IRotation)
            {
                dir = ((IRotation) tileEntity).getDirection();
            }
            else if (tileEntity instanceof ITileNodeHost && ((ITileNodeHost) tileEntity).getTileNode() instanceof IRotation)
            {
                dir = ((IRotation) ((ITileNodeHost) tileEntity).getTileNode()).getDirection();
            }

            if (dir != null && dir != ForgeDirection.UNKNOWN)
            {
                return MultiBlockLayoutHandler.get(layoutKey + "." + dir.name().toLowerCase());
            }
        }
        return MultiBlockLayoutHandler.get(layoutKey);
    }

    @Override
    public void breakBlock(Block block, int meta)
    {
        MultiBlockHelper.destroyMultiBlockStructure(getMultiTileHost(), true, true, false);
    }

    @Override
    public boolean removedByPlayer(EntityPlayer player, boolean willHarvest)
    {
        MultiBlockHelper.destroyMultiBlockStructure(getMultiTileHost(), willHarvest, true, true);
        return true;
    }

    @Override
    public boolean canPlaceAt()
    {
        return MultiBlockHelper.canBuild(world(), getMultiTileHost(), true);
    }

    public static class Builder implements ITileEventListenerBuilder
    {
        @Override
        public ITileEventListener createListener(Block block)
        {
            return new MultiBlockListener();
        }

        @Override
        public String getListenerKey()
        {
            return "multiblock";
        }
    }
}
