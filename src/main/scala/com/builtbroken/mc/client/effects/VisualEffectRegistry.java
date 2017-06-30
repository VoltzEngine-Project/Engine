package com.builtbroken.mc.client.effects;

import com.builtbroken.mc.core.Engine;

import java.util.HashMap;

/**
 * Handles spawning visual effects into the game world without knowing what the effect is directly. The primary
 * use of this system is for JSON handling of blocks and items. In which the JSON data can describe what effect
 * needs to be used and with what kind of data. This way direct calls or usage of the effect is not required by
 * the JSON content loader or other systems that handle the object.
 * <p>
 * Additionally this allows mods to provide effects for use by other mods. Without the mod needing to understand the
 * full implementation of the effect or the providing mod.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2017.
 */
public class VisualEffectRegistry
{
    /** Primary effect registry shared by all mods */
    public static VisualEffectRegistry main = new VisualEffectRegistry();

    /** Map of effects to providers */
    private HashMap<String, VisualEffectProvider> effects = new HashMap();

    /**
     * Called to add an effect provider to the registry
     *
     * @param provider - handles generating effects into the world
     */
    public static void addEffectProvider(VisualEffectProvider provider)
    {
        String key = provider.name.toLowerCase();
        if (main.effects.containsKey(key))
        {
            Engine.logger().warn("Effect " + main.effects.get(key) + " with key " + key + " is being overridden by " + provider);
        }
        main.effects.put(key, provider);
    }

    /**
     * Called to get an effect provider
     *
     * @param name - name of the effect
     * @return provider, or null if not found
     */
    public VisualEffectProvider get(String name)
    {
        return main.effects.get(name.toLowerCase());
    }
}
