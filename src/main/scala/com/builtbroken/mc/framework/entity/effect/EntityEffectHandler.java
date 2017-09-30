package com.builtbroken.mc.framework.entity.effect;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.entity.effect.effects.BleedingEffect;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Handles all effects applied to entities. Mainly used for creating effects and adding properties to entity.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2017.
 */
public class EntityEffectHandler
{
    public static final String ENTITY_EXTENDED_PROPERTY_IDENTIFIER = References.PREFIX + "effecthandler";

    public static Map<Integer, IEEPEntityEffect> entityToHandler = new HashMap();
    public static Map<String, Function<Entity, EntityEffect>> effectCreators = new HashMap();

    public EntityEffectHandler INSTANCE = new EntityEffectHandler();

    //Effects created by VE
    public static final String BLEEDING_EFFECT = "bleeding";

    private EntityEffectHandler()
    {
        addEffectCreator(BLEEDING_EFFECT, e -> new BleedingEffect(e, 0, 0));
    }

    /**
     * Adds a lambda function to allow creating an effect without reflection
     *
     * @param id       - id to reference, case insensitive. Make sure to prefix with mod ID to prevent overlap
     * @param function - lambda to create an EntityEffect for the given entity
     */
    public static void addEffectCreator(String id, Function<Entity, EntityEffect> function)
    {
        effectCreators.put(id.toLowerCase(), function);
    }

    /**
     * Called to apply an effect to an entity
     *
     * @param entity       - entity to apply on
     * @param entityEffect - effect to apply
     */
    public static void applyEffect(Entity entity, EntityEffect entityEffect)
    {
        if (entity != null && entityEffect != null)
        {
            int id = entity.getEntityId();
            IEEPEntityEffect property = entityToHandler.get(id);
            if (property != null)
            {
                property.addEffect(entityEffect);
            }
        }
    }

    /**
     * Creates an entity effect for the given entity
     *
     * @param id     - unique key to locate the effect
     * @param entity - entity to create the effect for
     * @return effect, or null if not found
     */
    public static EntityEffect create(String id, Entity entity)
    {
        if (effectCreators.containsKey(id.toLowerCase()))
        {
            return effectCreators.get(id.toLowerCase()).apply(entity);
        }
        return null;
    }

    @SubscribeEvent
    public void onEntityCreated(EntityEvent.EntityConstructing event)
    {
        Entity entity = event.entity;
        int id = entity.getEntityId();

        IEEPEntityEffect effectProperty = new IEEPEntityEffect();
        entity.registerExtendedProperties(ENTITY_EXTENDED_PROPERTY_IDENTIFIER, effectProperty);

        entityToHandler.put(id, effectProperty);
    }
}
