package com.builtbroken.mc.framework.tile.api;

import com.builtbroken.mc.framework.tile.BlockProperties;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2016.
 */
public interface IBlockWrapper
{
    BlockProperties getBlockData(World world, int x, int y, int z);
}
