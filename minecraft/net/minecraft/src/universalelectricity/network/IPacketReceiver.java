package net.minecraft.src.universalelectricity.network;

import java.io.DataInputStream;

import net.minecraft.src.NetworkManager;
import net.minecraft.src.forge.IPacketHandler;

public interface IPacketReceiver
{
	/**
	 * A ID for this type of packet
	 * @return
	 */
	public int getPacketID();
	
	/**
     * Called when we receive a Packet250CustomPayload for a channel that this
     * handler is registered to.
     *
     * @param network The NetworkManager for the current connection.
     * @param channel The Channel the message came on.
     * @param data The message payload.
     */
    public void onPacketData(NetworkManager network, String channel, DataInputStream dataStream);
}
