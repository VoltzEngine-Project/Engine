package universalelectricity.electricity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.Vector3;
import universalelectricity.extend.IElectricUnit;
import universalelectricity.extend.TileEntityConductor;
import cpw.mods.fml.common.TickType;

/**
 * This class is used to manage electricity transferring and flow. It is also used to call updates on UE tile entities.
 * 
 * Electricity Manager is made for each world so it doesn't conflict with electricity devices in different dimensions.
 * @author Calclavia
 *
 */
public class ElectricityManager
{
	/*
    public static final List<ElectricityManager> instances = new ArrayList<ElectricityManager>();
    
    /**
     * Gets an electricity manager instance.
     * @param worldObj
     * @return The Electricity Manager for this world. Will never return null.
     *
    public static ElectricityManager get(World worldObj)
    {
    	if(worldObj == null)
    	{
    		System.out.println("World cannot be null when getting an electricity manager!");
    		return null;
    	}
    	
    	for(ElectricityManager manager : instances)
    	{
    		if(manager.worldObj == worldObj)
    		{
    			return manager;
    		}
    	}
    	
    	ElectricityManager newManager = new ElectricityManager(worldObj);
    	instances.add(newManager);
		return newManager;
    }
    */
	
	public static ElectricityManager instance;

	public World worldObj;
	
    private List<IElectricUnit> electricUnits = new ArrayList<IElectricUnit>();
    private List<TileEntityConductor> electricConductors = new ArrayList<TileEntityConductor>();

    private List<ElectricityTransferData> electricityTransferQueue = new ArrayList<ElectricityTransferData>();
    private List<ElectricityConnection> wireConnections = new ArrayList<ElectricityConnection>();
    private int maxConnectionID = 0;

    public long inGameTicks = 0;
    
    public int refreshConductors;
    
    public ElectricityManager(World worldObj)
    {
    	this.reset(worldObj);
    }
    
    public void reset(World worldObj)
    {
    	this.worldObj = worldObj;
    	this.refreshConductors = 20*10;
    }

    /**
     * Registers an electricity consumer for it to receive electricity.
     * Call it in your consumer's tile entity constructor like this:
     * ElectricityManager.registerConsumer(this);
     * @param newUnit - The consumer to be registered.
     */
    public void registerElectricUnit(IElectricUnit newUnit)
    {
        if (!this.electricUnits.contains(newUnit))
        {
        	this.electricUnits.add(newUnit);
        }
    }

    /**
     * Registers a UE conductor
     * @param conductor - The conductor tile entity
     * @return - The ID of the connection line that is assigned to this conductor
     */
    public void registerConductor(TileEntityConductor newConductor)
    {
        cleanUpConnections();
        this.wireConnections.add(new ElectricityConnection(getMaxConnectionID(), newConductor));

        if (!this.electricConductors.contains(newConductor))
        {
        	this.electricConductors.add(newConductor);
        }
    }

    /**
     * Merges two connection lines together into one.
     * @param ID1 - ID of connection line
     * @param ID2 - ID of connection line
     */
    public void mergeConnection(int ID1, int ID2)
    {
        if(ID1 != ID2)
        {
            ElectricityConnection connection1 = getConnectionByID(ID1);
            ElectricityConnection connection2 = getConnectionByID(ID2);
            connection1.conductors.addAll(connection2.conductors);
            connection1.setID(ID1);
            this.wireConnections.remove(connection2);
        }
    }

    /**
     * Separate one connection line into two different ones between two conductors.
     * This function does this by resetting all wires in the connection line and
     * making them each reconnect.
     * @param conductorA - existing conductor
     * @param conductorB - broken/invalid conductor
     */
    public void splitConnection(TileEntityConductor conductorA, TileEntityConductor conductorB)
    {
        ElectricityConnection connection = getConnectionByID(conductorA.connectionID);
        connection.cleanUpArray();

        for(TileEntityConductor conductor : connection.conductors)
        {
            conductor.reset();
        }

        for(TileEntityConductor conductor : connection.conductors)
        {
            for (byte i = 0; i < 6; i++)
            {
                conductor.updateConnectionWithoutSplit(Vector3.getUEUnitFromSide(conductor.getWorld(), new Vector3(conductor.xCoord, conductor.yCoord, conductor.zCoord), ForgeDirection.getOrientation(i)), ForgeDirection.getOrientation(i));
            }
        }
    }

    /**
     * Gets a electricity wire connection line by it's connection ID
     * @param ID
     * @return
     */
    public ElectricityConnection getConnectionByID(int ID)
    {
        cleanUpConnections();

        for (int i = 0; i < this.wireConnections.size(); i++)
        {
            if (this.wireConnections.get(i).getID() == ID)
            {
                return this.wireConnections.get(i);
            }
        }

        return null;
    }

    /**
     * Clean up and remove useless connections
     */
    public void cleanUpConnections()
    {
        for (int i = 0; i < this.wireConnections.size(); i++)
        {
        	this.wireConnections.get(i).cleanUpArray();

            if (this.wireConnections.get(i).conductors.size() == 0)
            {
            	this.wireConnections.remove(i);
            }
        }
    }

