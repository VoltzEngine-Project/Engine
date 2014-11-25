package resonant.engine.content;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import resonant.content.factory.resources.DefinedResources;

import java.util.List;

/**
 * Created by robert on 11/24/2014.
 */
public class BlockOre extends Block
{
    public int set = 0;

    public BlockOre()
    {
        super(Material.rock);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @SideOnly(Side.CLIENT) @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for(int i = set * 16; i < (set * 16) + 16 && i < DefinedResources.values().length; i++)
        {
            DefinedResources re = DefinedResources.values()[i];
            if(re.block == this && re.requested)
            {
                list.add(new ItemStack(item, 1, re.meta));
            }
        }
    }
}
