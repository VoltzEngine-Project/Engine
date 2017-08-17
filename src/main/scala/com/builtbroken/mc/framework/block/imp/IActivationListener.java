package com.builtbroken.mc.framework.block.imp;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Applied to objects that listen for block action events
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface IActivationListener extends ITileEventListener
{
    /**
     * Called when the player right-clicks the block
     *
     * @param player - player clicking, check for automation if automation should be blocked
     * @param side   - side of the block
     * @param hitX   - location on side hit
     * @param hitY   - location on side hit
     * @param hitZ   - location on side hit
     * @return true if click is used
     */
    default boolean onPlayerActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Called when the player left clicks a tile
     *
     * @param player
     * @return
     */
    default boolean onPlayerClicked(EntityPlayer player)
    {
        return false;
    }

    @Override
    default String getListenerKey()
    {
        return "activation";
    }
}
