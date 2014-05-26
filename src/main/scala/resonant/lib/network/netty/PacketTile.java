package resonant.lib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import resonant.core.ResonantEngine;
import resonant.lib.network.IPacketReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @since 26/05/14
 * @author tgame14
 */
public class PacketTile extends PacketType
{
    protected int x;
    protected int y;
    protected int z;
    protected int id;

    public PacketTile (int x, int y, int z, int id, Object... args)
    {
        super(args);
        this.x = x;
        this.y = y;
        this.z = y;
        this.id = id;
    }

    public PacketTile (int x, int y, int z, Object... args)
    {
        this(x, y, z, -1, args);
    }


    @Override
    public void encodeInto (ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(id);

        buffer.writeBytes(this.data);

    }

    @Override
    public void decodeInto (ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.id = buffer.readInt();

        this.data = buffer.slice();
    }

    @Override
    public void handleClientSide (EntityPlayer player)
    {
        if (this.id == -1)
        {
            IPacketReceiver receiver = (IPacketReceiver) player.getEntityWorld().getTileEntity(this.x, this.y, this.z);
        }
    }
}
