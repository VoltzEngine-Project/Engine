package com.builtbroken.mc.abstraction;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.commands.permissions.GroupProfileHandler;
import com.builtbroken.mc.framework.access.AccessModule;
import com.builtbroken.mc.framework.json.IJsonGenMod;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.mod.loadable.LoadableHandler;

/**
 * Prefab for all version specific mod loaders
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public abstract class EngineLoader implements IJsonGenMod
{
    public static LoadableHandler loader = new LoadableHandler();

    public LoadableHandler getModuleLoader()
    {
        return loader;
    }

    /**
     * Loads handlers that can be loaded on any version of MC
     * <p>
     * if the load is version specific it needs to go into
     * one of the per version mod loaders
     */
    public void loadHandler()
    {
        getModuleLoader().applyModule(GroupProfileHandler.GLOBAL);
        getModuleLoader().applyModule(new AccessModule());
        getModuleLoader().applyModule(JsonContentLoader.INSTANCE);
    }

    @Override
    public String getPrefix()
    {
        return References.PREFIX;
    }

    @Override
    public String getDomain()
    {
        return References.DOMAIN;
    }
}
