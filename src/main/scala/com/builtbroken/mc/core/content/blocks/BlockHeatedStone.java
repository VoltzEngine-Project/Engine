package com.builtbroken.mc.core.content.blocks;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.helper.MathUtility;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Used by explosives to make stone hot to stand on
 * Created by robert on 2/24/2015.
 */
public class BlockHeatedStone extends Block
{
    public BlockHeatedStone()
    {
        super(Material.rock);
        this.setHardness(1.5f);
        this.setResistance(10.0f);
        this.setBlockName(References.PREFIX + "hotrock");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.stone.getIcon(side, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        if (world instanceof World)
        {
            short blockID = ExtendedBlockDataManager.INSTANCE.getValue((World) world, x, y, z);
            if (blockID != 0)
            {
                //MMMM BBBB BBBB BBBB
                Block block = Block.getBlockById(blockID);
                if (block != null)
                {
                    return block.getIcon(side, 0);
                }
            }
        }
        return Blocks.stone.getIcon(side, 0);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        //Sets the block to tick randomly so it can cool down
        if (world != null)
        {
            world.scheduleBlockUpdate(x, y, z, this, getTicksForMeta(world.getBlockMetadata(x, y, z)));
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
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        //TODO handle meta change through heat map to reduce TPS problems & smooth transition
        if (world.getBlockMetadata(x, y, z) > 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) - 1, 3);
            world.scheduleBlockUpdate(x, y, z, this, getTicksForMeta(world.getBlockMetadata(x, y, z)));
        }
        else
        {
            short blockID = ExtendedBlockDataManager.INSTANCE.getValue((World) world, x, y, z);
            if (blockID != 0)
            {
                //MMMM BBBB BBBB BBBB
                Block block = Block.getBlockById(blockID);
                if (block != null)
                {
                    world.setBlock(x, y, z, block);
                }
                else
                {
                    world.setBlock(x, y, z, Blocks.stone);
                }
            }
            else
            {
                world.setBlock(x, y, z, Blocks.stone);
            }
        }
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity)
    {
        damageEntity(world, x, y, z, entity);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        damageEntity(world, x, y, z, entity);
    }

    protected void damageEntity(World world, int x, int y, int z, Entity entity)
    {
        //TODO switch to applied heat method and use heat map to handle damage
        int meta = world.getBlockMetadata(x, y, z);
        if (entity instanceof EntityLivingBase)
        {
            entity.attackEntityFrom(DamageSource.inFire, 0.1f * (meta / 16));
            if (world.rand.nextFloat() > ((16 - meta) / 16)) //meta 0 -> 100%, 16 -> 0%
            {
                entity.setFire(5);
            }
        }
        //TODO damage no living entities through specific typing, e.g. missiles will warp casing
    }

    @Override
    public void fillWithRain(World world, int x, int y, int z)
    {
        if (world.rand.nextFloat() > 0.9)
        {
            updateTick(world, x, y, z, world.rand);
            //TODO add chance to crack stone
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List items)
    {
        items.add(new ItemStack(item, 1, 15));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
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
