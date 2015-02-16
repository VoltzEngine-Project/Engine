package com.builtbroken.mc.core.content.tool;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.handler.SelectionHandler;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Location;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;


/**
 * Created by robert on 2/16/2015.
 */
public class ItemSelectionWand extends Item
{
    public ItemSelectionWand()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int d)
    {
        return Items.wooden_hoe.getIconFromDamage(d);
    }

    @Override @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world != null && player != null)
        {
            handelSelection(player, new Location(world, x, y, z));
        }
        return true;
    }

    public static void handelSelection(EntityPlayer player, Location location)
    {
        if (!location.world().isRemote)
        {
            Cube select = SelectionHandler.getSelection(player);

            if (player.isSneaking())
            {
                select.setPointOne(location.toVector3());
                if (Engine.runningAsDev)
                    player.addChatComponentMessage(new ChatComponentText(("Point One: " + select)));
            }
            else
            {
                select.setPointTwo(location.toVector3());
                if (Engine.runningAsDev)
                    player.addChatComponentMessage(new ChatComponentText(("Point Two: " + select)));
            }

            SelectionHandler.setSelection(player, select);
        }
    }
}
