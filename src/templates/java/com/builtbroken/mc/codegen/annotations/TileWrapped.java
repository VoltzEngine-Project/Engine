package com.builtbroken.mc.codegen.annotations;

import com.builtbroken.mc.framework.logic.ITileNode;
import com.builtbroken.mc.framework.logic.wrapper.TileEntityWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Applied to instances of {@link ITileNode} so that a {@link TileEntityWrapper}
 * can be created for the tile.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface TileWrapped
{
    /** File name to use for the class */
    String className();
}
