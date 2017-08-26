package com.builtbroken.mc.core.network.packet.callback.chunk;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataManager;
import com.builtbroken.mc.lib.world.map.data.ChunkData;
import com.builtbroken.mc.lib.world.map.data.s.ChunkDataShort;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class PacketSendData extends PacketRequestData
{
    ChunkData data = null;

    public PacketSendData()
    {

    }

    public PacketSendData(int dim, int x, int z, int type, ChunkData data)
    {
        super(dim, x, z, type);
        this.data = data;
    }


    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        super.encodeInto(ctx, buffer);
        data.writeBytes(buffer);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        super.decodeInto(ctx, buffer);

        if (type == 0)
        {
            data = new ChunkDataShort(x, z);
        }
        else if (Engine.runningAsDev)
        {
            Engine.logger().error("ChunkPacketHandler: Failed to ID chunk type[" + type + "] while decoding chunk data packet");
        }

        if (data != null)
        {
            data.readBytes(buffer);
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        if (data != null && dim == player.world.provider.getDimension())
        {
            if (type == 0)
            {
                ExtendedBlockDataManager.CLIENT.chunks.put(data.position, (ChunkDataShort) data);
            }
        }
    }
}
