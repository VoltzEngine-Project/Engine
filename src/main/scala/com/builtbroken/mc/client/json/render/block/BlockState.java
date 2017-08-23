package com.builtbroken.mc.client.json.render.block;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.state.RenderState;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public class BlockState extends RenderState implements IRenderState
{
    public final String[] textureID = new String[6];

    public BlockState(String id)
    {
        super(id);
    }

    @Override
    public ResourceLocation getIcon(int side)
    {
        TextureData textureData = getTextureData(side);
        if (textureData != null)
        {
            return textureData.getLocation();
        }
        return null;
    }

    @Override
    public TextureData getTextureData(int side)
    {
        if (side >= 0 && side < 6)
        {
            return textureID[side] != null ? ClientDataHandler.INSTANCE.getTexture(textureID[side]) : null;
        }

        if (parentState != null)
        {
            return parentState.getTextureData(side);
        }
        return getTextureData(0);
    }

    @Override
    public final ImmutableList<ResourceLocation> getTextures()
    {
        ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
        //If recursive method is too deep, switch to iterative
        for (int i = 0; i < 6; i++)
        {
            TextureData data = getTextureData(i);
            if (data != null && data.getLocation() != null)
            {
                builder.add(data.getLocation());
            }
        }
        return builder.build();
    }
}
