package net.minecraft.src.universalelectricity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.universalelectricity.components.UniversalComponents;

/**
 * A block you may extend from to create your machine blocks! You do not have to extend from this block if 
 * you do not want to. It's optional but it comes with some useful functions that will make coding easier
 * for you.
 */
public abstract class UEBlockMachine extends BlockContainer
{
    public UEBlockMachine(String name, int id, Material material)
    {
        super(id, material);
        this.setBlockName(name);
        this.setHardness(0.5F);
    }
    
    @Override
    protected int damageDropped(int metadata)
    {
        return metadata;
    }
    
    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return this.blockID;
    }
    
    /**
     * Called when the block is right clicked by the player. This modified version detects
     * electric items and wrench actions on your machine block. Do not override this function.
     * Use machineActivated instead! (It does the same thing)
     */
    @Override
    public boolean blockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
    	int metadata = par1World.getBlockMetadata(x, y, z);
    	
    	/**
    	 * Check if the player is holding a wrench or an electric item. If so, do not open the GUI.
    	 */
    	if(par5EntityPlayer.inventory.getCurrentItem() != null)
    	{
    		if(par5EntityPlayer.inventory.getCurrentItem().itemID == UniversalComponents.ItemWrench.shiftedIndex)
        	{
    			if(this.onUseWrench(par1World, x, y, z, par5EntityPlayer))
    			{
    				par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
    				return true;
    			}
        	}
    		else if(par5EntityPlayer.inventory.getCurrentItem().getItem() instanceof UEElectricItem)
    		{
    			if(this.onUseElectricItem(par1World, x, y, z, par5EntityPlayer))
    			{
    				return true;
    			}
    		}
    	}
    	
    	return this.machineActivated(par1World, x, y, z, par5EntityPlayer);
    }
    
    public boolean machineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
		return false;
	}

    /**
     * Called when a player uses an electric item on the machine
     * @return True if some happens
     */
	public boolean onUseElectricItem(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
		return false;
	}

	/**
	 * Called when a player uses a wrench on the machine
	 * @return True if some happens
	 */
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
		return false;
	}

    /**
     * Returns the TileEntity used by this block. You should use the metadata sensitive version of this
     * to get the maximum optimization!
     */
    @Override
    public TileEntity getBlockEntity()
    {
        return null;
    }

    /**
     * Override this if you do not want your machine to be added to the creative menu or if you have
     * metadata machines and want to add more machines to the creative menu.
     */
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    	itemList.add(new ItemStack(this, 1, 0));
    }
}
