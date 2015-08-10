package com.builtbroken.mc.lib.mod.compat.rf;

import com.builtbroken.mc.lib.mod.compat.Mods;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;

/**
 * Created by Cow Pi on 8/10/2015.
 */
public class RFLoader extends AbstractLoadable
{
    @Override
    public void init()
    {
        super.init();
        //TODO load energy handler
        //TODO load RF tile for multiblocks
    }

    @Override
    public boolean shouldLoad()
    {
        return Mods.RF.isLoaded();
    }
}
