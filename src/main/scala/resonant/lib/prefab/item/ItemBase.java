package resonant.lib.prefab.item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Base Item class with useful functions
 * 
 * @author Darkguardsman */
public class ItemBase extends Item
{
    private LinkedHashMap<Integer, SubItem> subItems = new LinkedHashMap<Integer, SubItem>();
    private int lastMeta = 0;

    public ItemBase(int id)
    {
        super(id);
    }

    /** Adds a new sub item for the Item to create
     * 
     * @param name - name of the item, will be used for icon & unlocalizedName */
    public void addSubItem(String name)
    {
        addSubItem(lastMeta++, name);
    }

    /** Adds a new sub item for the Item to create
     * 
     * @param meta - index to use for the sub item
     * @param name - name of the item, will be used for icon & unlocalizedName */
    public void addSubItem(int meta, String name)
    {
        addSubItem(new SubItem(meta, name));
    }

    /** Adds a new sub item for the Item to create */
    public void addSubItem(SubItem item)
    {
        if (item != null)
        {
            if (!subItems.containsKey(item.meta()) || subItems.get(item.meta()) == null)
            {
                if (!this.getHasSubtypes())
                    this.setHasSubtypes(true);
                subItems.put(item.meta(), item);
            }
        }
    }

    /** Gets the sub item object for the meta */
    public SubItem getSubItem(int meta)
    {
        return subItems.containsKey(meta) ? subItems.get(meta) : null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int id, CreativeTabs tab, List list)
    {
        if (this.getHasSubtypes())
        {
            for (Entry<Integer, SubItem> item : subItems.entrySet())
            {
                list.add(new ItemStack(this, 1, item.getKey()));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister register)
    {
        if (this.getHasSubtypes())
        {
            for (Entry<Integer, SubItem> item : subItems.entrySet())
            {
                if (item.getValue() != null)
                {
                    item.getValue().loadIcon(register);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int meta)
    {
        if (getSubItem(meta) != null)
        {
            return getSubItem(meta).icon();
        }
        return super.getIconFromDamage(meta);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if (getSubItem(stack.getItemDamage()) != null)
        {
            return getSubItem(stack.getItemDamage()).unlocalizedName();
        }
        return super.getUnlocalizedName(stack);
    }

}
