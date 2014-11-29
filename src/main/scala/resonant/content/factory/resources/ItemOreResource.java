package resonant.content.factory.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import resonant.lib.utility.LanguageUtility;

import java.util.List;

/**
 * Created by robert on 11/28/2014.
 */
public class ItemOreResource extends Item
{
    @Override
    public void onCreated(ItemStack p_77622_1_, World p_77622_2_, EntityPlayer p_77622_3_)
    {
        //TODO future implement achievements for crafting each tier of ore item
        //TODO ensure config for achievements so they can be turned off
    }

    @SideOnly(Side.CLIENT) @Override
    public IIcon getIconFromDamage(int meta)
    {
        return this.itemIcon;
    }

    @SideOnly(Side.CLIENT) @Override
    public void registerIcons(IIconRegister reg)
    {
        for(GeneratedOreItem item : OreItemRegistry.generators)
        {
            item.registerIcons(reg);
        }
    }

    @SideOnly(Side.CLIENT) @Override
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        //TODO link to registry for grey scale items
        return 16777215;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        super.addInformation(stack, player, list, b);
        list.add(LanguageUtility.getLocal("info.isCraftingOnlyItem"));
    }

    @SideOnly(Side.CLIENT) @Override
    public void getSubItems(Item  item, CreativeTabs tab, List items)
    {

    }
}
