package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.lib.render.RenderSelection;
import com.builtbroken.mc.lib.transform.region.Cuboid;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/15/2015.
 */
public class PacketSelectionData extends PacketType
{
    public PacketSelectionData()
    {

    }

    List<Cuboid> cubes;

    public PacketSelectionData(List<Cuboid> cubes)
    {
        this.cubes = cubes;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        tag.setInteger("count", cubes.size());
        for(Cuboid cube: cubes)
        {
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound a = new NBTTagCompound();
            NBTTagCompound b = new NBTTagCompound();
            new Pos(cube.min()).writeNBT(a);
            new Pos(cube.max()).writeNBT(b);
            nbt.setTag("a", a);
            nbt.setTag("b", b);
            list.appendTag(nbt);
        }
        tag.setTag("cubes", tag);
        ByteBufUtils.writeTag(buffer, tag);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        NBTTagCompound tag = ByteBufUtils.readTag(buffer);
        NBTTagList list = tag.getTagList("cubes", 10);
        List<Cuboid> cubes = new ArrayList();

        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            Pos a = new Pos(nbt.getCompoundTag("a"));
            Pos b = new Pos(nbt.getCompoundTag("b"));
            cubes.add(new Cuboid(a, b));
        }

        //Clear old selections
        RenderSelection.cube_render_list.clear();
        RenderSelection.selection = new Cuboid();

        //Add new selections
        if(cubes.size() > 0)
        {
            RenderSelection.selection = cubes.get(0);
            cubes.remove(0);
            if(cubes.size() > 0)
            {
                RenderSelection.cube_render_list = cubes;
            }
        }
    }
}