    /**
     * Get the highest connection ID. Use this to assign new wire connection lines
     * @return
     */
    public int getMaxConnectionID()
    {
    	this.maxConnectionID ++;
        return this.maxConnectionID;
    }

    /**
     * Produces electricity into a specific conductor and distribute it evenly into different machines
     * @param targetConductor - The tile entity in which the electricity is being produced into
     * @param side - The side in which the electricity is coming in from. 0-5 byte.
     */
    public void produceElectricity(TileEntityConductor targetConductor, float watts, float voltage)
    {
        if(targetConductor != null && watts > 0 && voltage > 0)
        {
            //Find a path between this conductor and all connected units and try to send the electricity to them directly
            ElectricityConnection connection = this.getConnectionByID(targetConductor.connectionID);

            if(connection != null)
            {
                List<IElectricUnit> allElectricUnitsInLine = connection.getConnectedElectricUnits();
                float leftOverWatts = watts;

                for (TileEntityConductor conductor : connection.conductors)
                {
                    for (byte i = 0; i < conductor.connectedBlocks.length; i++)
                    {
                        TileEntity tileEntity = conductor.connectedBlocks[i];

                        if (tileEntity != null)
                        {
                            if (tileEntity instanceof IElectricUnit)
                            {
                                IElectricUnit electricUnit = (IElectricUnit)tileEntity;

                                if (electricUnit.ampRequest() > 0 && electricUnit.canReceiveFromSide(ForgeDirection.getOrientation(i).getOpposite()))
                                {
                                    float transferWatts = Math.max(0, Math.min(leftOverWatts, Math.min(watts / allElectricUnitsInLine.size(), electricUnit.ampRequest())));
                                    leftOverWatts -= transferWatts;
                                    this.electricityTransferQueue.add(new ElectricityTransferData(electricUnit, ForgeDirection.getOrientation(i).getOpposite(), transferWatts, voltage));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if the current connection line needs electricity
     * @return - The amount of electricity this connection line needs
     */
    public float electricityRequired(int ID)
    {
        ElectricityConnection connection = this.getConnectionByID(ID);
        float need = 0;

        if (connection != null)
        {
            for (TileEntityConductor conductor : connection.conductors)
            {
                for (byte i = 0; i < conductor.connectedBlocks.length; i++)
                {
                    TileEntity tileEntity = conductor.connectedBlocks[i];

                    if (tileEntity != null)
                    {
                        if (tileEntity instanceof IElectricUnit)
                        {
                            IElectricUnit electricUnit = (IElectricUnit)tileEntity;

                            if (electricUnit.canReceiveFromSide(ForgeDirection.getOrientation(i).getOpposite()))
                            {
                                need += electricUnit.ampRequest();
                            }
                        }
                    }
                }
            }
        }

        return need;
    }
    
    /**
     * This function is called to refresh all conductors in UE
     */
    public void refreshConductors()
    {
		for(int j = 0; j < this.electricConductors.size(); j ++)
        {
        	TileEntityConductor conductor  = this.electricConductors.get(j);
            conductor.refreshConnectedBlocks();
        }
    }

	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		try
		{
	        for(int i = 0; i < electricUnits.size(); i++)
	        {
	        	IElectricUnit electricUnit = electricUnits.get(i);
	        	
	            //Cleanup useless units
	            if (electricUnit == null)
	            {
	                electricUnits.remove(electricUnit);
	                break;
	            }
	            else if (((TileEntity)electricUnit).isInvalid())
	            {
	                electricUnits.remove(electricUnit);
	                break;
	            }
	
	            if(electricUnit.getTickInterval() > 0)
	            {
		            if(inGameTicks % electricUnit.getTickInterval() == 0)
		            {
		                float watts = 0;
		                float voltage = 0;
		                ForgeDirection side = ForgeDirection.UNKNOWN;
		
		                //Try to stack all electricity from one side into one update
		                for (int ii = 0; ii < electricityTransferQueue.size(); ii ++)
		                {
		                    if (electricityTransferQueue.get(ii).electricUnit == electricUnit)
		                    {
		                        //If the side is not set for this tick
		                        if (side == ForgeDirection.UNKNOWN)
		                        {
		                            watts = electricityTransferQueue.get(ii).watts;
		                            voltage = electricityTransferQueue.get(ii).voltage;
		                            side = electricityTransferQueue.get(ii).side;
		                            electricityTransferQueue.remove(ii);
		                        }
		                        else if (electricityTransferQueue.get(ii).side == side)
		                        {
		                            watts += electricityTransferQueue.get(ii).watts;
		                            voltage = Math.min(voltage, electricityTransferQueue.get(ii).voltage);
		                            electricityTransferQueue.remove(ii);
		                        }
		                    }
		                }
		
		                electricUnit.onUpdate(watts, voltage, side);
		            }
	            }
	        }
	
	        inGameTicks ++;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if(this.refreshConductors > 0)
		{
			this.refreshConductors();
			this.refreshConductors --;
		}
	}
}