package com.builtbroken.mc.lib.mod.loadable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by robert on 1/10/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadWithMod
{
    /** ID of the mod to check against */
    String mod_id();

    //TODO add mod version checking for loading content based on mod versions or MC versions
}
