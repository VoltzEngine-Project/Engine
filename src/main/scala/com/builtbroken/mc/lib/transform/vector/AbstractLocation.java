package com.builtbroken.mc.lib.transform.vector;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by robert on 1/13/2015.
 */
public abstract class AbstractLocation<R extends AbstractLocation> extends AbstractPos<R>
{
    public World world;

    public AbstractLocation(World world, double x, double y, double z)
    {
        super(x, y, z);
        this.world = world;
    }

    public AbstractLocation(NBTTagCompound nbt)
    {
        this(DimensionManager.getWorld(nbt.getInteger("dimension")), nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
    }

    public AbstractLocation(ByteBuf data)
    {
        this(DimensionManager.getWorld(data.readInt()), data.readDouble(), data.readDouble(), data.readDouble());
    }

    public AbstractLocation(Entity entity)
    {
        this(entity.worldObj, entity.posX, entity.posY, entity.posZ);
    }

    public AbstractLocation(TileEntity tile)
    {
        this(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public AbstractLocation(IWorldPosition vec)
    {
        this(vec.world(), vec.x(), vec.y(), vec.z());
    }

    public AbstractLocation(World world, IPos3D vector)
    {
        this(world, vector.x(), vector.y(), vector.z());
    }

    public AbstractLocation(World world, Vec3 vec)
    {
        this(world, vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public AbstractLocation(World world, MovingObjectPosition target)
    {
        this(world, target.hitVec);
    }

    public World world()
    {
        return world;
    }

    /**
     * Conversions
     */
    @Override
    public NBTTagCompound writeNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("dimension", world.provider.dimensionId);
        nbt.setDouble("x", x());
        nbt.setDouble("y", y());
        nbt.setDouble("z", z());
        return nbt;
    }

    @Override
    public ByteBuf writeByteBuf(ByteBuf data)
    {
        data.writeInt(world.provider.dimensionId);
        data.writeDouble(x());
        data.writeDouble(y());
        data.writeDouble(z());
        return data;
    }

    public Pos toVector3()
    {
        return new Pos(x(), y(), z());
    }

    /**
     * World Access
     */
    public Block getBlock()
    {
        if (world != null)
            return super.getBlock(world);
        else
            return null;
    }

    public int getBlockMetadata()
    {
        if (world != null)
            return super.getBlockMetadata(world);
        else
            return -1;
    }

    public TileEntity getTileEntity()
    {
        if (world != null)
            return super.getTileEntity(world);
        else
            return null;
    }

    public float getHardness()
    {
        return super.getHardness(world);
    }

    public float getResistance(Entity cause, double xx, double yy, double zz)
    {
        return getBlock(world).getExplosionResistance(cause, world, xi(), yi(), zi(), xx, yy, zz);
    }

    public boolean setBlock(Block block, int metadata, int notify)
    {
        return super.setBlock(world, block, metadata, notify);
    }

    public boolean setBlock(Block block, int metadata)
    {
        return super.setBlock(world, block, metadata);
    }

    public boolean setBlock(Block block)
    {
        return super.setBlock(world, block);
    }

    public boolean setBlockToAir()
    {
        return super.setBlockToAir(world);
    }

    public boolean isAirBlock()
    {
        return super.isAirBlock(world);
    }

    public boolean isBlockEqual(Block block)
    {
        return super.isBlockEqual(world, block);
    }

    public boolean isBlockFreezable()
    {
        return super.isBlockFreezable(world);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof IWorldPosition)
        {
            return (super.equals(obj)) && this.world == ((IWorldPosition) obj).world();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "VectorWorld [" + this.x() + "," + this.y() + "," + this.z() + "," + this.world + "]";
    }
}
