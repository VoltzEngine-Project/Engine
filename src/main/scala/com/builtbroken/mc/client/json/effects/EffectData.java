package com.builtbroken.mc.client.json.effects;

import com.builtbroken.mc.client.effects.VisualEffectProvider;
import com.builtbroken.mc.client.effects.VisualEffectRegistry;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.builtbroken.mc.lib.json.processors.JsonGenData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Holds audio data and is used to play audio
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class EffectData extends JsonGenData
{
    public final String key;
    public final String effectKey;
    public final NBTTagCompound nbt;

    public boolean useEndPointForVelocity = false;

    public EffectData(IJsonProcessor processor, String key, String effectKey, NBTTagCompound nbt)
    {
        super(processor);
        this.key = key.toLowerCase();
        this.effectKey = effectKey.toLowerCase();
        this.nbt = nbt;
    }

    @Override
    public void register()
    {
        ClientDataHandler.INSTANCE.addEffect(key, this);
    }

    public void trigger(World world, double x, double y, double z, double mx, double my, double mz, boolean endPoint)
    {
        trigger(world, x, y, z, mx, my, mz, endPoint, nbt);
    }

    public void trigger(World world, double x, double y, double z, double mx, double my, double mz, boolean endPoint, NBTTagCompound nbt)
    {
        VisualEffectProvider provider = VisualEffectRegistry.main.get(effectKey);
        if (provider != null)
        {
            NBTTagCompound usedNBT;
            if (nbt != null && !nbt.hasNoTags())
            {
                usedNBT = nbt;
            }
            else if (this.nbt != null)
            {
                usedNBT = nbt;
            }
            else
            {
                usedNBT = new NBTTagCompound();
            }
            provider.displayEffect(world, x, y, z, mx, my, mz, endPoint, usedNBT);
        }
        else
        {
            Engine.logger().error("Failed to find a visual effect provider for key '" + effectKey + "'");
        }
    }

    @Override
    public String toString()
    {
        return "EffectData[ " + key + ", " + effectKey + "]@" + hashCode();
    }
}
