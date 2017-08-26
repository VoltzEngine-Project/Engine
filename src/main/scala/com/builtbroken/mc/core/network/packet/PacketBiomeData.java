package com.builtbroken.mc.core.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

/**
 * Used to sync changes made to a chunk biome map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2016.
 */
public class PacketBiomeData extends PacketType
{
    int chunkX, chunkZ;
    byte[] biomes;

    public PacketBiomeData()
    {
        //needed for packet loading
    }

    public PacketBiomeData(Chunk chunk)
    {
        chunkX = chunk.x;
        chunkZ = chunk.z;
        biomes = chunk.getBiomeArray();
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(chunkX);
        buffer.writeInt(chunkZ);
        for (byte b : biomes)
        {
            buffer.writeByte(b);
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        chunkX = buffer.readInt();
        chunkZ = buffer.readInt();
        biomes = new byte[256];
        for (int i = 0; i < 256; i++)
        {
            byte b = buffer.readByte();
            if (b >= 0 && b < 256 && Biome.getBiome(b) != null)
            {
                biomes[i] = b;
            }
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        //Only do this client side to prevent cheating
        Chunk chunk = player.getEntityWorld().getChunkFromChunkCoords(chunkX, chunkZ);
        if (chunk != null)
        {
            chunk.setBiomeArray(biomes);
        }
    }
}
