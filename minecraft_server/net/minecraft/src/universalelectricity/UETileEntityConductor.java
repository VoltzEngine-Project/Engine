package net.minecraft.src.universalelectricity;

import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public abstract class UETileEntityConductor extends TileEntity implements UEIConsumer
{
	//The amount of electricity stored in the conductor
	protected int electricityStored = 0;
	
	//The maximum amount of electricity this conductor can take
	protected int capacity = 0;

	//Stores information on all connected blocks around this tile entity
	public TileEntity[] connectedBlocks = {null, null, null, null, null, null};

	//Checks if this is the first the tile entity updates
	protected boolean firstUpdate = true;
	
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
		if(tileEntity instanceof UEIProducer || tileEntity instanceof UEIConsumer)
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
    public int onReceiveElectricity(int watts, int voltage, byte side)
    {
	 	if(voltage > this.getVolts())
		{
			this.overCharge(voltage);
		}
	 
    	if(canReceiveElectricity(side))
		{
	    	int rejectedElectricity = Math.max((this.electricityStored + watts) - this.getElectricityCapacity(), 0);
			this.electricityStored = Math.max(this.electricityStored+watts - rejectedElectricity, 0);
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
		if(this.firstUpdate)
		{
			//Update some variables
			UEBlockConductor conductorBlock = (UEBlockConductor)this.getBlockType();			
			this.capacity = (conductorBlock).conductorCapacity();
			((UEBlockConductor)this.getBlockType()).updateConductorTileEntity(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			this.firstUpdate = false;
		}
		

		//Spread the electricity to neighboring blocks
		byte connectedUnits = 0;
		byte connectedConductors = 1;
		int averageElectricity = this.electricityStored;
		this.closestConsumer = null;
		
		Vector3 currentPosition = new Vector3(this.xCoord, this.yCoord, this.zCoord);

		//Find the connected unit with the least amount of electricity and give more to them
		for(byte i = 0; i < 6; i++)
        {
			if(connectedBlocks[i] != null)
			{
				if(connectedBlocks[i] instanceof UEIConsumer || connectedBlocks[i] instanceof UEIProducer)
				{
					connectedUnits ++;
					
					if(connectedBlocks[i].getClass() == this.getClass())
					{
						averageElectricity += ((UETileEntityConductor)connectedBlocks[i]).electricityStored;
						
						TileEntity tileEntity = ((UETileEntityConductor)connectedBlocks[i]).closestConsumer;
						
						if(tileEntity != null)
						{
							this.closestConsumer = tileEntity;
						}
					
						connectedConductors ++;
					}	
					else if(connectedBlocks[i] instanceof UEIConsumer)
					{
						if(((UEIConsumer)connectedBlocks[i]).canReceiveElectricity(UniversalElectricity.getOrientationFromSide(i, (byte)2)))
						{
							this.closestConsumer = connectedBlocks[i];
						}
					}
						
				}
			}
        }
		
		averageElectricity = averageElectricity/connectedConductors;
		
		
		float averageWatt = 0;

		if(connectedUnits > 0)
		{
			for(byte i = 0; i < 6; i++)
	        {
				if(connectedBlocks[i] != null)
				{
					//Spread the electricity among the different blocks
					if(connectedBlocks[i] instanceof UEIConsumer && this.electricityStored > 0)
					{						
						if(((UEIConsumer)connectedBlocks[i]).canReceiveElectricity(UniversalElectricity.getOrientationFromSide(i, (byte)2)))
						{
							int transferElectricityAmount  = 0;
							UEIConsumer connectedConsumer = ((UEIConsumer)connectedBlocks[i]);
							
							if(connectedBlocks[i].getClass() == this.getClass() && this.electricityStored > ((UETileEntityConductor)connectedConsumer).electricityStored)
							{
								transferElectricityAmount = Math.max(Math.min(averageElectricity - ((UETileEntityConductor)connectedConsumer).electricityStored, this.electricityStored), 0);
							}
							else if(!(connectedConsumer instanceof UETileEntityConductor))
							{
								transferElectricityAmount = this.electricityStored;
							}
							
							int rejectedElectricity = connectedConsumer.onReceiveElectricity(transferElectricityAmount, this.getVolts(), UniversalElectricity.getOrientationFromSide(i, (byte)2));
							this.electricityStored = Math.max(Math.min(this.electricityStored - transferElectricityAmount + rejectedElectricity, this.capacity), 0);
						}
					}
					
					if(connectedBlocks[i] instanceof UEIProducer && this.electricityStored < this.getElectricityCapacity())
					{
						if(((UEIProducer)connectedBlocks[i]).canProduceElectricity(UniversalElectricity.getOrientationFromSide(i, (byte)2)))
						{
							int gainedElectricity = ((UEIProducer)connectedBlocks[i]).onProduceElectricity(this.capacity-this.electricityStored, this.getVolts(), UniversalElectricity.getOrientationFromSide(i, (byte)2));
							this.onReceiveElectricity(gainedElectricity, ((UEIProducer)connectedBlocks[i]).getVolts(), i);
						}
					}
				}
	        }
		}
    	
	}
	
	/**
	 * Called when the conductor's voltage becomes higher than it should be.
	 * @param volts - The amount of volts being forced into the conductor
	 */
	protected void overCharge(int volts)
	{
		
	}

	/**
	 * @return Return the stored electricity in this consumer. Called by conductors to spread electricity to this unit.
	 */
    @Override
	public int getStoredElectricity()
    {
    	return this.electricityStored;
    }
    
    @Override
    public int getElectricityCapacity()
	{
		return this.capacity;
	}
	
	/**
     * Reads a tile entity from NBT.
     */
    @Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.electricityStored = par1NBTTagCompound.getInteger("electricityStored");
        this.capacity = par1NBTTagCompound.getInteger("capacity");
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("electricityStored", this.electricityStored);
    	par1NBTTagCompound.setInteger("capacity", this.capacity);
    }
    
    //Conductors can not be disabled
    @Override
	public void onDisable(int duration) { }


	@Override
	public boolean isDisabled() { return false; }
	
	/**
     * Gets the block type at the location of this entity (client-only).
     */
    public Block getBlockType()
    {
        if (this.blockType == null)
        {
            this.blockType = Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
        }

        return this.blockType;
    }
	
}
