package com.builtbroken.mc.seven.framework.block.listeners;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.abstraction.entity.IEntityData;
import com.builtbroken.mc.api.data.ActionResponse;
import com.builtbroken.mc.api.tile.access.IRotation;
import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.api.tile.node.ITileNode;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.data.Direction;
import com.builtbroken.mc.framework.block.imp.*;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.multiblock.MultiBlockHelper;
import com.builtbroken.mc.framework.multiblock.structure.MultiBlockLayout;
import com.builtbroken.mc.framework.multiblock.structure.MultiBlockLayoutHandler;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.helper.BlockUtility;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @JsonProcessorData("buildFirstTick")
    protected boolean buildFirstTick = true;

    @JsonProcessorData("directionOffset")
    protected boolean directionOffset = false;

    @JsonProcessorData(value = "directionMultiplier", type = "int")
    protected int directionMultiplier = 1;

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
        long offsetTick = (ticks + (Math.abs(this.xi() + this.yi() + this.zi())));
        if (ticks == 0 && buildFirstTick)
        {
            layoutKey = layoutKey != null ? layoutKey.toLowerCase() : "";
            if (MultiBlockHelper.canBuild(world().unwrap(), getMultiTileHost(), true))
            {
                MultiBlockHelper.buildMultiBlock(world().unwrap(), getMultiTileHost() != null ? getMultiTileHost() : this, true, true);
            }
            else
            {
                Engine.logger().error("Can not build multiblock structure at location " + new Location(world().unwrap(), xi(), yi(), zi()) + " for " + getMultiTileHost());
            }
        }
        else if (offsetTick % 200 == 0)
        {
            MultiBlockHelper.buildMultiBlock(world().unwrap(), getMultiTileHost() != null ? getMultiTileHost() : this, true, true);
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
            Pos pos = new Pos((TileEntity) tileMulti).floor().sub(new Pos(this).floor());
            HashMap<IPos3D, String> map = getLayoutOfMultiBlock();
            if (map != null && map.containsKey(pos))
            {
                _destroyingStructure = true;
                MultiBlockHelper.destroyMultiBlockStructure(getMultiTileHost() != null ? getMultiTileHost() : this, harvest, true, true);
                _destroyingStructure = false;
                return true;
            }
            else if(Engine.runningAsDev)
            {
                System.out.println("Error map was null");
            }
        }
        return false;
    }

    @Override
    public void onTileInvalidate(IMultiTile tileMulti)
    {

    }

    @Override
    public boolean onMultiTileActivated(IMultiTile tile, EntityPlayer player, EnumHand hand, EnumFacing side, float xHit, float yHit, float zHit)
    {
        Object tileEntity = getMultiTileHost();
        if (tileEntity instanceof IActivationListener)
        {
            return ((IActivationListener) tileEntity).onPlayerActivated(player, side, xHit, yHit, zHit);
        }
        return getBlockState().getBlock().onBlockActivated(world().unwrap(), pos, getBlockState(), player, hand, side, xHit, yHit, zHit);
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
            Direction dir = null;
            if (tileEntity instanceof IRotation)
            {
                dir = ((IRotation) tileEntity).getDirection();
            }
            else if (tileEntity instanceof ITileNodeHost && ((ITileNodeHost) tileEntity).getTileNode() instanceof IRotation)
            {
                dir = ((IRotation) ((ITileNodeHost) tileEntity).getTileNode()).getDirection();
            }
            return getLayoutOfMultiBlock(dir);
        }
        return MultiBlockLayoutHandler.get(layoutKey);
    }

    protected HashMap<IPos3D, String> getLayoutOfMultiBlock(Direction dir)
    {
        if (dir != null && dir != Direction.UNKNOWN)
        {
            final String key = layoutKey + "." + dir.name().toLowerCase();
            HashMap<IPos3D, String> directionalMap = MultiBlockLayoutHandler.get(key);
            if (directionalMap == null && directionOffset)
            {
                HashMap<IPos3D, String> map = MultiBlockLayoutHandler.get(layoutKey);
                if (map != null)
                {
                    directionalMap = new HashMap();
                    for (Map.Entry<IPos3D, String> entry : map.entrySet())
                    {
                        if (entry.getKey() != null)
                        {
                            Pos pos = entry.getKey() instanceof Pos ? (Pos) entry.getKey() : new Pos(entry.getKey());
                            pos = pos.add(new Pos(dir).multiply(directionMultiplier));
                            directionalMap.put(pos, entry.getValue());
                        }
                    }
                    MultiBlockLayout layout = new MultiBlockLayout(null, key);
                    layout.tiles.putAll(directionalMap);
                    MultiBlockLayoutHandler.register(layout);
                }
            }
            return directionalMap;
        }
        return MultiBlockLayoutHandler.get(layoutKey);
    }

    @Override
    public void breakBlock(IBlockState state)
    {
        MultiBlockHelper.destroyMultiBlockStructure(getMultiTileHost() != null ? getMultiTileHost() : this, true, true, false);
    }

    @Override
    public boolean removedByPlayer(EntityPlayer player, boolean willHarvest)
    {
        MultiBlockHelper.destroyMultiBlockStructure(getMultiTileHost() != null ? getMultiTileHost() : this, willHarvest, true, true);
        return true;
    }

    @Override
    public ActionResponse canPlaceAt()
    {
        return (doRotation || MultiBlockHelper.canBuild(world().unwrap(), getMultiTileHost() != null ? getMultiTileHost() : this, true)) ? ActionResponse.DO : ActionResponse.CANCEL;
    }

    @Override
    public ActionResponse canPlaceAt(IEntityData entity)
    {
        return (!doRotation ||MultiBlockHelper.canBuild(world().unwrap(), xi(), yi(), zi(), getLayoutOfMultiBlock(BlockUtility.determineDirection(entity)), true)) ? ActionResponse.DO : ActionResponse.CANCEL;
    }

    @Override
    protected boolean isValidForRuntime()
    {
        return isServer();
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
