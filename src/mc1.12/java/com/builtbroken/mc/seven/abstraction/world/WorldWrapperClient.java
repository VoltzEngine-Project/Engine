package com.builtbroken.mc.seven.abstraction.world;

import com.builtbroken.mc.api.abstraction.EffectInstance;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.audio.AudioData;
import com.builtbroken.mc.core.Engine;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/14/2017.
 */
public class WorldWrapperClient extends WorldWrapper
{
    public WorldWrapperClient(World world)
    {
        super(world);
    }

    @Override
    public void runEffect(EffectInstance effectInstance)
    {

    }

    @Override
    public void spawnParticle(EnumParticleTypes type, double x, double y, double z, double xx, double yy, double zz, int... params)
    {
        getWorld().spawnParticle(type, x, y, z, xx, yy, zz, params);
    }


    @Override
    public void playAudio(String audioKey, double x, double y, double z, float pitch, float volume)
    {
        try
        {
            if (audioKey != null)
            {
                AudioData data = ClientDataHandler.INSTANCE.getAudio(audioKey);
                if (data != null)
                {
                    data.play(x, y, z, pitch, volume);
                }
            }
        }
        catch (Exception e)
        {
            Engine.logger().error("Unexpected error while playing audio from Key[" + audioKey + "]", e);
        }
    }
}
