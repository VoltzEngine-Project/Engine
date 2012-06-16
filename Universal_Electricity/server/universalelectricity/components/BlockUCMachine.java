package net.minecraft.src.universalelectricity.components;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_UniversalElectricity;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.universalelectricity.UEBlockMachine;
import net.minecraft.src.universalelectricity.UEIPowerRedstone;
import net.minecraft.src.universalelectricity.UEIRotatable;

/**
 * The metadata machine class for all Universal Components machines
 * 0 - Battery Box
 * 1 - Coal Generator
 * 2 - Electric Furnace
 * @author Henry
 *
 */

public class BlockUCMachine extends UEBlockMachine implements ITextureProvider
{
    public BlockUCMachine(int id, int textureIndex)
    {
    	super("UCMachine", id, Material.wood);
    	this.blockIndexInTexture = textureIndex;
    	this.setStepSound(soundMetalFootstep);
    	this.setRequiresSelfNotify();
    	this.setTickRandomly(true);
    }
    
    @Override
    public String getTextureFile()
    {
    	return UCBlock.textureFile;
    }
    
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == 0 || side == 1)
        {
            return this.blockIndexInTexture;
        }
        else
        {
        	//If it is the front side
        	if(side == 3)
        	{
        		switch(metadata)
        		{
        			case 0: return this.blockIndexInTexture + 3;
        			case 1: return this.blockIndexInTexture + 5;
        			case 2: return this.blockIndexInTexture + 6;
        		}
        	}
        	//If it is the back side
        	else if(side == 2)
        	{
        		switch(metadata)
        		{
        			case 0: return this.blockIndexInTexture + 2;
        			case 1: return this.blockIndexInTexture + 3;
        			case 2: return this.blockIndexInTexture + 2;
        		}
        	}

        	if(metadata == 0)
            {
            	return this.blockIndexInTexture + 4;
            }
            else
            {
            	return this.blockIndexInTexture+1;
            }
        }
	}
    
    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
    {
        int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int metadata = par1World.getBlockMetadata(x, y, z);
        UEIRotatable tileEntity = (UEIRotatable)par1World.getBlockTileEntity(x, y, z);
        
        if(metadata == 0)
        {
	        switch (angle)
	        {
	        	case 0: tileEntity.setDirection((byte)5); break;
	        	case 1: tileEntity.setDirection((byte)3); break;
	        	case 2: tileEntity.setDirection((byte)4); break;
	        	case 3: tileEntity.setDirection((byte)2); break;
	        }
        }
        else
        {
        	switch (angle)
	        {
	        	case 0: tileEntity.setDirection((byte)3); break;
	        	case 1: tileEntity.setDirection((byte)4); break;
	        	case 2: tileEntity.setDirection((byte)2); break;
	        	case 3: tileEntity.setDirection((byte)5); break;
	        }
        }
        
        par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
    }
    
    @Override
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
    	UEIRotatable tileEntity = (UEIRotatable)par1World.getBlockTileEntity(x, y, z);

    	//Reorient the block
		switch(tileEntity.getDirection())
		{
			case 2: tileEntity.setDirection((byte)5); break;
	    	case 5: tileEntity.setDirection((byte)3); break;
	    	case 3: tileEntity.setDirection((byte)4); break;
	    	case 4: tileEntity.setDirection((byte)2); break;
		}
		
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		
		return true;
    }
    
    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean machineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
		int metadata = par1World.getBlockMetadata(x, y, z);

        if (!par1World.isRemote)
        {
        	
            TileEntity tileEntity = par1World.getBlockTileEntity(x, y, z);

            if (tileEntity != null)
            {
            	switch(metadata)
            	{
	            	case 0: par5EntityPlayer.openGui(new mod_UniversalElectricity(), 1997, par1World, x, y, z); break;
	            	/*case 1:	ModLoader.openGUI(par5EntityPlayer, new GUICoalGenerator(par5EntityPlayer.inventory, ((TileEntityCoalGenerator)tileEntity))); break;
	            	case 2:	ModLoader.openGUI(par5EntityPlayer, new GUIElectricFurnace(par5EntityPlayer.inventory, ((TileEntityElectricFurnace)tileEntity))); break;
            	*/}
            	
            	return true;
            }
        }
        
        return false;
    }
    
    /**
     * Called whenever the block is removed.
     */
    @Override
    public void onBlockRemoval(World par1World, int par2, int par3, int par4)
    {
    	IInventory tileEntity = (IInventory)par1World.getBlockTileEntity(par2, par3, par4);

        if (tileEntity != null)
        {
            for (int var6 = 0; var6 < tileEntity.getSizeInventory(); ++var6)
            {
                ItemStack var7 = tileEntity.getStackInSlot(var6);

                if (var7 != null)
                {
                	Random random = new Random();
                    float var8 = random.nextFloat() * 0.8F + 0.1F;
                    float var9 = random.nextFloat() * 0.8F + 0.1F;
                    float var10 = random.nextFloat() * 0.8F + 0.1F;

                    while (var7.stackSize > 0)
                    {
                        int var11 = random.nextInt(21) + 10;

                        if (var11 > var7.stackSize)
                        {
                            var11 = var7.stackSize;
                        }

                        var7.stackSize -= var11;
                        EntityItem var12 = new EntityItem(par1World, (par2 + var8), (par3 + var9), (par4 + var10), new ItemStack(var7.itemID, var11, var7.getItemDamage()));

                        if (var7.hasTagCompound())
                        {
                            var12.item.setTagCompound((NBTTagCompound)var7.getTagCompound().copy());
                        }

                        float var13 = 0.05F;
                        var12.motionX = ((float)random.nextGaussian() * var13);
                        var12.motionY = ((float)random.nextGaussian() * var13 + 0.2F);
                        var12.motionZ = ((float)random.nextGaussian() * var13);
                        par1World.spawnEntityInWorld(var12);
                    }
                }
            }
            
        }

        super.onBlockRemoval(par1World, par2, par3, par4);
    }
    
    @Override
    public void onBlockAdded(World par1World, int x, int y, int z)
    {
    	this.isBeingPowered(par1World, x, y, z);
    }
    
    @Override
    public void onNeighborBlockChange(World par1World, int x, int y, int z, int par5)
    {
    	this.isBeingPowered(par1World, x, y, z);
    }
    
    public void isBeingPowered(World par1World, int x, int y, int z)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);

        TileEntity tileEntity = par1World.getBlockTileEntity(x, y, z);
        
    	if(tileEntity instanceof UEIPowerRedstone)
    	{	
	        if (par1World.isBlockGettingPowered(x, y, z) || par1World.isBlockIndirectlyGettingPowered(x, y, z))
	        {
	        	//Send signal to tile entity
	    		((UEIPowerRedstone)tileEntity).onPowerOn();
	        }
	        else
	        {
	        	((UEIPowerRedstone)tileEntity).onPowerOff();
	        }
    	}
    }
    
    /**
     * Returns the TileEntity used by this block.
     */
    @Override
    public TileEntity getBlockEntity(int metadata)
    {
    	switch(metadata)
    	{
	    	case 0: return new TileEntityBatteryBox();
	    	case 1: return new TileEntityCoalGenerator();
	    	case 2: return new TileEntityElectricFurnace();
    	}
    	
    	return null;
    }
    
    @Override
    public int getRenderType()
    {
    	return UniversalComponents.MachineRenderType;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    	itemList.add(new ItemStack(this, 1, 0));
    	itemList.add(new ItemStack(this, 1, 1));
    	itemList.add(new ItemStack(this, 1, 2));
    }
}
