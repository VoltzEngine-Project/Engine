package com.builtbroken.mc.lib.transform.vector;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class Location extends AbstractLocation<Location> implements IWorldPosition, IPos3D
{
    public Location(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public Location(NBTTagCompound nbt)
    {
        this(DimensionManager.getWorld(nbt.getInteger("dimension")), nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
    }

    public Location(ByteBuf data)
    {
        this(DimensionManager.getWorld(data.readInt()), data.readDouble(), data.readDouble(), data.readDouble());
    }

    public Location(Entity entity)
    {
        this(entity.worldObj, entity.posX, entity.posY, entity.posZ);
    }

    public Location(TileEntity tile)
    {
        this(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public Location(IWorldPosition vec)
    {
        this(vec.world(), vec.x(), vec.y(), vec.z());
    }

    public Location(World world, IPos3D vector)
    {
        this(world, vector.x(), vector.y(), vector.z());
    }

    public Location(World world, Vec3 vec)
    {
        this(world, vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Location(World world, MovingObjectPosition target)
    {
        this(world, target.hitVec);
    }

    @Override
    public Location newPos(double x, double y, double z)
    {
        return new Location(world, x, y, z);
    }

    public void playSound(String sound, float volume, float pitch)
    {
        world().playSound(x(), y(), z(), sound, volume, pitch, false);
    }

    public void playSound(Block block)
    {
        Block.SoundType soundtype = block.stepSound;
        playSound(soundtype.getStepResourcePath(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(String t, IPos3D vel)
    {
        spawnParticle(t, vel.x(), vel.y(), vel.z());
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(String t, double vel_x, double vel_y, double vel_z)
    {
        world().spawnParticle(t, x(), y(), z(), vel_x, vel_y, vel_z);
    }

    @SideOnly(Side.CLIENT)
    public void playBlockBreakAnimation()
    {
        Block block = getBlock();
        if (block != null && block.getMaterial() != Material.air)
        {
            //Play block sound
            playSound(block);

            //Spawn random particles
            Random rand = world().rand;
            for (int i = 0; i < 3 + rand.nextInt(20); i++)
            {
                Location v = addRandom(rand, 0.5);
                Pos vel = new Pos().addRandom(rand, 0.2);
                v.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + v.getBlockMetadata(), vel);
            }
        }
    }

    public boolean isSideSolid(ForgeDirection side)
    {
        return getBlock().isSideSolid(world(), xi(), yi(), zi(), side);
    }
}