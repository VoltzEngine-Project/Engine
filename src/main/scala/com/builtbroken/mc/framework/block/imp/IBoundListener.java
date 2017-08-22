package com.builtbroken.mc.framework.block.imp;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2017.
 */
public interface IBoundListener extends ITileEventListener
{
    /**
     * Gets the sub collision boxes at the location
     *
     * @param aabb
     * @param list
     * @param entity
     */
    default void addCollisionBoxesToList(AxisAlignedBB aabb, List list, Entity entity)
    {

    }

    /**
     * Gets the selection bounds at the location
     *
     * @return
     */
    default AxisAlignedBB getSelectedBounds()
    {
        return null;
    }

    /**
     * Gets the collision bounds at the location
     *
     * @return
     */
    default AxisAlignedBB getCollisionBounds()
    {
        return null;
    }

    /**
     * Called to set block bounds based on current state
     */
    default void setBlockBoundsBasedOnState()
    {

    }

    /**
     * Called to set the block bounds for the item.
     * <p>
     * Does not inject location data
     */
    default void setBlockBoundsForItemRender()
    {

    }

    default String getListenerKey()
    {
        return "bounds";
    }
}
