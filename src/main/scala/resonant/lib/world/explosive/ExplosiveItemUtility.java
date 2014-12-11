package resonant.lib.world.explosive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import resonant.api.explosive.IExplosive;
import resonant.api.items.IExplosiveItem;
import resonant.lib.type.Pair;
import resonant.lib.utility.LanguageUtility;

import java.util.List;

/**
 * Utility to ensure common read/write/access to item save data for explosive items
 * Created by robert on 11/18/2014.
 */
public class ExplosiveItemUtility
{

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
        if (stack != null && stack.getItem() instanceof IExplosiveItem)
        {
            IExplosive ex = ((IExplosiveItem) stack.getItem()).getExplosive(stack);
            if (ex != null)
            {
                Pair<Integer, Integer> ranges = getExplosive(stack).getEstimatedRange(null, ExplosiveItemUtility.getSize(stack));
                lines.add(LanguageUtility.getLocal("info.icbm:warhead.loaded") + ": " + LanguageUtility.getLocal(getExplosive(stack).getTranslationKey()));
                lines.add(LanguageUtility.getLocal("info.icbm:warhead.size") + ": " + ranges.left() + " - " + ranges.right() + " blocks");
            }
        }
    }

    /**
     * Used to generate a sub item per explosive type in the registry
     *
     * @param item - item to get sub items for
     * @param list - list to add sub item too
     */
    public static void getSubItems(Item item, List list)
    {
        //TODO link in with blacklist
        for (IExplosive ex : ExplosiveRegistry.getExplosives())
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
    public static IExplosive getExplosive(ItemStack itemStack)
    {
        return getExplosive(itemStack.getTagCompound());
    }

    /**
     * Gets explosive instance from NBT data
     *
     * @param tag - NBT tag to access
     * @return explosive instance, null if the tag is empty or explosive doesn't exist
     */
    public static IExplosive getExplosive(NBTTagCompound tag)
    {
        if (tag != null)
        {
            return ExplosiveRegistry.get(tag.getString("explosiveString"));
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
    public static void setExplosive(ItemStack itemStack, IExplosive ex)
    {
        if (ex != null)
        {
            if (itemStack.getTagCompound() == null)
                itemStack.setTagCompound(new NBTTagCompound());

            setExplosive(itemStack.getTagCompound(), ex);
        }
    }

    /**
     * Sets the explosive in the nbt tag
     *
     * @param tag - nbt tag to encode to
     * @param ex  - explosive name to encode
     */
    public static void setExplosive(NBTTagCompound tag, IExplosive ex)
    {
        if (ex != null)
        {
            tag.setString("explosiveString", ex.getID());
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
            itemStack.setTagCompound(new NBTTagCompound());

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
        return tag.getInteger("exSize");
    }

    /**
     * Sets the size of the explosive in the nbt
     *
     * @param itemStack - stack to encode nbt to
     * @param size      - size to set
     * @return nbt tag
     */
    public static NBTTagCompound setSize(ItemStack itemStack, int size)
    {
        if (itemStack.getTagCompound() == null)
            itemStack.setTagCompound(new NBTTagCompound());

        return setSize(itemStack.getTagCompound(), size);
    }

    /**
     * Sets the size of the explosive in the nbt
     *
     * @param tag  - nbt tag to encode to
     * @param size - size to set
     * @return nbt tag
     */
    public static NBTTagCompound setSize(NBTTagCompound tag, int size)
    {
        tag.setInteger("exSize", size);
        return tag;
    }
}
