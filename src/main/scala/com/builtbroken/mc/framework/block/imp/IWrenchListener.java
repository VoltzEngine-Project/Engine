package com.builtbroken.mc.framework.block.imp;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Applied to objects that listen for wrench actions on a block
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface IWrenchListener extends ITileEventListener
{
    /**
     * Called when a player right clicks a block with a wrench
     *
     * @param player
     * @param side
     * @param hitX
     * @param hitY
     * @param hitZ
     * @return
     */
    default boolean onPlayerRightClickWrench(EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Can this tile handle the wrench activation
     *
     * @return false to ignore wrench right clicks and let normal right click code work
     */
    default boolean handlesWrenchRightClick()
    {
        return true;
    }

    @Override
    default String getListenerKey()
    {
        return "wrench";
    }
}
