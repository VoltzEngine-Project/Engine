package net.minecraft.src.universalelectricity;

import java.util.ArrayList;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.universalelectricity.api.UniversalOreData;

/**
 * A class used by Universal Electricity to create metadata ores. You can ignore this class.
 * @author Henry
 *
 */
public class UEBlockOre extends Block
{
	//A list containing all the ores.
	public UniversalOreData[] ores = new UniversalOreData[16];
	
	public UEBlockOre(int id)
	{
		super(id, Material.rock);
		this.setBlockName("Ore");
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundStoneFootstep);
	}
	
	@Override
	protected int damageDropped(int metadata)
    {
        return metadata;
    }

	/**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    @Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
    {
    	return ores[metadata].getBlockTextureFromSide(side);
    }
	
	@Override
	public void addCreativeItems(ArrayList itemList)
    {
		for(int i = 0; i < ores.length; i++)
		{
			if(ores[i] != null)
			{
				if(ores[i].addToCreativeList())
				{
					itemList.add(new ItemStack(this, 1, i));
				}
			}
		}
    }
}
