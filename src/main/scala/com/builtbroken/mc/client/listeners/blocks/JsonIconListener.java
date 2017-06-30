package com.builtbroken.mc.client.listeners.blocks;

import com.builtbroken.mc.api.tile.client.IJsonIconState;
import com.builtbroken.mc.api.tile.listeners.IBlockListener;
import com.builtbroken.mc.api.tile.listeners.ITileEventListener;
import com.builtbroken.mc.api.tile.listeners.ITileEventListenerBuilder;
import com.builtbroken.mc.api.tile.listeners.client.IIconListener;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.prefab.tile.listeners.TileListener;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced version of the built in JSON icon system for blocks. Allows the tile state to modify the content ID or state ID.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/30/2017.
 */
public class JsonIconListener extends TileListener implements IIconListener, IBlockListener
{
    public final BlockBase block;

    public JsonIconListener(BlockBase block)
    {
        this.block = block;
    }

    @Override
    public IIcon getTileIcon(int side, int meta)
    {
        RenderData renderData = block.getRenderData(meta);
        if (renderData != null)
        {
            TileEntity tileEntity = getTileEntity();
            if (tileEntity != null)
            {
                IJsonIconState node = null;
                if (tileEntity instanceof IJsonIconState)
                {
                    node = (IJsonIconState) tileEntity;
                }
                else if (tileEntity instanceof ITileNodeHost && ((ITileNodeHost) tileEntity).getTileNode() instanceof IJsonIconState)
                {
                    node = (IJsonIconState) ((ITileNodeHost) tileEntity).getTileNode();
                }

                if (node != null)
                {
                    String id = node.getContentStateForSide(side, meta);
                    if (id != null && !id.isEmpty())
                    {
                        for (String key : new String[]{"block." + id, "tile." + id,})
                        {
                            IRenderState state = renderData.getState(key);
                            if (state != null && state.getIcon(side) != null)
                            {
                                return state.getIcon(side);
                            }
                        }
                    }
                }
            }
        }
        return block.getIconFromJson(side, meta);
    }

    @Override
    public List<String> getListenerKeys()
    {
        List<String> list = new ArrayList();
        list.add("icon");
        return list;
    }

    public static class Builder implements ITileEventListenerBuilder
    {
        @Override
        public ITileEventListener createListener(Block block)
        {
            if (block instanceof BlockBase)
            {
                return new JsonIconListener((BlockBase) block);
            }
            return null;
        }

        @Override
        public String getListenerKey()
        {
            return "jsonTileIcon";
        }
    }
}
