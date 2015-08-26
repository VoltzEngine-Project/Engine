package com.builtbroken.mc.core.content.tool;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.resources.ItemSheetMetal;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.lib.helper.recipe.UniversalRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

/**
 * Basic tool used in hand crafting recipes for sheet metal
 * Created by Dark on 8/25/2015.
 */
public class ItemSheetMetalTools extends Item implements IPostInit, IRegistryInit
{
    public static boolean ENABLE_TOOL_DAMAGE = true;
    public static int MAX_SHEARS_DAMAGE = 600;
    public static int MAX_HAMMER_DAMAGE = 800;

    @SideOnly(Side.CLIENT)
    IIcon hammer;

    @SideOnly(Side.CLIENT)
    IIcon shears;

    public ItemSheetMetalTools()
    {
        this.setMaxStackSize(1);
        this.setUnlocalizedName(References.PREFIX + "sheetMetalTools");
    }

    @Override
    public void onPostInit()
    {
        if (Engine.itemSheetMetal != null)
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Engine.itemSheetMetal, 1, ItemSheetMetal.SheetMetal.FULL.ordinal()), "IH", 'I', UniversalRecipe.PRIMARY_METAL.get(), 'H', getHammer()));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Engine.itemSheetMetal, 2, ItemSheetMetal.SheetMetal.HALF.ordinal()), "IC", 'I', new ItemStack(Engine.itemSheetMetal, 1, ItemSheetMetal.SheetMetal.FULL.ordinal()), 'C', getShears()));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Engine.itemSheetMetal, 2, ItemSheetMetal.SheetMetal.QUARTER.ordinal()), "IC", 'I', new ItemStack(Engine.itemSheetMetal, 1, ItemSheetMetal.SheetMetal.HALF.ordinal()), 'C', getShears()));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Engine.itemSheetMetal, 2, ItemSheetMetal.SheetMetal.EIGHTH.ordinal()), "IC", 'I', new ItemStack(Engine.itemSheetMetal, 1, ItemSheetMetal.SheetMetal.QUARTER.ordinal()), 'C', getShears()));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Engine.itemSheetMetal, 3, ItemSheetMetal.SheetMetal.THIRD.ordinal()), "I", "C", 'I', new ItemStack(Engine.itemSheetMetal, 1, ItemSheetMetal.SheetMetal.FULL.ordinal()), 'C', getShears()));

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Engine.itemSheetMetal, 2, ItemSheetMetal.SheetMetal.TRIANGLE.ordinal()), "I ", " C", 'I', new ItemStack(Engine.itemSheetMetal, 1, ItemSheetMetal.SheetMetal.FULL.ordinal()), 'C', getShears()));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Engine.itemSheetMetal, 1, ItemSheetMetal.SheetMetal.CONE.ordinal()), "I ", " H", 'I', new ItemStack(Engine.itemSheetMetal, 1, ItemSheetMetal.SheetMetal.TRIANGLE.ordinal()), 'H', getHammer()));
        }

        GameRegistry.addRecipe(new ShapedOreRecipe(getHammer(), "III", " I ", " S ", 'I', UniversalRecipe.PRIMARY_METAL.get(), 'S', Items.stick));
        GameRegistry.addRecipe(new ShapedOreRecipe(getShears(), "I I", " I ", "S S", 'I', UniversalRecipe.PRIMARY_METAL.get(), 'S', Items.stick));
    }

    @Override
    public void onRegistered()
    {
        this.ENABLE_TOOL_DAMAGE = Engine.instance.getConfig().getBoolean("EnableToolDamage", "SheetMetalContent", true, "Enables tools taking damage in crafting recipes");
        this.MAX_HAMMER_DAMAGE = Engine.instance.getConfig().getInt("MaxHammerDamage", "SheetMetalContent", MAX_HAMMER_DAMAGE, 10, 10000, "Max damage the sheet metal hammer can take before breaking");
        this.MAX_SHEARS_DAMAGE = Engine.instance.getConfig().getInt("MaxShearsDamage", "SheetMetalContent", MAX_SHEARS_DAMAGE, 10, 10000, "Max damage the sheet metal shears can take before breaking");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientRegistered()
    {

    }

    @Override
    public int getMaxDamage(ItemStack stack)
    {
        String type = getType(stack);
        if ("hammer".equals(type))
        {
            return MAX_HAMMER_DAMAGE;
        } else if ("shears".equals(type))
        {
            return MAX_SHEARS_DAMAGE;
        }
        return 100;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.hammer = reg.registerIcon(References.PREFIX + "sheetMetalHammer");
        this.shears = reg.registerIcon(References.PREFIX + "sheetMetalShears");
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        String type = getType(stack);
        if (type != null && !type.isEmpty())
        {
            return super.getUnlocalizedName() + "." + type;
        }
        return super.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        list.add(getHammer());
        list.add(getShears());
    }

    public static ItemStack getHammer()
    {
        return getTool("hammer");
    }

    public static ItemStack getShears()
    {
        return getTool("shears");
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
                case "shears":
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
