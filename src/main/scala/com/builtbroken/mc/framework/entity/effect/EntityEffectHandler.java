package com.builtbroken.mc.framework.entity.effect;

import com.builtbroken.jlib.debug.DebugPrinter;
import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Handles all effects applied to entities. Mainly used for creating effects and adding properties to entity.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2017.
 */
public final class EntityEffectHandler
{
    /** Key used to register and get the effect property on an entity {@link Entity#getExtendedProperties(String)} */
    public static final String ENTITY_EXTENDED_PROPERTY_IDENTIFIER = References.PREFIX + "effecthandler";

    //Effects created by VE
    public static final String BLEEDING_EFFECT = "bleeding";

    /** Instance of the handler, only used for event handling */
    public static final EntityEffectHandler INSTANCE = new EntityEffectHandler();

    /** Enabled console debug */
    public static boolean doDebug = false;

    //map of effect id to its creation function
    private static Map<String, Function<Entity, EntityEffect>> effectCreators = new HashMap();

    //Console debug printer
    private DebugPrinter debugPrinter;

    private EntityEffectHandler()
    {
        //do nothing
        debugPrinter = new DebugPrinter(Engine.logger());
    }

    /**
     * Adds a lambda function to allow creating an effect without reflection
     *
     * @param id       - id to reference, case insensitive. Make sure to prefix with mod ID to prevent overlap
     * @param function - lambda to create an EntityEffect for the given entity
     */
    public static void addEffectCreator(String id, Function<Entity, EntityEffect> function)
    {
        if (id != null && function != null)
        {
            effectCreators.put(id.toLowerCase(), function);
        }
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
            IExtendedEntityProperties prop = entity.getExtendedProperties(ENTITY_EXTENDED_PROPERTY_IDENTIFIER);
            if (!(prop instanceof IEEPEntityEffect))
            {
                prop = createEffectProperty(entity);
            }

            //Apply effect
            if (prop instanceof IEEPEntityEffect)
            {
                prop.init(entity, entity.worldObj);
                ((IEEPEntityEffect) prop).addEffect(entityEffect);
            }
        }
    }

    /**
     * Creates an entity effect for the given entity
     *
     * @param id     - unique key to locate the effect
     * @param entity - entity to create the effect for, can be null
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

    protected static IEEPEntityEffect createEffectProperty(Entity entity)
    {
        if (entity != null && entity.worldObj != null)
        {
            //Create property object
            IEEPEntityEffect effectProperty = new IEEPEntityEffect();
            entity.registerExtendedProperties(ENTITY_EXTENDED_PROPERTY_IDENTIFIER, effectProperty);
            effectProperty.init(entity, entity.worldObj);
            return effectProperty;
        }
        return null;
    }

    //@SubscribeEvent
    public void onEntityCreated(EntityEvent.EntityConstructing event)
    {
        createEffectProperty(event.entity);
    }

    //@SubscribeEvent //TODO find a better way to trigger updates
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;
        if (event.phase == TickEvent.Phase.END)
        {
            debugPrinter.start("EntityEffectHandler", "onWorldTick(" + world.provider.dimensionId + ")", doDebug);
            try
            {
                long time = System.nanoTime();

                //Copy list to prevent concurrent mod errors
                final List loadedEntityList = new ArrayList();
                loadedEntityList.addAll(world.loadedEntityList);

                //Debug time taken to clone list
                time = System.nanoTime() - time;
                debugPrinter.log("copy list time: " + StringHelpers.formatNanoTime(time));
                time = System.nanoTime();

                //Loop entities, keep index loop to prevent concurrent modification exceptions from being thrown
                for (int i = 0; i < loadedEntityList.size(); i++)
                {
                    //Validate content
                    final Object object = loadedEntityList.get(i);
                    if (object instanceof Entity)
                    {
                        //Get property
                        IExtendedEntityProperties prop = ((Entity) object).getExtendedProperties(ENTITY_EXTENDED_PROPERTY_IDENTIFIER);
                        if (prop instanceof IEEPEntityEffect)
                        {
                            try
                            {
                                //Tick property to update effects
                                ((IEEPEntityEffect) prop).onWorldTick();
                            }
                            catch (Exception e)
                            {
                                Engine.logger().error("EntityEffectHandler#onWorldTick("
                                        + event.world.provider.dimensionId
                                        + ") >> unexpected error updating "
                                        + prop
                                        + " for "
                                        + object, e);
                            }
                        }
                    }
                }

                //Debug time taken to update all entities
                time = System.nanoTime() - time;
                debugPrinter.log("Entities: " + loadedEntityList.size());
                debugPrinter.log("RunTime: " + StringHelpers.formatNanoTime(time));
            }
            catch (Exception e)
            {
                Engine.logger().error("EntityEffectHandler#onWorldTick(" + event.world.provider.dimensionId + ") >> unexpected error updating effect providers");
            }
            debugPrinter.end();
        }
    }

    //@SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event)
    {
        //TODO trigger unique effects?
    }

    //@SubscribeEvent
    public void chunkUnloadEvent(ChunkEvent.Unload event)
    {
        //TODO notify effect of chunk unload
    }

    //@SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        //TODO notify effect of world unload
    }
}
