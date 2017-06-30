package com.builtbroken.mc.lib.mod;

import com.builtbroken.mc.core.registry.ModManager;
import net.minecraftforge.common.config.Configuration;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public interface IMod
{
    /** Prefix used by the mod before resources, example( Mod: ) */
    String getPrefix();

    /** Resource folder name, used to pull localization and textures */
    String getDomain();

    /**
     * Gets the manager that is used to
     * register content for the mod.
     *
     * @return
     */
    ModManager getManager();

    /**
     * Gets the config file
     *
     * @return
     */
    Configuration getConfig();
}
