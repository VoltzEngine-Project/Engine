package com.builtbroken.mc.client.json.effects;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IEffectData;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/12/2017.
 */
public class EffectList extends JsonGenData implements IEffectData
{
    public final String key;

    private NBTTagCompound nbt;
    private Pos renderOffset = Pos.zero;

    public List<IEffectData> layers = new ArrayList();

    //Cache object used for calculations but keep to save memory and GC time
    private static EulerAngle angle = new EulerAngle(0, 0);

    public EffectList(IJsonProcessor processor, String key)
    {
        super(processor);
        this.key = key.toLowerCase();
    }

    @Override
    public void register()
    {
        ClientDataHandler.INSTANCE.addEffect(key, this);
    }

    @Override
    public String getContentID()
    {
        return key;
    }

    @Override
    public String getUniqueID()
    {
        return key;
    }

    @Override
    public void trigger(World world, double x, double y, double z, double mx, double my, double mz, boolean endPoint, NBTTagCompound nbt)
    {
        for (IEffectData layer : layers)
        {
            NBTTagCompound usedNBT;
            if (nbt != null && !nbt.hasNoTags())
            {
                usedNBT = (NBTTagCompound) nbt.copy();
                //Merges base NBT with server nbt
                if (this.getNbt() != null)
                {
                    for (Object o : getNbt().func_150296_c())
                    {
                        if (o instanceof String)
                        {
                            String key = (String) o;
                            NBTBase tag = getNbt().getTag(key);
                            if (tag != null)
                            {
                                usedNBT.setTag(key, tag);
                            }
                        }
                    }
                }
            }
            else if (this.getNbt() != null)
            {
                usedNBT = nbt;
            }
            else
            {
                usedNBT = new NBTTagCompound();
            }
            Pos renderOffset = this.renderOffset;
            if (renderOffset != Pos.zero && (usedNBT.hasKey("yaw") || usedNBT.hasKey("pitch")))
            {
                float yaw = usedNBT.getFloat("yaw");
                float pitch = usedNBT.getFloat("pitch");
                angle.set(yaw, pitch, 0);
                renderOffset = (Pos) angle.transform(renderOffset);
            }
            layer.trigger(world, x + renderOffset.x(), y + renderOffset.y(), z + renderOffset.z(), mx, my, mz, endPoint, usedNBT);
        }
    }


    public NBTTagCompound getNbt()
    {
        return nbt;
    }

    @JsonProcessorData(value = "additionalEffectData", type = "nbt")
    public void setNbt(NBTTagCompound nbt)
    {
        this.nbt = nbt;
    }

    public Pos getRenderOffset()
    {
        return renderOffset;
    }

    @JsonProcessorData(value = "renderOffset", type = "pos")
    public void setRenderOffset(Pos renderOffset)
    {
        this.renderOffset = renderOffset;
    }

    @Override
    public String toString()
    {
        return "EffectList[ " + key + "]@" + hashCode();
    }
}
