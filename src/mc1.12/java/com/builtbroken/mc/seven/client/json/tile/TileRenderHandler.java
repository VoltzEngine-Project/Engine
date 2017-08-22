package com.builtbroken.mc.seven.client.json.tile;

import com.builtbroken.mc.api.tile.access.IRotation;
import com.builtbroken.mc.api.tile.node.ITileNode;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IModelState;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.data.Direction;
import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import com.builtbroken.mc.lib.helper.ReflectionUtility;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import com.builtbroken.mc.seven.framework.block.listeners.ListenerIterator;
import com.builtbroken.mc.seven.framework.block.listeners.client.ITileRenderListener;
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
public class TileRenderHandler extends TileEntitySpecialRenderer<TileEntity>
{
    private static Map<Class, String> classToNameMap = new HashMap();

    @Override
    public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GL11.glPushMatrix();
        try
        {
            GL11.glTranslated(x + 0.5, y, z + 0.5);
            RenderData data = getRenderData(tile);
            if (data != null && data.renderType.equalsIgnoreCase("tile"))
            {
                //Try to get tile specific state
                String key = getRenderStateKey(tile);

                //Loop default keys
                for (String de : new String[]{key, "tile", "entity", "item.entity"})
                {
                    if (de != null)
                    {
                        IRenderState state = data.getState(de);
                        if (state instanceof IModelState && ((IModelState) state).render(false))
                        {
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Engine.logger().error("TileRenderHandler: Error rendering " + tile, e);
        }
        GL11.glPopMatrix();

        //If BlockBase, iterate listeners
        if (tile.getBlockType() instanceof BlockBase)
        {
            ListenerIterator it = new ListenerIterator(tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), (BlockBase) tile.getBlockType(), "tilerender");
            while (it.hasNext())
            {
                ITileEventListener next = it.next();
                if (next instanceof ITileRenderListener)
                {
                    GL11.glPushMatrix();
                    try
                    {
                        ((ITileRenderListener) next).renderDynamic(tile, x, y, z, partialTicks);
                    }
                    catch (Exception e)
                    {
                        Engine.logger().error("TileRenderHandler: Error calling listener[" + next + "] for  Tile[" + tile + "]", e);
                    }
                    GL11.glPopMatrix();
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
                Field field = ReflectionUtility.getMCField(TileEntity.class, "classToNameMap", "field_145853_j");
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

        Class clazz = tile.getClass();
        String id = classToNameMap.get(clazz);
        if (id == null)
        {
            id = tile.getClass().getName();
        }
        return ClientDataHandler.INSTANCE.getRenderData(id);
    }

    protected String getRenderStateKey(TileEntity tile)
    {
        if (tile instanceof IRotation && ((IRotation) tile).getDirection() != Direction.UNKNOWN && ((IRotation) tile).getDirection() != null)
        {
            return "tile." + ((IRotation) tile).getDirection().name().toLowerCase();
        }
        else if (tile instanceof ITileNodeHost)
        {
            ITileNode node = ((ITileNodeHost) tile).getTileNode();
            if (node instanceof IRotation && ((IRotation) node).getDirection() != Direction.UNKNOWN && ((IRotation) node).getDirection() != null)
            {
                return "tile." + ((IRotation) node).getDirection().name().toLowerCase();
            }
        }
        return "tile." + tile.getBlockMetadata();
    }
}
