package net.minecraft.src.universalelectricity.network;

import net.minecraft.src.forge.IPacketHandler;

public interface IPacketReceiver extends IPacketHandler
{
	public int getPacketID();
}
