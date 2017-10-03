package com.builtbroken.mc.framework.block.imp;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Applied to objects that listen for block action events
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface IHardnessListener extends ITileEventListener
{
    default float getBlockHardness()
    {
        return -1;
    }

    default float getBlockHardness(EntityPlayer player)
    {
        return getBlockHardness();
    }

    @Override
    default String getListenerKey()
    {
        return "hardness";
    }
}
