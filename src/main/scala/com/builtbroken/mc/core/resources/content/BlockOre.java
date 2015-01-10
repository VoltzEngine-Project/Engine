package com.builtbroken.mc.core.resources.content;

import com.builtbroken.mc.core.resources.DefinedResources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import com.builtbroken.mc.core.References;

import java.util.List;

/** Generic ore block used by the resource generator
 * to quickly create ore blocks from its list
 *
 * Created by robert on 11/24/2014.
 */
public class BlockOre extends Block
{
    public int set = 0;
    public IIcon[] icon = new IIcon[16];

    public BlockOre()
    {
        super(Material.rock);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int damageDropped(int m)
    {
        return m;
    }

    @SideOnly(Side.CLIENT) @Override
    public IIcon getIcon(int side, int meta)
    {
        return icon[meta];
    }

    @SideOnly(Side.CLIENT) @Override
    public void registerBlockIcons(IIconRegister reg)
    {
        for(int i = 0; i < 16; i++)
        {
            //TODO change this to not use ore dictionary
            icon[i] = reg.registerIcon(References.PREFIX + OreDictionary.getOreName(OreDictionary.getOreIDs(new ItemStack(this, 1, i))[0]));
        }
    }

    @SideOnly(Side.CLIENT) @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for(int i = set * 16; i < (set * 16) + 16 && i < DefinedResources.values().length; i++)
        {
            DefinedResources re = DefinedResources.values()[i];
            if(re.block == this && re.generateOres && re.requested)
            {
                list.add(new ItemStack(item, 1, re.meta));
            }
        }
    }
}
