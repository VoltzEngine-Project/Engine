package com.builtbroken.mc.client.json.render;

import com.builtbroken.mc.client.SharedAssets;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.models.ModelData;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.builtbroken.mc.lib.transform.rotation.EulerAngle;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

/**
 * Render/Texture/Animation states used for rendering models in the game
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/22/2016.
 */
public class RenderState
{
    public String textureID;

    public String modelID;
    public String[] parts;
    public Pos offset;
    public EulerAngle rotation;

    public RenderState(String textureID)
    {
        this.textureID = textureID;
    }

    public RenderState(String modelID, String textureID, Pos offset, EulerAngle rotation)
    {
        this(textureID);
        this.modelID = modelID;
        this.offset = offset;
        this.rotation = rotation;
    }

    public IIcon getIcon()
    {
        TextureData textureData = getTextureData();
        if (textureData != null && textureData.getIcon() != null)
        {
            return textureData.getIcon();
        }
        return null;
    }

    public TextureData getTextureData()
    {
        return textureID != null ? ClientDataHandler.INSTANCE.getTexture(textureID) : null;
    }

    /**
     * Check if the render state can do
     * an in world render. Not all
     * render state are actually used
     * for rendering
     *
     * @return
     */
    public boolean isModelRenderer()
    {
        return modelID != null;
    }

    public boolean render()
    {
        if (isModelRenderer())
        {
            TextureData textureData = ClientDataHandler.INSTANCE.getTexture(textureID);
            ModelData data = ClientDataHandler.INSTANCE.getModel(modelID);
            if (data != null && data.getModel() != null)
            {
                GL11.glPushMatrix();
                if (textureData != null)
                {
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(textureData.getLocation());
                }
                else
                {
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(SharedAssets.GREY_TEXTURE);
                }
                if (rotation != null)
                {
                    //TODO test to make sure this is correct
                    GL11.glRotated(rotation.yaw(), 0, 1, 0);
                    GL11.glRotated(rotation.pitch(), 1, 0, 0);
                    GL11.glRotated(rotation.roll(), 0, 0, 1);
                }
                if (offset != null)
                {
                    GL11.glTranslated(offset.x(), offset.y(), offset.z());
                }
                data.render(parts);
                GL11.glPopMatrix();
                return true;
            }
        }
        return false;
    }
}
