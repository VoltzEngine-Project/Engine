package universalelectricity.basiccomponents;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import universalelectricity.BasicComponents;

public class BlockBCOre extends Block
{
	public BlockBCOre(int id)
	{
		super(id, 14, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockName("BCOre");
        this.setHardness(2f);
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
    {
        return this.blockIndexInTexture+metadata;
    }
	
	@Override
	public int damageDropped(int metadata)
    {
		return metadata;
    }
    
    @Override
    public String getTextureFile()
    {
        return BasicComponents.BLOCK_TEXTURE_FILE;
    }
    
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	par3List.add(new ItemStack(par1, 1, 0));
    	par3List.add(new ItemStack(par1, 1, 1));
    }
}
