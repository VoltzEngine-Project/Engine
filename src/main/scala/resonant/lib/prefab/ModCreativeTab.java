package resonant.lib.prefab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Prefab creative tab to either create a fast creative tab or reduce code
 * need to make a more complex tab
 * Created by robert on 11/25/2014.
 */
public class ModCreativeTab extends CreativeTabs
{
    public ItemStack itemStack;

    public ModCreativeTab(String name)
    {
        super(name);
    }

    public ModCreativeTab(String name, Block block)
    {
        super(name);
        this.itemStack = new ItemStack(block);
    }

    public ModCreativeTab(String name, Item item)
    {
        super(name);
        this.itemStack = new ItemStack(item);
    }

    public ModCreativeTab(String name, ItemStack stack)
    {
        super(name);
        this.itemStack = stack;
    }

    @Override
    public ItemStack getIconItemStack()
    {
        if (itemStack == null)
        {
            itemStack = new ItemStack(Items.iron_door);
        }
        return itemStack;
    }

    @Override
    public Item getTabIconItem()
    {
        if (itemStack == null)
        {
            itemStack = new ItemStack(Items.iron_door);
        }
        return itemStack.getItem();
    }
}
