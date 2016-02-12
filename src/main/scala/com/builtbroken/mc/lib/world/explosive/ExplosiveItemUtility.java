package com.builtbroken.mc.lib.world.explosive;

import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.items.IExplosiveItem;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility to ensure common read/write/access to item save data for explosive items
 * Created by robert on 11/18/2014.
 */
public class ExplosiveItemUtility
{
    public static final String EXPLOSIVE_SAVE = "explosiveString";
    public static final String SIZE_SAVE = "explosiveSize";

    /**
     * Adds information to the tool tip of the item
     *
     * @param stack  - itemstack to get the tooltip info for
     * @param player - player who is viewing the item
     * @param lines  - list of lines to add to the tool tip
     * @param b      - unknown?
     */
    public static void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean b)
    {
        if (stack != null)
        {
            if (stack.getItem() instanceof IExplosiveItem)
            {
                addInformation(stack, ((IExplosiveItem) stack.getItem()).getExplosive(stack), player, lines, b);
            }
            else
            {
                lines.add(LanguageUtility.getLocalName("error.stack.invalid.IExplosiveItem"));
            }
        }
        else
        {
            lines.add(LanguageUtility.getLocalName("error.null_stack"));
        }
    }

    /**
     * Adds information to the tool tip of the item
     *
     * @param stack  - itemstack to get the tooltip info for
     * @param player - player who is viewing the item
     * @param lines  - list of lines to add to the tool tip
     * @param b      - unknown?
     */
    public static void addInformation(ItemStack stack, IExplosiveHandler ex, EntityPlayer player, List lines, boolean b)
    {
        if (stack != null)
        {
            if (ex != null)
            {
                List<String> l = new ArrayList();
                ex.addInfoToItem(player, stack, l);
                for (String s : l)
                {
                    lines.add(s);
                }
            }
            else
            {
                lines.add(LanguageUtility.getLocalName("explosive.error.null_ex"));
            }
        }
        else
        {
            lines.add(LanguageUtility.getLocalName("error.null_stack"));
        }
    }

    /**
     * Used to generate a sub item per explosive type in the registry
     *
     * @param item - item to get sub items for
     * @param list - list to add sub item too
     */
    @Deprecated
    public static void getSubItems(Item item, List list)
    {
        //TODO link in with blacklist
        for (IExplosiveHandler ex : ExplosiveRegistry.getExplosives())
        {
            ItemStack stack = new ItemStack(item);
            ExplosiveItemUtility.setExplosive(stack, ex);
            ExplosiveItemUtility.setSize(stack, 1);
            list.add(stack);
        }
    }

    /**
     * Gets an explosive instance from the itemstack
     *
     * @param itemStack - stack to access NBT
     * @return explosive instance of null if the explosive doesn't exist or NBT is empty
     */
    public static IExplosiveHandler getExplosive(ItemStack itemStack)
    {
        return getExplosive(itemStack.getTagCompound());
    }

    /**
     * Gets explosive instance from NBT data
     *
     * @param tag - NBT tag to access
     * @return explosive instance, null if the tag is empty or explosive doesn't exist
     */
    public static IExplosiveHandler getExplosive(NBTTagCompound tag)
    {
        if (tag != null)
        {
            return ExplosiveRegistry.get(tag.getString(EXPLOSIVE_SAVE));
        }
        return null;
    }

    /**
     * Sets the explosive in the itemstack's nbt
     *
     * @param itemStack - - stack to access NBT
     * @param ex        - explosive name to encode
     */
    public static void setExplosive(ItemStack itemStack, String ex)
    {
        setExplosive(itemStack, ExplosiveRegistry.get(ex));
    }

    /**
     * Sets the explosive in the itemstack's nbt
     *
     * @param itemStack - - stack to access NBT
     * @param ex        - explosive name to encode
     */
    public static void setExplosive(ItemStack itemStack, IExplosiveHandler ex)
    {
        if (ex != null)
        {
            if (itemStack.getTagCompound() == null)
            {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            setExplosive(itemStack.getTagCompound(), ex);
        }
        else if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey(ExplosiveItemUtility.EXPLOSIVE_SAVE))
        {
            itemStack.getTagCompound().removeTag(ExplosiveItemUtility.EXPLOSIVE_SAVE);
        }
    }

    /**
     * Sets the explosive in the nbt tag
     *
     * @param tag - nbt tag to encode to
     * @param ex  - explosive name to encode
     */
    public static void setExplosive(NBTTagCompound tag, IExplosiveHandler ex)
    {
        if (ex != null)
        {
            tag.setString(EXPLOSIVE_SAVE, ex.getID());
        }
    }

    /**
     * Gets the explosive size from the itemstack
     *
     * @param itemStack - stack to access NBT
     * @return
     */
    public static int getSize(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return getSize(itemStack.getTagCompound());
    }

    /**
     * Gets the explosive size from nbt
     *
     * @param tag - nbt tag to access
     * @return
     */
    public static int getSize(NBTTagCompound tag)
    {
        return tag.getInteger(SIZE_SAVE);
    }

    /**
     * Sets the size of the explosive in the nbt
     *
     * @param itemStack - stack to encode nbt to
     * @param size      - size to set
     * @return nbt tag
     */
    public static NBTTagCompound setSize(ItemStack itemStack, double size)
    {
        if (size != 0)
        {
            if (itemStack.getTagCompound() == null)
            {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            return setSize(itemStack.getTagCompound(), size);
        }
        else if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey(ExplosiveItemUtility.SIZE_SAVE))
        {
            itemStack.getTagCompound().removeTag(ExplosiveItemUtility.SIZE_SAVE);
        }
        return itemStack.getTagCompound();
    }

    /**
     * Sets the size of the explosive in the nbt
     *
     * @param tag  - nbt tag to encode to
     * @param size - size to set
     * @return nbt tag
     */
    public static NBTTagCompound setSize(NBTTagCompound tag, double size)
    {
        tag.setDouble(SIZE_SAVE, size);
        return tag;
    }
}
