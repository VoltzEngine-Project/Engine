package com.builtbroken.mc.core.content.resources.gems;

import com.builtbroken.mc.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Random;

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
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int var)
    {

        return Item.getItemFromBlock(this);
    }
}
