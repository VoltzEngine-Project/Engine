package com.builtbroken.mc.lib.world.map.data;

import com.builtbroken.jlib.data.network.IByteBufReader;
import com.builtbroken.jlib.data.network.IByteBufWriter;
import com.builtbroken.mc.api.ISave;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public abstract class ChunkData implements ISave, IByteBufWriter, IByteBufReader
{
    public final ChunkCoordIntPair position;

    public ChunkData(int x, int z)
    {
        position = new ChunkCoordIntPair(x, z);
    }

    @Override
    public void load(NBTTagCompound nbt)
    {

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        return nbt;
    }

    @Override
    public Object readBytes(ByteBuf buf)
    {
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf buf)
    {
        return buf;
    }
}
