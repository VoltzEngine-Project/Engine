package com.builtbroken.mc.client.json.render.state;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public class TextureState extends RenderState implements IRenderState
{
    public String textureID;

    public TextureState(String id)
    {
        super(id);
    }

    @Override
    public ResourceLocation getIcon(int side)
    {
        TextureData textureData = getTextureData(side);
        if (textureData != null && textureData.getLocation() != null)
        {
            return textureData.getLocation();
        }
        return null;
    }

    @Override
    public TextureData getTextureData(int side)
    {
        if (textureID != null)
        {
            return ClientDataHandler.INSTANCE.getTexture(textureID);
        }
        return parentState != null ? parentState.getTextureData(side) : null;
    }

    @Override
    public final ImmutableList<ResourceLocation> getTextures()
    {
        ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
        populateTextures(builder);
        return builder.build();
    }

    /**
     * Recursive method to get textures
     * @param builder
     */
    protected void populateTextures(ImmutableList.Builder<ResourceLocation> builder)
    {
        //If recursive method is too deep, switch to iterative
        if (textureID != null)
        {
            TextureData data = ClientDataHandler.INSTANCE.getTexture(textureID);
            if (data != null && data.getLocation() != null)
            {
                builder.add(data.getLocation());
            }
        }
        if (parentState instanceof TextureState)
        {
            ((TextureState) parentState).populateTextures(builder);
        }
    }
}
