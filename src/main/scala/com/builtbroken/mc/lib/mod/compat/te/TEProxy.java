package com.builtbroken.mc.lib.mod.compat.te;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.mod.ModProxy;
import com.builtbroken.mc.lib.mod.compat.Mods;
import com.builtbroken.mc.lib.mod.compat.rf.RFEnergyHandler;

/**
 * Loads support for thermal expansion
 * Created by Dark(DarkGuardsman, Robert) on 8/10/2016.
 */
public class TEProxy extends ModProxy
{
    public TEProxy()
    {
        super(Mods.TF_EXPANSION);
    }

    @Override
    public void init()
    {
        super.init();
        Engine.instance.logger().info("Thermal Expansion support loaded");
        RFEnergyHandler.thermalExpansionHandler = new ThermalExpansionEnergyHandler();
    }
}
