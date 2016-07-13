package com.builtbroken.mc.lib.mod.compat.mek;

import com.builtbroken.mc.lib.helper.WrenchUtility;
import com.builtbroken.mc.lib.mod.ModProxy;
import com.builtbroken.mc.lib.mod.compat.Mods;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/13/2016.
 */
public class MekProxy extends ModProxy
{
    public MekProxy()
    {
        super(Mods.MEKANISM);
    }

    @Override
    public void init()
    {
        WrenchUtility.registerWrenchType(new MekWrenchProxy());
    }
}
