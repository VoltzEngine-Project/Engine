package com.builtbroken.mc.core.content.resources;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.content.resources.items.Gems;
import com.builtbroken.mc.core.content.resources.items.ItemGems;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * @author - Kolatra
 */
public class BlockGemOre extends Block
{
    public IIcon[] icon = new IIcon[16];

    private String type;

    public BlockGemOre(String type)
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
        for(GemOres ore : GemOres.values())
        {
            icon[ore.ordinal()] = reg.registerIcon(References.PREFIX + type + "_" + ore.name().toLowerCase() + "_ore");
        }
    }

    @SideOnly(Side.CLIENT) @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for(GemOres ore : GemOres.values())
        {
            list.add(new ItemStack(item, 1, ore.ordinal()));
        }
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune)
    {
        switch (meta)
        {
            case 1:
                return ItemGems.getItemById(1);
            case 2:
                return ItemGems.getItemById(2);
            case 3:
                return ItemGems.getItemById(3);
            case 4:
                return ItemGems.getItemById(4);
            case 5:
                return ItemGems.getItemById(5);
            case 6:
                return ItemGems.getItemById(6);
            case 7:
                return ItemGems.getItemById(7);
            case 8:
                return ItemGems.getItemById(8);
            case 9:
                return ItemGems.getItemById(9);
            default:
                return null;
        }
    }
}
