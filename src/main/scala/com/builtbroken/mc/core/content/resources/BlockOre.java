package com.builtbroken.mc.core.content.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import com.builtbroken.mc.core.References;

import java.util.List;

/** Generic ore block used by the resource generator
 * to quickly create ore blocks from its list
 *
 * Created by robert on 11/24/2014.
 */
public class BlockOre extends Block
{
    public IIcon[] icon = new IIcon[16];

    private String type;

    public BlockOre(String type)
    {
        super(Material.rock);
        this.type = type;
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
        for(MetallicOres ore : MetallicOres.values())
        {
            icon[ore.ordinal()] = reg.registerIcon(References.PREFIX + type + "_" + ore.name().toLowerCase() + "_ore");
        }
    }

    @SideOnly(Side.CLIENT) @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for(MetallicOres ore : MetallicOres.values())
        {
            list.add(new ItemStack(item, 1, ore.ordinal()));
        }
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return true;
    }
}
