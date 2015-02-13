package com.builtbroken.mc.core.content;

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

/** Used to create a hole in the ground for looking at world generation amounts, caves, etc
 * Created by robert on 11/25/2014.
 */
public class ItemInstaHole extends Item
{
    public ItemInstaHole()
    {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @SideOnly(Side.CLIENT) @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return Items.stone_shovel.getIcon(stack, pass);
    }

    @SideOnly(Side.CLIENT) @Override
    public void registerIcons(IIconRegister p_94581_1_)
    {
        //No icon to register
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int xi, int yi, int zi, int side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote)
        {
            Chunk chunk = world.getChunkFromBlockCoords(xi, zi);
            int chunkX = chunk.xPosition << 4;
            int chunkZ = chunk.zPosition << 4;
            for(int y = 0; y < 256; y++)
            {
                for(int x = 0; x < 16; x++)
                {
                    for(int z = 0; z < 16; z++)
                    {
                        Block block = world.getBlock(x + chunkX, y, z + chunkZ);
                        if(!(block instanceof BlockOre || block instanceof net.minecraft.block.BlockOre || block == Blocks.bedrock && y == 0))
                        {
                            world.setBlockToAir(x + chunkX, y, z + chunkZ);
                        }
                        else if(block == Blocks.air)
                        {
                            if( y <= chunk.getHeightValue(chunk.xPosition + x, chunk.zPosition + y))
                            {
                                world.setBlock(x + chunkX, y, z + chunkZ, Blocks.glass);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
