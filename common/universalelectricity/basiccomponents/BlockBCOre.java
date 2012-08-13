package universalelectricity.basiccomponents;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

public class BlockBCOre extends Block
{
	protected BlockBCOre(int id)
	{
		super(id, 0, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockName("BCOre");
	}
	
    protected int damageDropped(int metadata)
    {
		return metadata;
    }
}
