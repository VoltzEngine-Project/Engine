package com.builtbroken.mc.prefab.tile.interfaces.tile;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public interface IRedstone extends ITile
{
    int getWeakRedstonePower(int side);

    int getStrongRedstonePower(int side);
}
