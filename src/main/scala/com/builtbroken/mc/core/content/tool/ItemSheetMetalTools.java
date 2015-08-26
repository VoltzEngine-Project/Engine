package com.builtbroken.mc.core.content.tool;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Basic tool used in hand crafting recipes for sheet metal
 * Created by Dark on 8/25/2015.
 */
public class ItemSheetMetalTools extends Item implements IPostInit
{
    public static boolean ENABLE_TOOL_DAMAGE = true;

    @SideOnly(Side.CLIENT)
    IIcon hammer;

    @SideOnly(Side.CLIENT)
    IIcon shears;

    public ItemSheetMetalTools()
    {
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    @Override
    public void onPostInit()
    {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.hammer = reg.registerIcon(References.PREFIX + "sheetMetalHammer");
        this.shears = reg.registerIcon(References.PREFIX + "sheetMetalShears");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        list.add(getTool("hammer"));
        list.add(getTool("shear"));
    }

    public static ItemStack getTool(String type)
    {
        ItemStack stack = new ItemStack(Engine.itemSheetMetalTools);
        ((ItemSheetMetalTools) Engine.itemSheetMetalTools).setType(stack, type);
        return stack;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        String type = getType(stack);
        if (type != null && !type.isEmpty())
        {
            switch (type)
            {
                case "shear":
                    return shears;
                case "hammer":
                    return hammer;
            }
        }
        return Items.stone_hoe.getIcon(stack, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 1;
    }

    public String getType(ItemStack stack)
    {
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey("toolType") ? stack.getTagCompound().getString("toolType") : null;
    }

    public void setType(ItemStack stack, String type)
    {
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());

        stack.getTagCompound().setString("toolType", type);
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack)
    {
        return !(stack.getItemDamage() > 0);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        if (itemStack.getItemDamage() > 0)
        {
            ItemStack stack = itemStack.copy();
            if (ENABLE_TOOL_DAMAGE)
                stack.setItemDamage(stack.getItemDamage() - 1);
            return stack;
        }
        return null;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return !(stack.getItemDamage() > 0);
    }
}
