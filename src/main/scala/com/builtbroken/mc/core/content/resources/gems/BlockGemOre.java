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
import net.minecraft.world.World;

import java.util.ArrayList;
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
    public int damageDropped(int meta)
    {
        return meta;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return icon[meta];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg)
    {
        for (GemOres ore : GemOres.values())
        {
            icon[ore.ordinal()] = reg.registerIcon(References.PREFIX + type + "_" + ore.name().toLowerCase() + "_ore");
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for (GemOres ore : GemOres.values())
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
        if (meta >= 0 && meta < GemOres.values().length)
        {
            Item item = GemOres.values()[meta].getOreItem();
            if (item != null)
            {
                return item;
            }
        }
        return Item.getItemFromBlock(this);
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        //TODO implement increased drop rate
        return super.quantityDropped(meta, fortune, random);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        int count = quantityDropped(metadata, fortune, world.rand);
        Item item = getItemDropped(metadata, world.rand, fortune);

        if (item != null)
        {
            ret.add(new ItemStack(item, count, item instanceof ItemGem ? GemOres.values()[metadata].gem.ordinal() : metadata));
        }

        return ret;
    }
}
