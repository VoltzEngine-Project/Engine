package com.builtbroken.mc.core.content.debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Used to create a hole in the ground for looking at world generation amounts, caves, etc
 * Created by robert on 11/25/2014.
 */
@Deprecated
public class ItemInstaHole extends Item
{
    public ItemInstaHole()
    {
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta)
    {
        return Items.stone_shovel.getIconFromDamage(meta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister p_94581_1_)
    {
        //No icon to register
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int xi, int yi, int zi, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            Chunk chunk = world.getChunkFromBlockCoords(xi, zi);
            int chunkX = chunk.xPosition << 4;
            int chunkZ = chunk.zPosition << 4;
            for (int y = 1; y < 256; y++)
            {
                for (int x = 0; x < 16; x++)
                {
                    for (int z = 0; z < 16; z++)
                    {
                        Block block = world.getBlock(x + chunkX, y, z + chunkZ);
                        if (block == Blocks.stone || block == Blocks.sand || block == Blocks.sandstone || block == Blocks.dirt || block == Blocks.grass || block == Blocks.gravel)
                        {
                            world.setBlockToAir(x + chunkX, y, z + chunkZ);
                        }
                        else if (block == Blocks.water || block == Blocks.flowing_water)
                        {
                            world.setBlock(x + chunkX, y, z + chunkZ, Blocks.stained_glass, 4, 2);
                        }
                        else if (block == Blocks.lava || block == Blocks.flowing_lava)
                        {
                            world.setBlock(x + chunkX, y, z + chunkZ, Blocks.stained_glass, 1, 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
