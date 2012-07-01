package net.minecraft.src.universalelectricity.extend;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.Vector3;
import net.minecraft.src.universalelectricity.electricity.IElectricUnit;

/**
 * REQUIRED
 * This tile entity is for all conductors.
 * @author Calclavia
 *
 */
public abstract class TileEntityConductor extends TileEntity
{
	/**
	 * Stores information on the blocks that this conductor is connected to
	 */
	public TileEntity[] connectedBlocks = {null, null, null, null, null, null};
	
	/**
	 * Adds a connection between this conductor and a UE unit
	 * @param tileEntity - Must be either a producer, consumer or a conductor
	 * @param side - side in which the connection is coming from
	 */
	public void addConnection(TileEntity tileEntity, byte side)
	{
		if(tileEntity instanceof TileEntityConductor || tileEntity instanceof IElectricUnit)
		{
			this.connectedBlocks[side] = tileEntity;
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
    	if(this.worldObj != null)
		{
			BlockConductor.updateConductorTileEntity(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
    	
        return false;
    }
	
	/**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     
	@Override
    public void updateEntity()
	{
		BlockConductor.updateConductorTileEntity(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		
		//Find the connected unit with the least amount of electricity and give more to them
		if(!this.worldObj.isRemote)
        {
			//Spread the electricity to neighboring blocks
			byte connectedUnits = 0;
			byte connectedConductors = 1;
			double averageElectricity = this.electricityStored;
			this.closestConsumer = null;
			
			Vector3 currentPosition = new Vector3(this.xCoord, this.yCoord, this.zCoord);
			
			for(byte i = 0; i < 6; i++)
	        {
				if(connectedBlocks[i] != null)
				{
					if(connectedBlocks[i] instanceof IElectricityConsumer || connectedBlocks[i] instanceof IElectricityProducer)
					{
						connectedUnits ++;
						
						if(connectedBlocks[i].getClass() == this.getClass())
						{
							averageElectricity += ((TileEntityConductor)connectedBlocks[i]).electricityStored;
							
							TileEntity tileEntity = ((TileEntityConductor)connectedBlocks[i]).closestConsumer;
							
							if(tileEntity != null)
							{
								this.closestConsumer = tileEntity;
							}
						
							connectedConductors ++;
						}	
						else if(connectedBlocks[i] instanceof IElectricityConsumer)
						{
							if(((IElectricityConsumer)connectedBlocks[i]).canReceiveElectricity(UniversalElectricity.getOrientationFromSide(i, (byte)2)))
							{
								this.closestConsumer = connectedBlocks[i];
							}
						}
							
					}
				}
	        }
			
			averageElectricity = averageElectricity/connectedConductors;
			
			
			//float averageWatt = 0;
	
			if(connectedUnits > 0)
			{
				for(byte i = 0; i < 6; i++)
		        {
					if(connectedBlocks[i] != null)
					{
						//Spread the electricity among the different blocks
						if(connectedBlocks[i] instanceof IElectricityConsumer && this.electricityStored > 0.0)
						{						
							if(((IElectricityConsumer)connectedBlocks[i]).canReceiveElectricity(UniversalElectricity.getOrientationFromSide(i, (byte)2)))
							{
								double transferElectricityAmount  = 0.0;
								IElectricityConsumer connectedConsumer = ((IElectricityConsumer)connectedBlocks[i]);
								
								if(connectedBlocks[i].getClass() == this.getClass() && this.electricityStored > ((TileEntityConductor)connectedConsumer).electricityStored)
								{
									transferElectricityAmount = Math.max(Math.min(averageElectricity - ((TileEntityConductor)connectedConsumer).electricityStored, this.electricityStored), 0.0);
								}
								else if(!(connectedConsumer instanceof TileEntityConductor))
								{
									transferElectricityAmount = this.electricityStored;
								}
								
								double rejectedElectricity = connectedConsumer.onReceiveElectricity(transferElectricityAmount, this.getVolts(), UniversalElectricity.getOrientationFromSide(i, (byte)2));
								this.electricityStored = Math.max(Math.min(this.electricityStored - transferElectricityAmount + rejectedElectricity, this.getElectricityCapacity()), 0.0);
							}
						}
						
						if(connectedBlocks[i] instanceof IElectricityProducer && this.electricityStored < this.getElectricityCapacity())
						{
							if(((IElectricityProducer)connectedBlocks[i]).canProduceElectricity(UniversalElectricity.getOrientationFromSide(i, (byte)2)))
							{
								double gainedElectricity = ((IElectricityProducer)connectedBlocks[i]).onProduceElectricity(this.getElectricityCapacity()-this.electricityStored, this.getVolts(), UniversalElectricity.getOrientationFromSide(i, (byte)2));
								this.onReceiveElectricity(gainedElectricity, ((IElectricityProducer)connectedBlocks[i]).getVolts(), i);
							}
						}
					}
		        }
			}
        }
	}*/
	
	/**
	 * Gets the resistance of the conductor. Used to calculate energy loss.
	 * A higher resistance means a higher energy loss.
	 * @return The amount of Ohm's. E.g 1.2Ohms or 3.0Ohms
	 */
	public abstract double getResistance();
}
