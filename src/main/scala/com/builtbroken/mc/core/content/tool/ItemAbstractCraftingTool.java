package com.builtbroken.mc.core.content.tool;

import com.builtbroken.mc.api.items.tools.IItemTool;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2015.
 */
public abstract class ItemAbstractCraftingTool extends Item implements IItemTool, IRegistryInit
{
    public boolean ENABLE_TOOL_DAMAGE = true;

    private String configCat;

    public ItemAbstractCraftingTool(String configCatigory)
    {
        this.configCat = configCatigory;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        String type = getToolType(stack);
        if (type != null && !type.isEmpty())
        {
            return super.getUnlocalizedName() + "." + type;
        }
        return super.getUnlocalizedName();
    }

    @Override
    public String getToolType(ItemStack stack)
    {
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey("toolType") ? stack.getTagCompound().getString("toolType") : "";
    }

    @Override
    public void setToolType(ItemStack stack, String type)
    {
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());

        stack.getTagCompound().setString("toolType", type);
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack)
    {
        return !hasContainerItem(stack);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return stack.getItemDamage() <= getMaxDamage(stack);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        if (itemStack.getItemDamage() <= getMaxDamage(itemStack))
        {
            ItemStack stack = itemStack.copy();
            if (ENABLE_TOOL_DAMAGE)
                stack.setItemDamage(stack.getItemDamage() + 1);
            return stack;
        }
        return null;
    }

    @Override
    public void onRegistered()
    {
        this.ENABLE_TOOL_DAMAGE = Engine.instance.getConfig().getBoolean("EnableToolDamage", configCat + "Content", true, "Enables tools taking damage in crafting recipes");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientRegistered()
    {
    }
}
