package com.builtbroken.mc.lib.world.map.data.s;

import com.builtbroken.mc.lib.world.map.data.ChunkData;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public class ChunkDataShort extends ChunkData
{
    public final ChunkSectionShort[] sections = new ChunkSectionShort[16];

    public ChunkDataShort(int x, int z)
    {
        super(x, z);
    }

    public short getValue(int x, int y, int z)
    {
        int s = y / 16;
        if (sections[s] == null)
        {
            sections[s] = new ChunkSectionShort();
        }
        return sections[s].getValue(x, y & 15, z);
    }

    public short setValue(int x, int y, int z, int value)
    {
        return setValue(x, y, z, (short) value);
    }

    public short setValue(int x, int y, int z, short value)
    {
        int s = y / 16;
        if (sections[s] == null)
        {
            sections[s] = new ChunkSectionShort();
        }
        short prev = sections[s].getValue(x, y & 15, z);
        sections[s].setValue(x, y & 15, z, value);
        return prev;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        clear();

        NBTTagList list = nbt.getTagList("sections", 10);
        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            int section = tag.getInteger("section_id");
            sections[section] = new ChunkSectionShort();
            sections[section].load(nbt);
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < sections.length; i++)
        {
            if (sections[i] != null) //TODO does need saved
            {
                NBTTagCompound tag = new NBTTagCompound();
                sections[i].save(nbt);
                tag.setByte("section_id", (byte) i);
                list.appendTag(tag);
            }
        }
        nbt.setTag("sections", list);
        return nbt;
    }

    @Override
    public Object readBytes(ByteBuf buf)
    {
        load(ByteBufUtils.readTag(buf));
        return this;
    }

    @Override
    public ByteBuf writeBytes(ByteBuf buf)
    {
        NBTTagCompound tag = new NBTTagCompound();
        save(tag);
        ByteBufUtils.writeTag(buf, tag);
        return buf;
    }

    public void clear()
    {
        for (int i = 0; i < sections.length; i++)
        {
            sections[i] = null; //TODO clear data
        }
    }
}
