package com.builtbroken.mc.framework.json;

import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import com.builtbroken.mc.framework.mod.IMod;
import net.minecraftforge.common.config.Configuration;

/**
 * Applied to mods that support loading json generated objects.
 * <p>
 * The interface is used to access data need to register content to
 * the mod container at runtime.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/13/2017.
 */
public interface IJsonGenMod extends IMod
{
    /**
     * Called by Voltz Engine's content loading system
     * to ensure that as many json handlers are loaded
     * as possible.
     * <p>
     * Not all handlers need to be registered inside this
     * method but it does improve performance. As well
     * fixes issues with nested loaders such as
     * {@link ITileEventListener} loading which is only run
     * once.
     *
     * Do not run logic or registry calls at this time. This should
     * only be used to add JSON handlers and converters to the system.
     * This way the content system can load as many data points as possible
     * before blocks and items are created.
     */
    default void loadJsonContentHandlers() //TODO replace with event call
    {

    }

    /**
     * Gets the manager that is used to
     * register content for the mod.
     *
     * @return
     */
    default ModManager getJsonContentManager()
    {
        return getManager();
    }

    /**
     * Gets the config file
     *
     * @return
     */
    default Configuration getJsonContentConfig()
    {
        return getConfig();
    }
}
