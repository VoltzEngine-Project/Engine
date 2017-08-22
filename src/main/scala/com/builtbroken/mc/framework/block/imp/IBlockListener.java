package com.builtbroken.mc.framework.block.imp;

import com.builtbroken.mc.api.abstraction.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Applied to {@link ITileEventListener} that listen directly for the block even and not the tile version of the event.
 * <p>
 * This should be used for events that need to fire regardless if a tile exists.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface IBlockListener
{
    /**
     * Called to set the location of the listener so that
     * normal tile calls can be used.
     *
     * @param world
     */
    void inject(IWorld world, BlockPos pos);

    void inject(IBlockAccess world, BlockPos pos);

    default void eject()
    {

    }
}
