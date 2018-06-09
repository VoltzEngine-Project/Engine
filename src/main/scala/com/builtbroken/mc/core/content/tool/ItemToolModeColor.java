package com.builtbroken.mc.core.content.tool;

import com.builtbroken.mc.api.items.tools.IItemMouseScroll;
import com.builtbroken.mc.api.tile.connection.ConnectionColor;
import com.builtbroken.mc.lib.helper.NBTUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Prefab for tools that support a mix of modes and colors for interaction
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/9/2018.
 */
public abstract class ItemToolModeColor extends Item implements IItemMouseScroll
{
    public static final String NBT_COLOR = "toolColor";
    public static final String NBT_MODE = "toolMode";

    @Override
    public Result onMouseWheelScrolled(EntityPlayer player, ItemStack stack, boolean ctrlHeld, boolean shiftHeld, boolean scrolledForward)
    {
        if (shiftHeld)
        {
            //Send packet to server
            if (player.worldObj.isRemote)
            {
                return Result.SERVER;
            }

            if (ctrlHeld)
            {
                toggleMode(stack, scrolledForward);
            }
            else
            {
                toggleColor(stack, scrolledForward);
            }

            return Result.SERVER;
        }
        return Result.PASS;
    }

    public void toggleMode(ItemStack stack, boolean forward)
    {
        int mode = getMode(stack);
        if (forward)
        {
            mode += 1;
            if (mode >= getModeCount(stack))
            {
                mode = 0;
            }
        }
        else
        {
            mode -= 1;
            if (mode < 0)
            {
                mode = getModeCount(stack) - 1;
            }
        }
        setMode(stack, mode);
    }

    /**
     * Number of modes the tool has
     *
     * @param stack
     * @return
     */
    public abstract int getModeCount(ItemStack stack);

    public int getMode(ItemStack itemStack)
    {
        return NBTUtility.getNBTTagCompound(itemStack).getInteger(NBT_MODE);
    }

    public void setMode(ItemStack itemStack, int mode)
    {
        NBTUtility.getNBTTagCompound(itemStack).setInteger(NBT_MODE, mode);
    }

    public void toggleColor(ItemStack stack, boolean forward)
    {
        setColor(stack, forward ? getColor(stack).next() : getColor(stack).prev());
    }

    public ConnectionColor getColor(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            return ConnectionColor.get(stack.getTagCompound().getInteger(NBT_COLOR));
        }
        return ConnectionColor.RED;
    }

    public void setColor(ItemStack stack, ConnectionColor color)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger(NBT_COLOR, color.ordinal());
    }
}
