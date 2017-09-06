package com.builtbroken.mc.client.json.render.state;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.texture.TextureData;
import net.minecraft.util.IIcon;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public class TextureState extends RenderState implements IRenderState
{
    private String textureID;

    public TextureState(String id)
    {
        super(id);
    }

    @Override
    public IIcon getIcon(int side)
    {
        TextureData textureData = getTextureData(side);
        if (textureData != null && textureData.getIcon() != null)
        {
            return textureData.getIcon();
        }
        return null;
    }

    @Override
    public TextureData getTextureData(int side)
    {
        if (getTextureID() != null)
        {
            return ClientDataHandler.INSTANCE.getTexture(getTextureID());
        }
        return parentState != null ? parentState.getTextureData(side) : null;
    }

    public String getTextureID()
    {
        return textureID;
    }

    public void setTextureID(String textureID)
    {
        this.textureID = textureID;
    }
}
