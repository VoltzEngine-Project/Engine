package com.builtbroken.mc.core.content.blocks;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.helper.MathUtility;
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
        if (world.getBlockMetadata(x, y, z) > 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) - 1, 3);
            world.scheduleBlockUpdate(x, y, z, this, getTicksForMeta(world.getBlockMetadata(x, y, z)));
        }
        else
        {
            world.setBlock(x, y, z, Blocks.stone);
        }
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            entity.attackEntityFrom(DamageSource.inFire, 0.1f);
            entity.setFire(5);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            entity.attackEntityFrom(DamageSource.inFire, 0.5f);
            entity.setFire(5);
        }
    }

    @Override
    public void fillWithRain(World world, int x, int y, int z)
    {
        if (world.rand.nextFloat() > 0.9)
        {
            updateTick(world, x, y, z, world.rand);
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
            case 0:
                return Integer.parseInt("FFE6E6", 16);
            case 1:
                return Integer.parseInt("FFCCCC", 16);
            case 2:
                return Integer.parseInt("FFB2B2", 16);
            case 3:
                return Integer.parseInt("FF9999", 16);
            case 4:
                return Integer.parseInt("FF8080", 16);
            case 5:
                return Integer.parseInt("FF6666", 16);
            case 6:
                return Integer.parseInt("FF4D4D", 16);
            case 7:
                return Integer.parseInt("FF3333", 16);
            case 8:
                return Integer.parseInt("FF1919", 16);
            case 9:
                return Integer.parseInt("FF0000", 16);
            case 10:
                return Integer.parseInt("E60000", 16);
            case 11:
                return Integer.parseInt("B20000", 16);
            case 12:
            case 13:
            case 14:
            case 15:
                return Integer.parseInt("990000", 16);
        }
        return 16777215;
    }
}
