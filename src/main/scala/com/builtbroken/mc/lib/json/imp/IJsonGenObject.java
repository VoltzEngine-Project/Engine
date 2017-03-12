package com.builtbroken.mc.lib.json.imp;

/**
 * Applied to all objects created from Json data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public interface IJsonGenObject
{
    /**
     * Called to register the object to its
     * handlers. Use this to register
     * blocks, items, and entities. Do
     * not use it for oreNames, recipes, or
     * code that needs other entries to function.
     * use {@link com.builtbroken.mc.core.registry.implement.IPostInit}
     * instead to allow all other mods to register there
     * content in the preInit and init phases
     */
    void register();

    /**
     * Unique key of the loader that created
     * this gen object.
     *
     * @return key for loader
     */
    String getLoader();
}
