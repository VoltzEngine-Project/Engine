package com.builtbroken.mc.client.json.render.tile;

import com.builtbroken.mc.api.tile.listeners.ITileEventListener;
import com.builtbroken.mc.api.tile.listeners.client.ITileRenderListener;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IModelState;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.prefab.tile.listeners.ListenerIterator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public class TileRenderHandler extends TileEntitySpecialRenderer
{
    private static Map<Class, String> classToNameMap = new HashMap();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        RenderData data = getRenderData(tile);
        if (data != null && data.renderType.equalsIgnoreCase("tile"))
        {
            IRenderState state = null;

            //Try to get tile specific state
            String key = getRenderStateKey(tile);
            if (key != null)
            {
                state = data.getState(key);
                if (!(state instanceof IModelState) || ((IModelState) state).getModel() == null)
                {
                    state = null;
                }
            }

            //Get default(s)
            if (state == null)
            {
                for (String de : new String[]{"tile", "entity", "item.entity"})
                {
                    state = data.getState(de);
                    if (!(state instanceof IModelState) || ((IModelState) state).getModel() == null)
                    {
                        state = null;
                    }
                    else
                    {
                        break;
                    }
                }
            }

            if (state instanceof IModelState)
            {
                //float scale = 100; //0.0254f;//0.0255f;
                //GL11.glScalef(scale, scale, scale);
                ((IModelState) state).render();
            }
        }
        GL11.glPopMatrix();

        //If block base iterate listeners
        if (tile.getBlockType() instanceof BlockBase)
        {
            ListenerIterator it = new ListenerIterator(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, (BlockBase) tile.getBlockType(), "tilerender");
            while (it.hasNext())
            {
                ITileEventListener next = it.next();
                if (next instanceof ITileRenderListener)
                {
                    ((ITileRenderListener) next).renderDynamic(tile, x, y, z, f);
                }
            }
        }
    }

    protected RenderData getRenderData(TileEntity tile)
    {
        if (classToNameMap.isEmpty())
        {
            try
            {
                Class clazz = TileEntity.class;
                Field field = clazz.getDeclaredField("classToNameMap");
                field.setAccessible(true);
                HashMap map = (HashMap) field.get(null);

                Set<Map.Entry> set = map.entrySet();
                for (Map.Entry entry : set)
                {
                    classToNameMap.put((Class) entry.getKey(), (String) entry.getValue());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        String id = classToNameMap.get(tile.getClass());
        if (id == null)
        {
            id = tile.getClass().getName();
        }
        return ClientDataHandler.INSTANCE.getRenderData(id);
    }

    protected String getRenderStateKey(TileEntity tile)
    {
        return "tile." + tile.getBlockMetadata();
    }
}
