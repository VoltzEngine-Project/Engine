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
    List<Cube> regions;
    Cube selection;

    public PacketSelectionData()
    {

    }

    public PacketSelectionData(Cube selection, List<Cube> cubes, List<Cube> regions)
    {
        this.selection = selection;
        this.cubes = cubes;
        this.regions = regions;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        //Write player's selection
        selection.writeBytes(buffer);

        //Write cubes
        buffer.writeInt(cubes.size());
        for (Cube cube : cubes)
        {
            if (cube.pointOne() != null || cube.pointTwo() != null)
               cube.writeBytes(buffer);
        }

        //Write regions
        buffer.writeInt(regions.size());
        for (Cube cube : regions)
        {
            if (cube.pointOne() != null || cube.pointTwo() != null)
                cube.writeBytes(buffer);
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        //Read selection
        RenderSelection.selection = new Cube(buffer);

        //Read other player's selections to render
        int count = buffer.readInt();
        for(int i = 0; i < count; i++)
        {
            RenderSelection.cube_render_list.add(new Cube(buffer));
        }

        //Read region bounds
        count = buffer.readInt();
        for(int i = 0; i < count; i++)
        {
            RenderSelection.cube_render_list.add(new Cube(buffer));
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
