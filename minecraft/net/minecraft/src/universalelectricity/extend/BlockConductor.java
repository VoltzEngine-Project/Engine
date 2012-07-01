package net.minecraft.src.universalelectricity.extend;

/**
 * REQUIRED
 * This class is to be extended by all blocks that conduct electricity. Wires, cables and all electrical block that passes electricity will
 * extend from this block. The block contains a tile entity which stores the electric data and conducts it by spreading it to neighbour conductors.
 * @author Calclavia
 *
 */

import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.electricity.IElectricUnit;

public abstract class BlockConductor extends BlockContainer
{
	public BlockConductor(int id, Material material)
	{
		super(id, material);
	}
	
	/**
	 * Checks if the block is being connected to a conductor
	 * @param world - The world in which this conductor block is in
	 * @param x - The X axis of the conductor
	 * @param y - The Y axis of the conductor
	 * @param z - The Z axis of the conductor
	 * @return Returns the tile entity for the block on the designated side. Returns null if not a UE Unit
	 */
	public static TileEntity getUEUnit(World world, int x, int y, int z, byte side)
	{
		switch(side)
		{
			case 0: y -= 1; break;
			case 1: y += 1; break;
			case 2: z += 1; break;
			case 3: z -= 1; break;
			case 4: x += 1; break;
			case 5: x -= 1; break;
		}
		
		//Check if the designated block is a UE Unit - producer, consumer or a conductor
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if(tileEntity instanceof TileEntityConductor)
		{
			return tileEntity;
		}
		
		if(tileEntity instanceof IElectricUnit)
		{
			if(((IElectricUnit)tileEntity).canReceiveElectricity(UniversalElectricity.getOrientationFromSide(side, (byte)2)))
			{
				return tileEntity;
			}
		}
		
		return null;
	}
	
	/**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
	@Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        
        this.updateConductorTileEntity(world, x, y, z);
        
        world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {        
    	this.updateConductorTileEntity(world, x, y, z);
    }
	
	public static void updateConductorTileEntity(World world, int x, int y, int z)
	{
		for(byte i = 0; i < 6; i++)
        {
            //Update the tile entity on neighboring blocks
        	TileEntityConductor conductorTileEntity = (TileEntityConductor)world.getBlockTileEntity(x, y, z);
        	
        	if(conductorTileEntity != null)
        	{
            	conductorTileEntity.addConnection(getUEUnit(world, x, y, z, i), i);
        	}
        }
	}
	
	/**
	 * In your Block Conductor class you must specify the UETileEntityConductor class.
	 */
	@Override
	public TileEntity getBlockEntity()
    {
    	return null;
    }
}
