package net.minecraft.src.universalelectricity.extend;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.Vector3;
import net.minecraft.src.universalelectricity.electricity.IElectricityConsumer;
import net.minecraft.src.universalelectricity.electricity.IElectricityProducer;

public abstract class TileEntityConductor extends TileEntity implements IElectricityConsumer
{
	//The amount of electricity stored in the conductor
	protected double electricityStored = 0.0;

	//Stores information on all connected blocks around this tile entity
	public TileEntity[] connectedBlocks = {null, null, null, null, null, null};
	
	//The amount of ticks that the conductor is receiving the overCharge
	private int overChargeTicks;

	
	/**
	 * The tile entity of the closest electric consumer. Null if none. Use this to detect if electricity
	 * should transfer
	 */
	public TileEntity closestConsumer = null;
	
	/**
	 * This function adds a connection between this conductor and the UE unit
	 * @param tileEntity - Must be either a producer, consumer or a conductor
	 * @param side - side in which the connection is coming from
	 */
	public void addConnection(TileEntity tileEntity, byte side)
	{
		if(tileEntity instanceof IElectricityProducer || tileEntity instanceof IElectricityConsumer)
		{
			this.connectedBlocks[side] = tileEntity;
		}
		else
		{
			this.connectedBlocks[side] = null;
		}
	}
	
	/**
	 * onRecieveElectricity is called whenever a Universal Electric conductor sends a packet of electricity to the consumer (which is this block).
	 * @param watts - The amount of watt this block recieved
	 * @param side - The side of the block in which the electricity came from
	 * @return watt - The amount of rejected power to be sent back into the conductor
	 */
	 @Override
    public double onReceiveElectricity(double watts, int voltage, byte side)
    {
	 	if(voltage > this.getVolts())
		{
	 		overChargeTicks++;
			this.overCharge(voltage, overChargeTicks);
		}else{
			overChargeTicks = 0;
		}
	 
    	if(canReceiveElectricity(side) && voltage != 0)
		{
    		double rejectedElectricity = Math.max((this.electricityStored + watts) - this.getElectricityCapacity(), 0);
    		double electricityLoss = (Math.pow(UniversalElectricity.getAmps(watts - rejectedElectricity, voltage), 2))* this.getResistance();
			this.electricityStored = Math.max(this.electricityStored + (watts - rejectedElectricity) - (electricityLoss), 0);
			return rejectedElectricity;
		}
    	
    	return watts;
    }

	/**
	 * You can use this to check if a wire can connect to this UE consumer to properly render the graphics
	 * @return Returns true or false if this consumer can receive electricity at this given tick or moment.
	 */
	@Override
	public boolean canReceiveElectricity(byte side)
	{
		return true;
	}
	
	/**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
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
	}
	
	/**
	 * Called when the conductor's voltage becomes higher than it should be.
	 * @param volts - The amount of volts being forced into the conductor
	 * @param ticks - The amount of ticks that the conductor is receiving the overCharge
	 */
	protected void overCharge(int volts, int ticks)
	{
		
	}

	/**
	 * @return Return the stored electricity in this consumer. Called by conductors to spread electricity to this unit.
	 */
    @Override
	public double getStoredElectricity()
    {
    	return this.electricityStored;
    }
	
	/**
     * Reads a tile entity from NBT.
     */
    @Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.electricityStored = par1NBTTagCompound.getDouble("electricityStored");
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setDouble("electricityStored", this.electricityStored);
    }
    
    //Conductors can not be disabled
    @Override
	public void onDisable(int duration) { }


	@Override
	public boolean isDisabled() { return false; }
	
	/**
	 * Gets the resistance of the conductor. Used to calculate energy loss.
	 * A higher resistance means a higher energy loss.
	 * @return The amount of Ohm's. E.g 1.2Ω or 3.0Ω
	 */
	public abstract double getResistance();
}
