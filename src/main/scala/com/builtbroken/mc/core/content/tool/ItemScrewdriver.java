package com.builtbroken.mc.core.content.tool;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.tool.screwdriver.ToolMode;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.NBTUtility;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemScrewdriver extends Item
{
    public ItemScrewdriver()
    {
        this.setRegistryName(References.DOMAIN, "screwdriver");
        this.setUnlocalizedName(References.PREFIX + "screwdriver");
        this.setNoRepair();
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(LanguageUtility.getLocal("toolmode.mode") + ": " + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName()));

        String translation = LanguageUtility.getLocal("item.resonant:screwdriver.tooltip");
        tooltip.addAll(LanguageUtility.splitByLine(translation, 120)); //TODO test size
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
    {
       ItemStack itemStack = player.getHeldItem(handIn);
        // TODO: Fix this.
        if (player.isSneaking())
        {
            setMode(itemStack, (getMode(itemStack) + 1) % ToolMode.REGISTRY.size());

            if (!world.isRemote)
            {
                player.sendMessage(new TextComponentString(LanguageUtility.getLocal("item.resonant:screwdriver.toolmode.set") + LanguageUtility.getLocal(ToolMode.REGISTRY.get(getMode(itemStack)).getName())));
            }
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
        }

        return ToolMode.REGISTRY.get(getMode(itemStack)).onItemRightClick(itemStack, world, player, handIn);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        return ToolMode.REGISTRY.get(getMode(stack)).onItemUseFirst(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        return ToolMode.REGISTRY.get(getMode(stack)).onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, net.minecraft.world.IBlockAccess world, BlockPos pos, EntityPlayer player)
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
}
