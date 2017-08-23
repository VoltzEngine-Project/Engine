package com.builtbroken.mc.client.json.imp;

import com.builtbroken.mc.client.json.texture.TextureData;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public interface IRenderState
{

    /**
     * Gets the icon for the side given.
     * <p>
     * For items side is used as the layer
     *
     * @param side - side of the block or layer
     * @return icon, or null if no icon
     */
    ResourceLocation getIcon(int side);

    /**
     * Called to get the texture data for the side given.
     * <p>
     * This is primarily only used to register the texture
     * or do debug actions.
     * <p>
     * For items side is used as the layer
     *
     * @param side - side of the block or layer
     * @return texture data, or null if none
     */
    TextureData getTextureData(int side);

    ImmutableList<ResourceLocation> getTextures();
}
