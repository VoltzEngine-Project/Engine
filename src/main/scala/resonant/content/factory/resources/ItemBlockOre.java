package resonant.content.factory.resources;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import resonant.content.prefab.itemblock.ItemBlockMetadata;
import resonant.engine.References;

/**
 * Created by robert on 11/25/2014.
 */
public class ItemBlockOre extends ItemBlockMetadata
{
    public ItemBlockOre(Block block)
    {
        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        String s = OreDictionary.getOreName(OreDictionary.getOreID(new ItemStack(this, 1, itemstack.getItemDamage())));
        if(s != null && !s.isEmpty())
        {
            return "tile." + References.PREFIX + s;
        }
        return super.getUnlocalizedName(itemstack);
    }
}
