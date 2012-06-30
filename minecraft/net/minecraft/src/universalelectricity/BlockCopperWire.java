package net.minecraft.src.universalelectricity;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.ITextureProvider;

public class BlockCopperWire extends UEBlockConductor implements ITextureProvider
{	
	public BlockCopperWire(int id)
	{
		super(id, Material.cloth);
		this.setBlockName("Copper Wire");
		this.setStepSound(soundClothFootstep);
		this.setResistance(0.2F);
		this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
		this.setRequiresSelfNotify();
		this.blockIndexInTexture = 7;
	}
    
	/**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
	public void onNeighborBlockChange(World par1World, int x, int y, int z, int neighborID)
    {
    	super.onNeighborBlockChange(par1World, x, y, z, neighborID);
    	
    	TileEntityCopperWire tileEntity = (TileEntityCopperWire)par1World.getBlockTileEntity(x, y, z);
    	
        if(neighborID == Block.fire.blockID || neighborID == Block.lavaMoving.blockID || neighborID == Block.lavaStill.blockID || ((neighborID == Block.waterMoving.blockID || neighborID == Block.waterStill.blockID) && tileEntity.getStoredElectricity() > 0))
        {
        	par1World.setBlockWithNotify(x, y, z, Block.fire.blockID);
        	par1World.spawnParticle("largesmoke", x, y, z, 0, 0, 0);
        }
    }
	
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    /**
     * The type of render function that is called for this block
    */
    @Override
	public int getRenderType()
    {
        return -1;
    }
	
	/**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return UniversalElectricity.ItemCopperWire.shiftedIndex;
    }
    
    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

	@Override
	public TileEntity getBlockEntity()
    {
    	return new TileEntityCopperWire();
    }
	
    @Override
    public String getTextureFile()
    {
    	return UCBlock.textureFile;
    }
}