package com.builtbroken.mc.core.content.blocks;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.helper.MathUtility;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Used by explosives to make stone hot to stand on
 * Created by robert on 2/24/2015.
 */
public class BlockHeatedStone extends Block
{
    public BlockHeatedStone()
    {
        super(Material.ROCK);
        this.setHardness(1.5f);
        this.setResistance(10.0f);
        this.setUnlocalizedName(References.PREFIX + "hotrock");
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        //Sets the block to tick randomly so it can cool down
        if (world != null)
        {
            world.scheduleBlockUpdate(pos, this, getTicksForMeta(getMetaFromState(state)), 1);
        }
    }

    private int getTicksForMeta(int meta)
    {
        if (meta > 0)
        {
            return (15 - meta) * 50 + MathUtility.rand.nextInt(100);
        }
        return 10 + MathUtility.rand.nextInt(100);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        //TODO handle meta change through heat map to reduce TPS problems & smooth transition
        if (getMetaFromState(state) > 0)
        {
            world.setBlockState(pos, getStateFromMeta(getMetaFromState(state) - 1));
            world.scheduleBlockUpdate(pos, this, getTicksForMeta(getMetaFromState(state)), 1);
        }
        else
        {
            short blockID = ExtendedBlockDataManager.SERVER.getValue((World) world, pos.getX(), pos.getY(), pos.getZ());
            if (blockID != 0)
            {
                //MMMM BBBB BBBB BBBB
                Block block = Block.getBlockById(blockID);
                if (block != null)
                {
                    world.setBlockState(pos, block.getDefaultState());
                }
                else
                {
                    world.setBlockState(pos, Blocks.STONE.getDefaultState());
                }
            }
            else
            {
                world.setBlockState(pos, Blocks.STONE.getDefaultState());
            }
        }
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity)
    {
        damageEntity(world, pos, entity);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        damageEntity(world, pos, entity);
    }

    protected void damageEntity(World world, BlockPos pos, Entity entity)
    {
        //TODO switch to applied heat method and use heat map to handle damage
        IBlockState state = world.getBlockState(pos);
        int meta = getMetaFromState(state);
        if (entity instanceof EntityLivingBase)
        {
            entity.attackEntityFrom(DamageSource.IN_FIRE, 0.1f * (meta / 16));
            if (world.rand.nextFloat() > ((16 - meta) / 16)) //meta 0 -> 100%, 16 -> 0%
            {
                entity.setFire(5);
            }
        }
        //TODO damage no living entities through specific typing, e.g. missiles will warp casing
    }

    @Override
    public void fillWithRain(World world, BlockPos pos)
    {
        if (world.rand.nextFloat() > 0.9)
        {
            updateTick(world, pos, world.getBlockState(pos), world.rand);
            //TODO add chance to crack stone
        }
    }

    public int colorMultiplier(int meta)
    {
        switch (meta)
        {
            //https://en.wikipedia.org/wiki/Color_temperature
            case 0:
                return Integer.parseInt("FFE5CE", 16);
            case 1:
                return Integer.parseInt("FFE5CE", 16);
            case 2:
                return Integer.parseInt("FFD4A8", 16);
            case 3:
                return Integer.parseInt("FFC07F", 16);
            case 4:
                return Integer.parseInt("FFBC76", 16);
            case 5:
                return Integer.parseInt("FFC07F", 16);
            case 6:
                return Integer.parseInt("FFBC76", 16);
            case 7:
                return Integer.parseInt("FFB569", 16);
            case 8:
                return Integer.parseInt("FFAA54", 16);
            case 9:
                return Integer.parseInt("FFA448", 16);
            case 10:
                return Integer.parseInt("FF9D3C", 16);
            case 11:
                return Integer.parseInt("FF8200", 16);
            case 12:
                return Integer.parseInt("FF7A00", 16);
            case 13:
                return Integer.parseInt("FF7A00", 16);
            case 14:
                return Integer.parseInt("FF7A00", 16);
            case 15:
                return Integer.parseInt("FF7A00", 16);
        }
        return 16777215;
    }
}
