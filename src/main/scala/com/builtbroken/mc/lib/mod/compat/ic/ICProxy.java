package com.builtbroken.mc.lib.mod.compat.ic;

import com.builtbroken.mc.lib.helper.WrenchUtility;
import com.builtbroken.mc.lib.mod.ModProxy;
import com.builtbroken.mc.lib.mod.compat.Mods;
import net.minecraftforge.common.MinecraftForge;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/26/2016.
 */
public class ICProxy extends ModProxy
{
    public ICProxy()
    {
        super(Mods.IC2);
    }

    @Override
    public void init()
    {
        WrenchUtility.registerWrenchType(new ICWrenchProxy());
        MinecraftForge.EVENT_BUS.register(ICStaticForwarder.INSTANCE);
    }
}
