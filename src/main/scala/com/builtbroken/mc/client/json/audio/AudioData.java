package com.builtbroken.mc.client.json.audio;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

/**
 * Holds audio data and is used to play audio
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class AudioData extends JsonGenData
{
    public final String key;
    public final ResourceLocation soundLocation;
    public SoundCategory category = SoundCategory.NEUTRAL;

    public AudioData(IJsonProcessor processor, String key, String soundLocation)
    {
        super(processor);
        this.key = key;
        this.soundLocation = new ResourceLocation(soundLocation);
    }

    public void play(double x, double y, double z, float pitch, float volume)
    {
        try
        {
            if (soundLocation != null)
            {
                if (Minecraft.getMinecraft() != null)
                {
                    if (Minecraft.getMinecraft().world != null)
                    {
                        SoundEvent sound = SoundEvent.REGISTRY.getObject(soundLocation);
                        if (sound != null)
                        {
                            Minecraft.getMinecraft().world.playSound(x, y, z, sound, category, pitch, volume, false);
                        }
                        else
                        {
                            Engine.logger().debug("AudioData#play() Failed to locate sound instance for " + this, new RuntimeException());
                        }
                    }
                    else if (Engine.runningAsDev)
                    {
                        Engine.logger().debug("AudioData#play() Minecraft client world is not loaded" + this, new RuntimeException());
                    }
                }
                else if (Engine.runningAsDev)
                {
                    Engine.logger().debug("AudioData#play() Minecraft is not loaded" + this, new RuntimeException());
                }
            }
            else if (Engine.runningAsDev)
            {
                Engine.logger().debug("AudioData#play() " + this + " does not have a sound location", new RuntimeException());
            }
        }
        catch (Exception e)
        {
            Engine.logger().error("AudioData#play() : Failed to play sound " + this, e);
        }
    }

    @Override
    public void register()
    {
        ClientDataHandler.INSTANCE.addAudio(key, this);
    }

    @Override
    public String getContentID()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "AudioData[ " + key + ", " + soundLocation + "]@" + hashCode();
    }
}
