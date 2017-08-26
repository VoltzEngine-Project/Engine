package com.builtbroken.mc.framework.json;

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
     */
    default void loadJsonContentHandlers()
    {

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
