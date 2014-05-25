package resonant.lib.network.netty;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

/**
 * @since 25/05/14
 * @author tgame14
 */
public class PacketContext
{
    public final INetHandler netHandler;
    public final Side side;

    PacketContext (INetHandler netHandler, Side side)
    {
        this.netHandler = netHandler;

        this.side = side;
    }

    public NetHandlerPlayServer getServerHandler()
    {
        return (NetHandlerPlayServer) netHandler;
    }

    public NetHandlerPlayClient getClientHandler()
    {
        return (NetHandlerPlayClient) netHandler;
    }
}
