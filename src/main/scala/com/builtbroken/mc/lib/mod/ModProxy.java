package com.builtbroken.mc.lib.mod;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/13/2016.
 */
public class ModProxy extends AbstractLoadable
{
    public final Mods mod;

    public ModProxy(Mods mod)
    {
        this.mod = mod;
    }

    @Override
    public boolean shouldLoad()
    {
        return Engine.instance.getConfig().getBoolean("Load_" + mod.mod_id, "Mod_Support", true, "");
    }
}
