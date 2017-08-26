package com.builtbroken.mc.core.content.debug;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.helper.ReflectionUtility;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataManager;
import com.builtbroken.mc.prefab.items.ItemAbstract;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

/**
 * Prints out simple data about the block/tile being clicked
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class ItemDevData extends ItemAbstract
{
    public ItemDevData()
    {
        this.setRegistryName(References.DOMAIN, "ve.devTool");
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(References.PREFIX + "devDataTool");
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return this.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag)
    {
        list.add("Display data about a tile in chat");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase hit, EntityLivingBase player)
    {
        if (hit != null && player != null)
        {
            if (player instanceof EntityPlayer && !player.world.isRemote)
            {
                ((EntityPlayer) player).sendMessage(new TextComponentString("Entity: " + hit));
                ((EntityPlayer) player).sendMessage(new TextComponentString("ID: " + hit.getEntityId()));
                ((EntityPlayer) player).sendMessage(new TextComponentString("Name: " + hit.getName()));
                ((EntityPlayer) player).sendMessage(new TextComponentString("Class: " + hit.getClass()));
            }
            return true;
        }
        return false;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote)
        {
            IBlockState block = world.getBlockState(pos);
            TileEntity tile = world.getTileEntity(pos);

            if (stack.getItemDamage() == 0)
            {
                player.sendMessage(new TextComponentString("Block: " + block));
                player.sendMessage(new TextComponentString(" Extended: " + ExtendedBlockDataManager.SERVER.getValue(world, pos.getX(), pos.getY(), pos.getZ())));
                player.sendMessage(new TextComponentString(" Class: " + block.getClass()));
                if (tile != null)
                {
                    player.sendMessage(new TextComponentString(" Tile: " + tile));
                    player.sendMessage(new TextComponentString("  Class: " + tile.getClass()));
                }
            }
            //Super tile data mode
            else if (stack.getItemDamage() == 1)
            {
                try
                {
                    if (tile != null)
                    {
                        int i = 0;
                        for (Field field : ReflectionUtility.getAllFields(tile.getClass()))
                        {
                            if (!Modifier.isStatic(field.getModifiers()))
                            {
                                field.setAccessible(true);
                                Object obj = field.get(tile);
                                if (!(obj instanceof Collection))
                                {
                                    player.sendMessage(new TextComponentString("Field[" + (i++) + ", " + field.getName() + "] = " + obj));
                                    if (i % 5 == 0)
                                    {
                                        player.sendMessage(new TextComponentString(""));
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponentString("No tile to pull data from"));
                    }
                }
                catch (Exception e)
                {
                    player.sendMessage(new TextComponentString("Failed to reflect data! see log for details! " + e.getMessage()));
                    e.printStackTrace();
                }
            }
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (this.isInCreativeTab(tab))
        {
            list.add(new ItemStack(this, 1, 0));
            list.add(new ItemStack(this, 1, 1));
        }
    }
}
