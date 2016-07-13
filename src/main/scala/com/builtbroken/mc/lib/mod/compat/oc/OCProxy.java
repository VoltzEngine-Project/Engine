package com.builtbroken.mc.lib.mod.compat.oc;

import com.builtbroken.mc.lib.mod.ModProxy;
import com.builtbroken.mc.lib.mod.compat.Mods;
import li.cil.oc.api.Driver;

/**
 * Created by robert on 3/2/2015.
 */
public class OCProxy extends ModProxy
{
    public OCProxy()
    {
        super(Mods.OC);
    }

    @Override
    public void init()
    {
        Driver.add(new ConverterIPos());
        Driver.add(new ConverterIPos2D());
        Driver.add(new ConverterIWorldPosition());
    }

    @Override
    public boolean shouldLoad()
    {
        return Mods.OC.isLoaded();
    }
}
