package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.api.ISave;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by robert on 1/12/2015.
 */
public class PacketSaveData extends PacketTile
{
    NBTTagCompound tag;

    public PacketSaveData()
    {

    }

    public PacketSaveData(TileEntity tile)
    {
        super(tile, 0);
        tag = new NBTTagCompound();
        tile.writeToNBT(tag);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        write(tag, buffer);
        super.encodeInto(ctx, buffer);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        tag = ByteBufUtils.readTag(buffer);
        super.decodeInto(ctx, buffer);
    }

    @Override
    public void handle(EntityPlayer player)
    {
        TileEntity tile = player.getEntityWorld().getTileEntity(this.x, this.y, this.z);
        if(tile != null)
        {
            tile.readFromNBT(tag);
        }
    }
}
