package com.builtbroken.mc.client.json;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.IItemRenderer;

import java.util.List;

/**
 * Applied to {@link com.builtbroken.mc.framework.json.imp.IJsonGenObject} or there products that have
 * generated json render state data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/13/2017.
 */
public interface IJsonRenderStateProvider
{
    /**
     * Gets the ID that is used
     * to access the render state
     *
     * @param renderType          - type of renderer, if the object is a tile
     *                            entity is the same.
     * @param objectBeingRendered - normally an entity, tile, or item
     * @return unique render ID of the object
     */
    @SideOnly(Side.CLIENT)
    default String getRenderContentID(IItemRenderer.ItemRenderType renderType, Object objectBeingRendered)
    {
        return getRenderContentID();
    }

    /**
     * Gets the ID that is used
     * to access the render state
     * <p>
     * This is an internal interface call and not externally called
     *
     * @return unique render ID of the object
     */
    @SideOnly(Side.CLIENT)
    default String getRenderContentID()
    {
        return null;
    }

    /**
     * Overrides the default render state
     *
     * @param renderType          - type of renderer, if the object is a tile
     *                            entity is the same.
     * @param key                 - extra key type that was passed in, ex: gun.empty
     * @param objectBeingRendered - normally an entity, tile, or item
     * @return state key for use in rendering
     */
    @SideOnly(Side.CLIENT)
    default String getRenderStateKey(IItemRenderer.ItemRenderType renderType, String key, Object objectBeingRendered)
    {
        return getRenderStateKey(key);
    }

    /**
     * Overrides the default render state
     *
     * @param key - extra key type that was passed in, ex: gun.empty
     * @return state key for use in rendering
     */
    @SideOnly(Side.CLIENT)
    default String getRenderStateKey(String key)
    {
        return null;
    }

    /**
     * Gets all content IDs used by this object
     * <p>
     * Used to register renderers.
     *
     * @return
     */
    @SideOnly(Side.CLIENT)
    default List<String> getRenderContentIDs()
    {
        String id = getRenderContentID();
        if (id != null)
        {
            return Lists.newArrayList(id);
        }
        return null;
    }
}
