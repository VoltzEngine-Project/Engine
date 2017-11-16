package com.builtbroken.mc.core.content.debug;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.helper.ReflectionUtility;
import com.builtbroken.mc.lib.world.map.block.ExtendedBlockDataManager;
import com.builtbroken.mc.prefab.items.ItemAbstract;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

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
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setTextureName(References.PREFIX + "devDataTool");
        this.setUnlocalizedName(References.PREFIX + "devDataTool");
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "item." + this.unlocalizedName + "." + stack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        list.add("Display data about a tile in chat");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase hit, EntityLivingBase player)
    {
        if (hit != null && player != null)
        {
            if (player instanceof EntityPlayer && !player.worldObj.isRemote)
            {
                ((EntityPlayer) player).addChatComponentMessage(new ChatComponentText("Entity: " + hit));
                ((EntityPlayer) player).addChatComponentMessage(new ChatComponentText("ID: " + hit.getEntityId()));
                ((EntityPlayer) player).addChatComponentMessage(new ChatComponentText("Name: " + hit.getCommandSenderName()));
                ((EntityPlayer) player).addChatComponentMessage(new ChatComponentText("Class: " + hit.getClass()));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            Block block = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
            TileEntity tile = world.getTileEntity(x, y, z);

            if (stack.getItemDamage() == 0)
            {
                player.addChatComponentMessage(new ChatComponentText("Block: " + block));
                player.addChatComponentMessage(new ChatComponentText(" Extended: " + ExtendedBlockDataManager.SERVER.getValue(world, x, y, z)));
                player.addChatComponentMessage(new ChatComponentText(" Meta: " + meta));
                player.addChatComponentMessage(new ChatComponentText(" Class: " + block.getClass()));
                if (tile != null)
                {
                    player.addChatComponentMessage(new ChatComponentText(" Tile: " + tile));
                    player.addChatComponentMessage(new ChatComponentText("  Class: " + tile.getClass()));
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
                                    player.addChatComponentMessage(new ChatComponentText("Field[" + (i++) + ", " + field.getName() + "] = " + obj));
                                    if (i % 5 == 0)
                                    {
                                        player.addChatComponentMessage(new ChatComponentText(""));
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        player.addChatComponentMessage(new ChatComponentText("No tile to pull data from"));
                    }
                }
                catch (Exception e)
                {
                    player.addChatComponentMessage(new ChatComponentText("Failed to reflect data! see log for details! " + e.getMessage()));
                    e.printStackTrace();
                }
            }
            //Meta only
            else if (stack.getItemDamage() == 2)
            {
                player.addChatComponentMessage(new ChatComponentText(" Meta: " + meta));
            }
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xf, float yf, float zf)
    {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }
}
