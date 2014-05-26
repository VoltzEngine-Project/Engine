package resonant.lib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.tileentity.TileEntity;
import resonant.core.ResonantEngine;

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

    public PacketTile (TileEntity tile, Object... args)
    {
        this(tile.xCoord, tile.yCoord, tile.zCoord, args);
    }

    public PacketTile(int id, TileEntity tile, Object... args)
    {
        super(id, getArgs(tile.xCoord, tile.yCoord, tile.zCoord, args));
    }

    public PacketTile (int x, int y, int z, Object... args)
    {
        super(getArgs(x, y, z, args));
    }

    @Override
    public void encodeInto (ChannelHandlerContext ctx, ByteBuf buffer)
    {

    }

    @Override
    public void decodeInto (ChannelHandlerContext ctx, ByteBuf buffer)
    {

    }

    public static Object[] getArgs (int x, int y, int z, Object... args)
    {
        List newArgs = new ArrayList();

        newArgs.add(x);
        newArgs.add(y);
        newArgs.add(z);

        for (Object obj : args)
        {
            newArgs.add(obj);
        }
        return newArgs.toArray();
    }
}
