package com.builtbroken.mc.lib.mod.compat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;

/**
 * Created by robert on 12/31/2014.
 */
public enum Mods
{
    OC("OpenComputers"),
    CC("ComputerCraft"),
    AE("AppliedEnergistics"),
    AM2("arsmagica2"),
    BOP( "BiomesOPlenty"),
    BC("BuildCraft|Core"),
    IC2("IC2"),
    WAILA("Waila");

    public final String mod_id;

    private Mods(String id)
    {
        this.mod_id = id;
    }

    public boolean isLoaded()
    {
        return Loader.isModLoaded(mod_id);
    }
}
