package net.minecraft.src.universalelectricity.extend;

import net.minecraft.src.TileEntity;
import net.minecraft.src.universalelectricity.electricity.ElectricityManager;
import net.minecraft.src.universalelectricity.electricity.IElectricUnit;

/**
 * REQUIRED
 * This tile entity is for all conductors.
 * @author Calclavia
 *
 */
public abstract class TileEntityConductor extends TileEntity
{
	public int connectionID = 0;
		
	/**
	 * Stores information on the blocks that this conductor is connected to
	 */
	public TileEntity[] connectedBlocks = {null, null, null, null, null, null};
	
	public TileEntityConductor()
	{
		this.reset();
	}
	
	/**
	 * Adds a connection between this conductor and a UE unit
	 * @param tileEntity - Must be either a producer, consumer or a conductor
	 * @param side - side in which the connection is coming from
	 */
	public void updateConnection(TileEntity tileEntity, byte side)
	{
		if(tileEntity instanceof TileEntityConductor || tileEntity instanceof IElectricUnit)
		{
			this.connectedBlocks[side] = tileEntity;
			
			if(tileEntity instanceof TileEntityConductor)
			{
				ElectricityManager.mergeConnection(this.connectionID, ((TileEntityConductor)tileEntity).connectionID);
			}
		}
		else
		{
			if(this.connectedBlocks[side] != null)
			{
				if(this.connectedBlocks[side] instanceof TileEntityConductor)
				{
					ElectricityManager.splitConnection(this, (TileEntityConductor)this.connectedBlocks[side]);
				}
			}
			 
			this.connectedBlocks[side] = null;
		}
	}
	
	public void updateConnectionWithoutSplit(TileEntity tileEntity, byte side)
	{
		if(tileEntity instanceof TileEntityConductor || tileEntity instanceof IElectricUnit)
		{
			this.connectedBlocks[side] = tileEntity;
			
			if(tileEntity instanceof TileEntityConductor)
			{
				ElectricityManager.mergeConnection(this.connectionID, ((TileEntityConductor)tileEntity).connectionID);
			}
		}
		else
		{
			this.connectedBlocks[side] = null;
		}
	}
	
	/**
     * Determines if this TileEntity requires update calls.
     * @return True if you want updateEntity() to be called, false if not
     */
    public boolean canUpdate()
    {
    	this.refreshConnectedBlocks();
        return false;
    }
    
    public void reset()
    {
    	this.connectionID = 0;
		ElectricityManager.registerConductor(this);
    }
    
    public void refreshConnectedBlocks()
    {
    	if(this.worldObj != null)
		{
			BlockConductor.updateConductorTileEntity(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
    }
	
	/**
	 * Gets the resistance of the conductor. Used to calculate energy loss.
	 * A higher resistance means a higher energy loss.
	 * @return The amount of Ohm's. E.g 1.2Ohms or 3.0Ohms
	 */
	public abstract double getResistance();
}
