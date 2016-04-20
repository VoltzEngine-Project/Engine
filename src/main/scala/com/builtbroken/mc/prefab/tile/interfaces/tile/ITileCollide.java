package com.builtbroken.mc.prefab.tile.interfaces.tile;

import com.builtbroken.mc.lib.transform.region.Cube;
import net.minecraft.entity.Entity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public interface ITileCollide extends ITile
{
    void onCollide(Entity entity);

    Iterable<Cube> getCollisionBoxes(Cube subtract, Entity entity);

    Cube getSelectBounds();

    Cube getCollisionBounds();
}
