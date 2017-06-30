package com.builtbroken.mc.core.network.packet.callback.chunk;

import com.builtbroken.mc.api.data.IPacket;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataManager;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataMap;
import com.builtbroken.mc.lib.world.map.data.ChunkData;
import com.builtbroken.mc.lib.world.map.data.s.ChunkDataShort;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class PacketRequestData implements IPacket
{
    int dim, x, z, type;

    public PacketRequestData()
    {

    }

    public PacketRequestData(int dim, int x, int z, int type)
    {
        this.dim = dim;
        this.x = x;
        this.z = z;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(dim);
        buffer.writeInt(x);
        buffer.writeInt(z);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        dim = buffer.readInt();
        x = buffer.readInt();
        z = buffer.readInt();
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            ChunkData data = null;
            if (type == 0)
            {
                ExtendedBlockDataMap map = ExtendedBlockDataManager.SERVER.getMapForDim(dim, false);
                if (map != null)
                {
                    data = map.getChunk(x, z);
                }

                if (data == null)
                {
                    data = new ChunkDataShort(x, z);
                }
            }

            if (data != null)
            {
                Engine.instance.packetHandler.sendToPlayer(new PacketSendData(dim, x, z, type, data), (EntityPlayerMP) player);
            }
        }
    }
}
