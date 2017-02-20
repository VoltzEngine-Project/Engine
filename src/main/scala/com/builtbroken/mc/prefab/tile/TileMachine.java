package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.tile.IRotation;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Prefab for anything that acts as a machine block
 * Created by Dark(DarkGuardsman, Robert) on 1/12/2015.
 */
public class TileMachine extends TileEnt implements IRotation
{
    /** Direction the machine is facing, try to use {@link #getFacing()} and {@link #setFacing(ForgeDirection)} */
    protected ForgeDirection facing = ForgeDirection.UNKNOWN;

    /**
     * Creates a new TileMachine instance
     *
     * @param name     - name of the tile
     * @param material - material of the tile
     */
    public TileMachine(String name, Material material)
    {
        super(name, material);
    }

    @Override
    public void firstTick()
    {
        super.firstTick();
        if (useMetaForFacing())
        {
            facing = ForgeDirection.getOrientation(world().getBlockMetadata(xi(), yi(), zi()));
        }
    }

    @Override
    public void onPlaced(EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onPlaced(entityLiving, itemStack);
        setRotationOnPlacement(entityLiving, itemStack);
    }

    /**
     * Called to set the rotation when an entity places the tile
     *
     * @param entityLiving
     * @param itemStack
     */
    protected void setRotationOnPlacement(EntityLivingBase entityLiving, ItemStack itemStack)
    {
        //TODO implement advanced placement for improved user controls
        this.setFacing(determineForgeDirection(entityLiving));
    }

    @Override
    public ForgeDirection getDirection()
    {
        return getFacing();
    }

    /**
     * Called to see if the tile wants to
     * store the facing direction as a meta
     * value rather than as an NBT value
     *
     * @return true if yes
     */
    protected boolean useMetaForFacing()
    {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (!useMetaForFacing())
        {
            setFacing(ForgeDirection.getOrientation(nbt.getByte("facing")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (!useMetaForFacing() && getFacing() != null)
        {
            nbt.setByte("facing", (byte) getFacing().ordinal());
        }
    }

    @Override
    public void readDescPacket(ByteBuf buf)
    {
        super.readDescPacket(buf);
        if (!useMetaForFacing())
        {
            setFacing(ForgeDirection.getOrientation(buf.readByte()));
        }
    }

    @Override
    public void writeDescPacket(ByteBuf buf)
    {
        super.writeDescPacket(buf);
        if (!useMetaForFacing())
        {
            buf.writeByte(getFacing() == null ? ForgeDirection.NORTH.ordinal() : getFacing().ordinal());
        }
    }

    /**
     * Gets the tile's facing direction
     *
     * @return direction
     */
    public ForgeDirection getFacing()
    {
        if (facing == null)
        {
            if (useMetaForFacing())
            {
                //Facing is used as an internal cache of the meta
                facing = ForgeDirection.getOrientation(getMetadata());
            }
            else
            {
                facing = ForgeDirection.NORTH;
            }
        }
        return facing;
    }

    /**
     * Called to set the facing direction
     *
     * @param facing - direction to face
     */
    public void setFacing(ForgeDirection facing)
    {
        this.facing = facing;
        if (useMetaForFacing())
        {
            //Facing is used as an internal cache of the meta
            this.setMeta(facing.ordinal());
        }
    }
}
