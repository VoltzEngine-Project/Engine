package com.builtbroken.mc.framework.block.imp;

import com.builtbroken.mc.api.abstraction.entity.IEntityData;
import com.builtbroken.mc.api.data.ActionResponse;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

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
     * Called to see if the placement should be blocked
     *
     * @param side
     * @return
     */
    default ActionResponse canPlaceOnSide(EnumFacing side)
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
    default ActionResponse canPlaceAt(IEntityData entity)
    {
        return canPlaceAt();
    }

    @Override
    default String getListenerKey()
    {
        return "placement";
    }

}
