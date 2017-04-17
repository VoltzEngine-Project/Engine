package com.builtbroken.mc.codegen.annotations;

import com.builtbroken.mc.framework.logic.ITileNode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Applied to {@link ITileNode} that need wrapped with inventory code
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface InventoryWrapped
{
    int size();
}
