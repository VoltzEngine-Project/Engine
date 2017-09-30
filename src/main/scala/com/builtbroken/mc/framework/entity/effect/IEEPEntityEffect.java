package com.builtbroken.mc.framework.entity.effect;

import com.builtbroken.mc.core.Engine;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles entity effects for an entity
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/28/2017.
 */
public class IEEPEntityEffect implements IExtendedEntityProperties
{
    public static final String NBT_EFFECTS = "effects";
    public static final String NBT_EFFECT_ID = "effectID";

    /** Map of effects by id */
    protected final Map<String, EntityEffect> effects = new HashMap();

    /** Entity the effect should modify */
    public Entity entity;

    /**
     * Called to add an effect
     *
     * @param entityEffect
     */
    public void addEffect(EntityEffect entityEffect)
    {
        final String id = entityEffect.id.toLowerCase();
        final EntityEffect original = getEffect(id);
        if (original != null)
        {
            original.merge(entityEffect);
        }
        else
        {
            effects.put(id, entityEffect);
        }
    }

    /**
     * Called to get an effect
     *
     * @param id
     * @return
     */
    public EntityEffect getEffect(String id)
    {
        id = id.toLowerCase();
        if (effects.containsKey(id))
        {
            return effects.get(id);
        }
        return null;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        if (!effects.isEmpty())
        {
            final NBTTagList list = new NBTTagList();
            for (EntityEffect entityEffect : effects.values())
            {
                if (entityEffect != null)
                {
                    final NBTTagCompound tag = new NBTTagCompound();

                    //save effect
                    entityEffect.save(tag);

                    //Warning if tag is used
                    if (tag.hasKey(NBT_EFFECT_ID))
                    {
                        Engine.logger().warn("IEEPEntityEffect#saveNBTData(NBT) >> save data for effect '"
                                + entityEffect.id + "' contains tag "
                                + NBT_EFFECT_ID
                                + ", this is used by the save system for the effect's creation ID.");
                    }

                    //Save tag
                    tag.setString(NBT_EFFECT_ID, entityEffect.id);

                    //Append
                    list.appendTag(tag);
                }
            }
            //Only save list if contains data, this way empty list will not be added
            if (list.tagCount() > 0)
            {
                compound.setTag(NBT_EFFECTS, list);
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
        effects.clear();
        if (compound.hasKey(NBT_EFFECTS))
        {
            NBTTagList list = compound.getTagList("effects", 10);
            if (list.tagCount() > 0)
            {
                for (int i = 0; i < list.tagCount(); i++)
                {
                    NBTTagCompound tag = list.getCompoundTagAt(i);
                    String id = tag.getString(NBT_EFFECT_ID);
                    if (id != null)
                    {
                        final EntityEffect entityEffect = EntityEffectHandler.create(id, entity);
                        if (entityEffect != null)
                        {
                            entityEffect.load(tag);
                        }
                        else
                        {
                            //TODO error
                        }
                    }
                }
            }
        }
    }

    @Override
    public void init(Entity entity, World world)
    {
        this.entity = entity;
        for (EntityEffect entityEffect : effects.values())
        {
            if (entityEffect != null)
            {
                entityEffect.init(entity, world);
            }
        }
    }
}
