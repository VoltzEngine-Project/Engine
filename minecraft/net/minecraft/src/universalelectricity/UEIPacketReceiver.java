package net.minecraft.src.universalelectricity;

import net.minecraft.src.forge.IPacketHandler;

public interface UEIPacketReceiver extends IPacketHandler
{
	public int getPacketID();
}
