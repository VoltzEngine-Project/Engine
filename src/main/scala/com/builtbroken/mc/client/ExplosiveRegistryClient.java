package com.builtbroken.mc.client;

import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.items.explosives.IExplosiveContainerItem;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.state.TextureState;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.framework.json.imp.IJsonKeyDataProvider;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Client side registry for explosive system. Most of the code is designed to minimize the requirements
 * need to implement client side support for explosives. As most explosives just needs a few small icons
 * and don't really need to implement new class files.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2016.
 */
@Deprecated
public class ExplosiveRegistryClient
{
    /** Number of passes to use when rendering explosive icons n = (1 + layers used) */
    public static final int renderPassesForItem = 4;


    public static HashMap<ResourceLocation, EntityList.EntityEggInfo> entityIDToEgg = new HashMap();

    public static void mapEntityIDsToEggs()
    {
        entityIDToEgg.clear();
        Iterator<EntityList.EntityEggInfo> iterator = EntityList.ENTITY_EGGS.values().iterator();

        while (iterator.hasNext())
        {
            EntityList.EntityEggInfo entityegginfo = iterator.next();
            if (entityegginfo != null)
            {
                entityIDToEgg.put(entityegginfo.spawnedID, entityegginfo);
            }
        }
    }

    public static EntityList.EntityEggInfo getEggInfo(int id)
    {
        if (entityIDToEgg.isEmpty())
        {
            mapEntityIDsToEggs();
        }
        return entityIDToEgg.get(id);
    }

    /**
     * Gets the corner icon for the explosive item.
     *
     * @param stack
     * @return item or missing icon
     */
    public static ResourceLocation getCornerIconFor(final ItemStack stack, int pass)
    {
        ItemStack item = stack;

        //Pull explosive from container
        if (item.getItem() instanceof IExplosiveContainerItem)
        {
            item = ((IExplosiveContainerItem) item.getItem()).getExplosiveStack(stack);
        }

        //If item
        if (item != null)
        {
            IExplosiveHandler handler = ExplosiveRegistry.get(item);

            //Try to get icon from JSON
            String contentID = handler.getID();
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(handler.getMod() + ":" + contentID); //TODO consider using state ID to get render
            if (data != null && data.renderType.equalsIgnoreCase("ex"))
            {
                List<String> keys = getStatesForCornerIcon(stack, handler, pass);
                for (String key : keys)
                {
                    IRenderState state = data.getState(key);
                    if (state != null && state instanceof TextureState)
                    {
                        return state.getIcon(0);
                    }
                }
            }
        }
        return null;
    }

    public static int getColorForCornerIcon(final ItemStack stack, int pass)
    {
        ItemStack item = stack;
        if (item.getItem() instanceof IExplosiveContainerItem)
        {
            item = ((IExplosiveContainerItem) item.getItem()).getExplosiveStack(stack);
        }
        if (item != null)
        {
            IExplosiveHandler handler = ExplosiveRegistry.get(item);

            String contentID = handler.getID();
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(handler.getMod() + ":" + contentID); //TODO consider using state ID to get render
            if (data != null && data.renderType.equalsIgnoreCase("ex"))
            {
                List<String> keys = getStatesForCornerIcon(stack, handler, pass);
                for (String key : keys)
                {
                    IRenderState state = data.getState(key);
                    if (state != null && state instanceof TextureState)
                    {
                        String colorKey = ((TextureState) state).color;
                        if (colorKey != null && !colorKey.isEmpty())
                        {
                            if (colorKey.startsWith("data@"))
                            {
                                if (handler instanceof IJsonKeyDataProvider)
                                {
                                    Object object = ((IJsonKeyDataProvider) handler).getJsonKeyData(colorKey.substring(5, colorKey.length()), stack);
                                    if (object instanceof Integer)
                                    {
                                        return (int) object;
                                    }
                                    else if (object instanceof Color)
                                    {
                                        return ((Color) object).getRGB();
                                    }
                                    //TODO more options?
                                }
                                else
                                {
                                    //TODO add warning about missing implementation
                                }
                            }
                            else if (ClientDataHandler.INSTANCE.canSupportColor(colorKey))
                            {
                                return ClientDataHandler.INSTANCE.getColorAsInt(colorKey);
                            }
                            else
                            {
                                //TODO add warning about missing color data
                            }
                        }
                    }
                }
            }
        }
        return 16777215;
    }

    public static List<String> getStatesForCornerIcon(final ItemStack stack, final IExplosiveHandler handler, int pass)
    {
        List<String> keys = new ArrayList();
        String stateOne = handler.getStateID(stack);
        if (stateOne != null && !stateOne.isEmpty())
        {
            keys.add("ex.corner." + handler.getStateID(stack) + "." + pass);
            keys.add("ex.corner." + handler.getStateID(stack));
        }
        keys.add("ex.corner." + pass);
        keys.add("ex.corner");

        return keys;
    }
}
