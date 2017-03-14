package com.builtbroken.mc.client.json;

import com.builtbroken.mc.client.json.render.RenderData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.IItemRenderer;

import java.util.List;

/**
 * Applied to {@link com.builtbroken.mc.lib.json.imp.IJsonGenObject} or there products that have
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
     * @return name of the object for simplist implementation
     */
    @SideOnly(Side.CLIENT)
    String getRenderContentID(IItemRenderer.ItemRenderType renderType, Object objectBeingRendered);

    /**
     * Overrides the default render state
     *
     * @param renderType          - type of renderer, if the object is a tile
     *                            entity is the same.
     * @param objectBeingRendered - normally an entity, tile, or item
     * @return -1 to use the default, or the ID of the state
     */
    @SideOnly(Side.CLIENT)
    default int getRenderStateID(IItemRenderer.ItemRenderType renderType, Object objectBeingRendered)
    {
        return RenderData.DEFAULT_RENDER;
    }

    /**
     * Gets all content IDs used by this object
     * <p>
     * Used to register renderers.
     *
     * @return
     */
    @SideOnly(Side.CLIENT)
    List<String> getRenderContentIDs();
}
