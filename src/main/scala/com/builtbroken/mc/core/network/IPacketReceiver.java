package com.builtbroken.mc.core.network;

import com.builtbroken.mc.core.network.packet.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by robert on 1/12/2015.
 */
public interface IPacketReceiver
{
    public boolean read(EntityPlayer player, AbstractPacket packet);
}
