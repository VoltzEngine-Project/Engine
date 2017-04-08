package com.builtbroken.mc.client.json;

import com.builtbroken.mc.client.json.audio.AudioData;
import com.builtbroken.mc.client.json.effects.EffectData;
import com.builtbroken.mc.client.json.models.ModelData;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.builtbroken.mc.core.Engine;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.HashMap;

/**
 * Handles all data for client side JSON
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/22/2016.
 */
public class ClientDataHandler
{
    /** Model key to model data */
    public HashMap<String, ModelData> models = new HashMap();
    /** Texture key to texture data */
    public HashMap<String, TextureData> textures = new HashMap();
    /** Render key to render data */
    public HashMap<String, RenderData> renderData = new HashMap();
    /** Audio key to audio data */
    public HashMap<String, AudioData> audioData = new HashMap();
    /** Effect key to effect data */
    public HashMap<String, EffectData> effectData = new HashMap();

    /** Global client data handler for Voltz Engine */
    public static final ClientDataHandler INSTANCE = new ClientDataHandler();

    public void addTexture(String key, TextureData texture)
    {
        if (textures.containsKey(key))
        {
            Engine.logger().error("Overriding " + textures.get(key) + " with " + texture);
        }
        textures.put(key, texture);
    }

    public void addModel(String key, ModelData model)
    {
        if (models.containsKey(key))
        {
            Engine.logger().error("Overriding " + models.get(key) + " with " + model);
        }
        models.put(key, model);
    }

    public void addRenderData(String key, RenderData data)
    {
        if (renderData.containsKey(key))
        {
            Engine.logger().error("Overriding " + renderData.get(key) + " with " + data);
        }
        renderData.put(key, data);
    }

    public void addAudio(String key, AudioData data)
    {
        if (audioData.containsKey(key))
        {
            Engine.logger().error("Overriding " + audioData.get(key) + " with " + data);
        }
        audioData.put(key, data);
    }


    public void addEffect(String key, EffectData data)
    {
        if (effectData.containsKey(key))
        {
            Engine.logger().error("Overriding " + audioData.get(key) + " with " + data);
        }
        effectData.put(key, data);
    }

    public RenderData getRenderData(String key)
    {
        if (key == null || key.isEmpty())
        {
            return null;
        }
        return renderData.get(key);
    }

    public ModelData getModel(String key)
    {
        if (key == null || key.isEmpty())
        {
            return null;
        }
        return models.get(key);
    }

    public TextureData getTexture(String key)
    {
        if (key == null || key.isEmpty())
        {
            return null;
        }
        return textures.get(key);
    }

    public AudioData getAudio(String key)
    {
        if (key == null || key.isEmpty())
        {
            return null;
        }
        return audioData.get(key);
    }

    public EffectData getEffect(String key)
    {
        if (key == null || key.isEmpty())
        {
            return null;
        }
        return effectData.get(key);
    }

    @SubscribeEvent
    public void textureEvent(TextureStitchEvent.Pre event)
    {
        /** 0 = terrain.png, 1 = items.png */
        final int textureType = event.map.getTextureType();
        for (TextureData data : textures.values())
        {
            if (textureType == 0 && data.type == TextureData.Type.BLOCK)
            {
                data.register(event.map);
            }
            else if (textureType == 1 && data.type == TextureData.Type.ITEM)
            {
                data.register(event.map);
            }
        }
    }
}
