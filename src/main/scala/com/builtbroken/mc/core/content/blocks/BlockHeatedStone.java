package com.builtbroken.mc.core.content.blocks;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.helper.MathUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Used by explosives to make stone hot to stand on
 * Created by robert on 2/24/2015.
 */
public class BlockHeatedStone extends Block
{
    protected BlockHeatedStone()
    {
        super(Material.rock);
        this.setHardness(1.5f);
        this.setResistance(10.0f);
        this.setBlockName(References.PREFIX + "hotrock");
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        //Sets the block to tick randomly so it can cool down
        if (world != null)
            world.scheduleBlockUpdate(x, y, z, this, getTicksForMeta(world.getBlockMetadata(x, y, z)));
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
        if(world.getBlockMetadata(x, y, z) > 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) - 1, 3);
            world.scheduleBlockUpdate(x, y, z, this, getTicksForMeta(world.getBlockMetadata(x, y, z)));
        }
        else
        {
            world.setBlock(x, y, z, Blocks.stone);
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List items)
    {
        items.add(new ItemStack(item, 1, 15));
    }
}
