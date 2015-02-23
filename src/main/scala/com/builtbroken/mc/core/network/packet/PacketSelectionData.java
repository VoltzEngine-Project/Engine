package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.core.handler.RenderSelection;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/15/2015.
 */
public class PacketSelectionData extends AbstractPacket
{
    List<Cube> cubes;

    public PacketSelectionData()
    {

    }

    public PacketSelectionData(List<Cube> cubes)
    {
        this.cubes = cubes;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (Cube cube : cubes)
        {
            if (cube.max() != null || cube.min() != null)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                NBTTagCompound a = new NBTTagCompound();
                NBTTagCompound b = new NBTTagCompound();
                if (cube.min() != null)
                {
                    new Pos(cube.min()).writeNBT(a);
                    nbt.setTag("a", a);
                }
                if (cube.max() != null)
                {
                    new Pos(cube.max()).writeNBT(b);
                    nbt.setTag("b", b);
                }
                list.appendTag(nbt);
            }
        }
        tag.setTag("cubes", list);
        ByteBufUtils.writeTag(buffer, tag);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        NBTTagCompound tag = ByteBufUtils.readTag(buffer);
        NBTTagList list = tag.getTagList("cubes", 10);
        List<Cube> cubes = new ArrayList();

        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            Pos a = nbt.hasKey("a") ? new Pos(nbt.getCompoundTag("a")) : null;
            Pos b = nbt.hasKey("b") ? new Pos(nbt.getCompoundTag("b")) : null;
            cubes.add(new Cube(a, b));
        }

        //Clear old selections
        RenderSelection.cube_render_list.clear();
        RenderSelection.selection = new Cube();

        //Add new selections
        if (cubes.size() > 0)
        {
            RenderSelection.selection = cubes.get(0);
            cubes.remove(0);
            if (cubes.size() > 0)
            {
                RenderSelection.cube_render_list = cubes;
            }
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        handle(player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        handle(player);
    }

    public void handle(EntityPlayer player)
    {

    }
}
