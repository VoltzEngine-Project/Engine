package com.builtbroken.mc.client;

import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.explosive.ITexturedExplosiveHandler;
import com.builtbroken.mc.api.items.explosives.IExplosiveContainerItem;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.state.TextureState;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.framework.json.imp.IJsonKeyDataProvider;
import com.builtbroken.mc.lib.data.item.ItemStackWrapper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Client side registry for explosive system. Most of the code is designed to minimize the requirements
 * need to implement client side support for explosives. As most explosives just needs a few small icons
 * and don't really need to implement new class files.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2016.
 */
public class ExplosiveRegistryClient
{
    /** Number of passes to use when rendering explosive icons n = (1 + layers used) */
    public static final int renderPassesForItem = 4;

    /** Map of explosive items to corner icons, icons are item icons */
    public static final HashMap<ItemStackWrapper, IIcon> EX_CORNER_ICONS = new HashMap();

    /** Map of explosive items to resource locations for registering icons, resources must be item icons */
    public static final HashMap<ItemStackWrapper, String> EX_CORNER_RESOURCES = new HashMap();

    @SideOnly(Side.CLIENT)
    public static IIcon missing_corner_icon;

    public static HashMap<Integer, EntityList.EntityEggInfo> entityIDToEgg = new HashMap();

    public static void mapEntityIDsToEggs()
    {
        entityIDToEgg.clear();
        Iterator iterator = EntityList.entityEggs.values().iterator();

        while (iterator.hasNext())
        {
            EntityList.EntityEggInfo entityegginfo = (EntityList.EntityEggInfo) iterator.next();
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
     * Gets the corner icon for the explosive item. Defaults
     * to using {@link ITexturedExplosiveHandler} if the explosive
     * handler implements the interface. If it doesn't then
     * the map will be searched for the item.
     *
     * @param stack
     * @return item or missing icon
     */
    public static IIcon getCornerIconFor(final ItemStack stack, int pass)
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
            if (handler != null)
            {
                //Old outdated system TODO remove once converted to JSON fully
                if (handler instanceof ITexturedExplosiveHandler)
                {
                    IIcon icon = ((ITexturedExplosiveHandler) handler).getBottomLeftCornerIcon(item, pass);
                    if (icon != null)
                    {
                        return icon;
                    }
                }

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

            //Cache
            ItemStackWrapper wrapper = new ItemStackWrapper(item);
            if (EX_CORNER_ICONS.containsKey(wrapper))
            {
                return EX_CORNER_ICONS.get(wrapper);
            }
        }
        return missing_corner_icon;
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
            if (handler != null)
            {
                //Old system TODO remove once fully JSON
                if (handler instanceof ITexturedExplosiveHandler)
                {
                    return ((ITexturedExplosiveHandler) handler).getBottomLeftCornerIconColor(item, pass);
                }

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

    /**
     * Called to register a resource location for an icon to load
     *
     * @param item     - explosive item
     * @param resource - domain:textureName
     */
    public static void registerIcon(ItemStack item, String resource)
    {
        ItemStackWrapper wrapper = new ItemStackWrapper(item);
        if (!EX_CORNER_RESOURCES.containsKey(wrapper))
        {
            EX_CORNER_RESOURCES.put(wrapper, resource);
        }
        else
        {
            Engine.logger().error("Something attempt to register an existing resource location for loading ex icons", new RuntimeException());
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0)
        {
            for (IExplosiveHandler handler : ExplosiveRegistry.getExplosives())
            {
                if (handler instanceof ITexturedExplosiveHandler)
                {
                    ((ITexturedExplosiveHandler) handler).registerExplosiveHandlerIcons(event.map, true);
                }
            }
        }
        else if (event.map.getTextureType() == 1)
        {
            for (IExplosiveHandler handler : ExplosiveRegistry.getExplosives())
            {
                if (handler instanceof ITexturedExplosiveHandler)
                {
                    ((ITexturedExplosiveHandler) handler).registerExplosiveHandlerIcons(event.map, false);
                }
            }
            EX_CORNER_ICONS.clear();
            for (Map.Entry<ItemStackWrapper, String> entry : EX_CORNER_RESOURCES.entrySet())
            {
                EX_CORNER_ICONS.put(entry.getKey(), event.map.registerIcon(entry.getValue()));
            }
            missing_corner_icon = event.map.registerIcon(References.PREFIX + "ex.icon.missing");
        }
    }
}
