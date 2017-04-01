package com.builtbroken.mc.framework.logic.annotations;

import com.builtbroken.mc.framework.logic.ITileController;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Applied to instances of {@link ITileController} so that a {@link com.builtbroken.mc.framework.logic.TileEntityWrapper}
 * can be created for the tile.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface LogicTile
{
    /** ID to register tile with */
    String id();

    /** File name to use for the class */
    String className();
}
