package com.builtbroken.mc.core.content.tool;

import buildcraft.api.tools.IToolWrench;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolMode;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.lib.helper.recipe.OreNames;
import com.builtbroken.mc.lib.recipe.item.RecipeTool;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.NBTUtility;

import java.util.List;

public class ItemScrewdriver extends Item implements IToolWrench, IPostInit
{
    public ItemScrewdriver()
    {
        this.setUnlocalizedName(References.PREFIX + "screwdriver");
        this.setTextureName(References.PREFIX + "screwdriver");
        this.setNoRepair();
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean canWrench(EntityPlayer entityPlayer, int x, int y, int z)
    {
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer entityPlayer, int x, int y, int z)
    {

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(LanguageUtility.getLocal("toolmode.mode") + ": " + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName()));
        par3List.addAll(LanguageUtility.splitStringPerWord(LanguageUtility.getLocal("item.resonant:screwdriver.tooltip"), 4));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        // TODO: Fix this.
        if (player.isSneaking())
        {
            setMode(itemStack, (getMode(itemStack) + 1) % ToolMode.REGISTRY.size());

            if (!world.isRemote)
            {
                player.addChatMessage(new ChatComponentText(LanguageUtility.getLocal("item.resonant:screwdriver.toolmode.set") + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName())));
            }
            return itemStack;
        }

        return ToolMode.REGISTRY.get(getMode(itemStack)).onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return ToolMode.REGISTRY.get(getMode(stack)).onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return ToolMode.REGISTRY.get(getMode(stack)).onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }

    public int getMode(ItemStack itemStack)
    {
        return NBTUtility.getNBTTagCompound(itemStack).getInteger("mode");
    }

    public void setMode(ItemStack itemStack, int mode)
    {
        NBTUtility.getNBTTagCompound(itemStack).setInteger("mode", mode);
    }

    @Override
    public void onPostInit()
    {
        if (Engine.itemSimpleCraftingTools != null)
        {
            GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemWrench), "RFH", "DRW", "SWI", 'F', ItemSimpleCraftingTool.getFile(), 'H', ItemSimpleCraftingTool.getHammer(), 'D', ItemSimpleCraftingTool.getDrill(), 'R', OreNames.ROD_STEEL, 'I', OreNames.INGOT_STEEL, 'W', OreNames.WOOD, 'S', OreNames.SCREW_STEEL));
            if (Engine.itemSheetMetalTools != null)
            {
                GameRegistry.addRecipe(new RecipeTool(new ItemStack(Engine.itemWrench), "RFH", "DRW", "SWI", 'F', ItemSimpleCraftingTool.getFile(), 'H', ItemSheetMetalTools.getHammer(), 'D', ItemSimpleCraftingTool.getDrill(), 'R', OreNames.ROD_STEEL, 'I', OreNames.INGOT_STEEL, 'W', OreNames.WOOD, 'S', OreNames.SCREW_STEEL));
            }
        }
    }
}
