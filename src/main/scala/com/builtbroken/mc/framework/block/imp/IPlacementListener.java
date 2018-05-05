package com.builtbroken.mc.framework.block.imp;

import com.builtbroken.mc.api.abstraction.entity.IEntityData;
import com.builtbroken.mc.api.data.ActionResponse;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface IPlacementListener extends ITileEventListener
{
    /**
     * Called when the tile is added
     */
    default void onAdded()
    {

    }

    /**
     * called when the tile is placed by an entity
     *
     * @param entityLivingBase
     * @param stack
     */
    default void onPlacedBy(EntityLivingBase entityLivingBase, ItemStack stack)
    {

    }

    /**
     * Called after the tile has been placed
     *
     * @param meta
     */
    default void onPostPlaced(int meta)
    {

    }

    /**
     * Called to see if the placement should be blocked
     *
     * @param side
     * @return
     */
    default ActionResponse canPlaceOnSide(int side)
    {
        return ActionResponse.IGNORE;
    }

    /**
     * Called to see if the tile can be placed
     *
     * @return true if can place
     */
    default ActionResponse canPlaceAt()
    {
        return ActionResponse.IGNORE;
    }

    /**
     * Called to see if the tile can be placed
     *
     * @param entity - entity trying to place the block, only
     *               used for com.builtbroken.mc.framework.block.ItemBlockBase
     * @return true if can place
     */
    default ActionResponse canPlaceAt(IEntityData entity, ItemStack stack)
    {
        return canPlaceAt(entity);
    }

    /**
     * Called to see if the tile can be placed
     *
     * @param entity - entity trying to place the block, only
     *               used for com.builtbroken.mc.framework.block.ItemBlockBase
     * @return true if can place
     */
    @Deprecated
    default ActionResponse canPlaceAt(IEntityData entity)
    {
        return canPlaceAt();
    }

    /**
     * Called to check if the block can be replace and be placed at the location.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param side
     * @param stack
     * @return
     */
    default ActionResponse canReplace(World world, int x, int y, int z, int side, ItemStack stack)
    {
        return ActionResponse.IGNORE;
    }

    /**
     * Checks if the block can stay after a world update
     *
     * @return {@link ActionResponse#CANCEL} to destroy block
     */
    default ActionResponse canBlockStay()
    {
        return ActionResponse.IGNORE;
    }

    @Override
    default String getListenerKey()
    {
        return BlockListenerKeys.PLACEMENT;
    }

}
