package com.builtbroken.mc.client;

import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.explosive.ITexturedExplosiveHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.world.explosive.ExplosiveRegistry;
import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.HashMap;
import java.util.Map;

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
    /** Map of explosive items to corner icons, icons are item icons */
    public static final HashMap<ItemStackWrapper, IIcon> EX_CORNER_ICONS = new HashMap();

    /** Map of explosive items to resource locations for registering icons, resources must be item icons */
    public static final HashMap<ItemStackWrapper, String> EX_CORNER_RESOURCES = new HashMap();

    /**
     * Gets the corner icon for the explosive item. Defaults
     * to using {@link ITexturedExplosiveHandler} if the explosive
     * handler implementes the interface. If it doesn't then
     * the map will be searched for the item.
     *
     * @param item
     * @return item or null if nothing was registered
     */
    public static IIcon getCornerIconFor(ItemStack item)
    {
        IExplosiveHandler handler = ExplosiveRegistry.get(item);
        if (handler instanceof ITexturedExplosiveHandler)
        {
            IIcon icon = ((ITexturedExplosiveHandler) handler).getBottomLeftCornerIcon(item);
            if (icon != null)
            {
                return icon;
            }
        }
        ItemStackWrapper wrapper = new ItemStackWrapper(item);
        if (EX_CORNER_ICONS.containsKey(wrapper))
        {
            return EX_CORNER_ICONS.get(wrapper);
        }
        return null;
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
        }
    }
}
