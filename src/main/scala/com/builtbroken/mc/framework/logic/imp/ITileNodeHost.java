package com.builtbroken.mc.framework.logic.imp;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.framework.logic.ITileNode;
import net.minecraft.tileentity.TileEntity;

/**
 * Applied to objects that act as hosts for {@link com.builtbroken.mc.framework.logic.ITileNode}
 * <p>
 * Keep in mind the host will not always be {@link TileEntity} as it could be an entity
 * or an a ghost object acting as a data provider.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
public interface ITileNodeHost extends IWorldPosition
{
    /**
     * Gets the node that controls all logic
     * for this tile
     *
     * @return node, should never be null on
     * a valid tile.
     */
    ITileNode getTileNode();
}
