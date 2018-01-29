package com.builtbroken.mc.seven.server;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.blast.emp.ExEmp;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.seven.CommonProxy;

/**
 * Created by robert on 2/17/2015.
 */
public class ServerProxy extends CommonProxy
{
    @Override
    public void init()
    {
        super.init();

        //Register explosives
        ExplosiveRegistry.registerOrGetExplosive(References.DOMAIN, "Emp", new ExEmp());
    }
}
