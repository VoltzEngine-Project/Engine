package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.api.data.IPacket;
import com.builtbroken.mc.core.handler.RenderSelection;
import com.builtbroken.mc.imp.transform.region.Cube;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/15/2015.
 */
public class PacketSelectionData implements IPacket
{
    List<Cube> cubes;
    List<Cube> regions;
    Cube selection;

    public PacketSelectionData()
    {
        //Needed for forge to construct the packet
    }

    public PacketSelectionData(Cube selection, List<Cube> cubes, List<Cube> regions)
    {
        this.selection = selection;
        this.cubes = cubes;
        this.regions = regions;

        if (this.selection == null)
            this.selection = new Cube();

        if (this.cubes == null)
            this.cubes = new ArrayList();

        if (this.regions == null)
            this.regions = new ArrayList();
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
            cube.writeBytes(buffer);
        }

        //Write regions
        buffer.writeInt(regions.size());
        for (Cube cube : regions)
        {
            cube.writeBytes(buffer);
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        RenderSelection.cube_render_list.clear();
        RenderSelection.region_render_list.clear();
        //Read selection
        RenderSelection.selection = new Cube(buffer);

        //Read other player's selections to render
        int count = buffer.readInt();
        if (count > 0)
            for (int i = 0; i < count; i++)
            {
                RenderSelection.cube_render_list.add(new Cube(buffer));
            }

        //Read region bounds
        count = buffer.readInt();
        if (count > 0)
            for (int i = 0; i < count; i++)
            {
                RenderSelection.region_render_list.add(new Cube(buffer));
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
