package com.builtbroken.mc.core.content.resources.gems;

import com.builtbroken.mc.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/28/2016.
 */
@Deprecated //TODO move to JSON
public class ItemGem extends Item
{
    public final GemTypes itemType;

    /**
     * @param itemType - type of item this is, should not be duplicated and should never be null
     * @throws RuntimeException if ItemType is null
     */
    public ItemGem(GemTypes itemType)
    {
        if (itemType == null)
        {
            throw new RuntimeException("Item type can not be null for ItemGenMaterial");
        }

        this.itemType = itemType;
        this.setUnlocalizedName(References.PREFIX + itemType.name().toLowerCase());
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        itemType.icons = new IIcon[Gems.values().length];
        for (Gems gem : Gems.values())
        {
            if(gem != Gems.UNKNOWN)
            {
                itemType.icons[gem.ordinal()] = reg.registerIcon(References.PREFIX + "gem." + gem.name + "." + itemType.name);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        if (meta >= 0 && meta < itemType.icons.length)
        {
            return itemType.icons[meta];
        }
        return this.itemIcon;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "item." + References.PREFIX + "gem." + getMaterial(stack).name + "." + itemType.name;
    }

    public Gems getMaterial(ItemStack stack)
    {
        if (stack.getItemDamage() >= 0 && stack.getItemDamage() < Gems.values().length)
        {
            return Gems.values()[stack.getItemDamage()];
        }
        return Gems.UNKNOWN;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (Gems mat : Gems.values())
        {
            if (mat != Gems.UNKNOWN && !itemType.ignoreMaterials.contains(mat))
            {
                list.add(new ItemStack(item, 1, mat.ordinal()));
            }
        }
    }
}
