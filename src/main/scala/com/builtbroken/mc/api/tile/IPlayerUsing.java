package com.builtbroken.mc.api.tile;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;

/**
 * Created by robert on 1/12/2015.
 */
public interface IPlayerUsing
{
    Collection<EntityPlayer> getPlayersUsing();
}
