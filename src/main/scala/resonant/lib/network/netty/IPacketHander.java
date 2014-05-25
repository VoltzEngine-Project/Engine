package resonant.lib.network.netty;

/**
 * A message handler based on {@link resonant.lib.network.netty.INettyPacket}. Implement and override {@link #onMessage(IMessage)} to
 * process your packet. Supply the class to {@link SimpleNetworkWrapper#registerMessage(Class, Class, byte, cpw.mods.fml.relauncher.Side)}
 * to register both the message type and it's associated handler.
 *
 * @since 25/05/14
 * @author cpw, tgame14
 */
public interface IPacketHander<REQ extends INettyPacket, REPLY extends INettyPacket>
{

    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @return an optional return message
     */
    public REPLY onPacket(REQ message, PacketContext ctx);

}
