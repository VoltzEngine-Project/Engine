package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.tile.IRotation;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by robert on 1/12/2015.
 */
public class TileMachine extends TileEnt implements IRotation
{
    protected ForgeDirection facing = ForgeDirection.NORTH;

    public TileMachine(String name, Material material)
    {
        super(name, material);
    }

    @Override
    public void onPlaced(EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onPlaced(entityLiving, itemStack);
        this.facing = determineForgeDirection(entityLiving);
    }

    @Override
    public ForgeDirection getDirection()
    {
        if (facing == null)
            facing = ForgeDirection.NORTH;
        return facing;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("facing"))
            this.facing = ForgeDirection.getOrientation(nbt.getByte("facing"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (facing != null && facing != ForgeDirection.NORTH)
            nbt.setByte("facing", (byte) facing.ordinal());
    }

    @Override
    public void readDescPacket(ByteBuf buf)
    {
        super.readDescPacket(buf);
        facing = ForgeDirection.getOrientation(buf.readByte());
    }

    @Override
    public void writeDescPacket(ByteBuf buf)
    {
        super.writeDescPacket(buf);
        buf.writeByte((byte)(facing != null ? facing.ordinal() : 2));
    }
}
