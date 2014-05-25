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

    public REPLY onPacket(REQ message, /* */)

}
