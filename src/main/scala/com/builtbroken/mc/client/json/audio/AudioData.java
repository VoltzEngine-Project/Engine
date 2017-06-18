package com.builtbroken.mc.client.json.audio;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.builtbroken.mc.lib.json.processors.JsonGenData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.util.ResourceLocation;

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

    public AudioData(IJsonProcessor processor, String key, String soundLocation)
    {
        super(processor);
        this.key = key;
        this.soundLocation = new ResourceLocation(soundLocation);
    }

    public void play(double x, double y, double z, float pitch, float volume)
    {
        if(Minecraft.getMinecraft() != null && Minecraft.getMinecraft().theWorld != null)
        {
            SoundEventAccessorComposite sound = Minecraft.getMinecraft().getSoundHandler().getSound(soundLocation);
            if (sound != null)
            {
                Minecraft.getMinecraft().theWorld.playSound(x, y, z, soundLocation.toString(), pitch, volume, false);
            }
            else
            {
                Engine.logger().error("No sound file for " + soundLocation);
            }
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
