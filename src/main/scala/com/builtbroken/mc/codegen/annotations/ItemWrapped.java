package com.builtbroken.mc.codegen.annotations;

import com.builtbroken.mc.framework.item.ItemNode;

/**
 * Applied to {@link ItemNode} to add support for {@link net.minecraft.item.Item} interfaces
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/9/2017.
 */
public @interface ItemWrapped
{
    /** File name to use for the class */
    String className();

    /** Wrappers to use during processing */
    String wrappers() default "";
}
