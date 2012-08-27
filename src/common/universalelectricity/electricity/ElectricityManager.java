package universalelectricity.electricity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Load;
import universalelectricity.Vector3;
import universalelectricity.extend.IElectricUnit;
import universalelectricity.extend.TileEntityConductor;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * This class is used to manage electricity transferring and flow
 * @author Calclavia
 *
 */
public class ElectricityManager implements ITickHandler
{
	public World worldObj;
	
    private static List<IElectricUnit> electricUnits = new ArrayList<IElectricUnit>();
    private static List<TileEntityConductor> electricConductors = new ArrayList<TileEntityConductor>();

    private static List<ElectricityTransferData> electricityTransferQueue = new ArrayList<ElectricityTransferData>();
    private static List<ElectricityConnection> wireConnections = new ArrayList<ElectricityConnection>();
    private static int maxConnectionID = 0;

    public long inGameTicks = 0;
    
    public boolean refreshConductors = false;

    /**
     * Registers an electricity consumer for it to receive electricity.
     * Call it in your consumer's tile entity constructor like this:
     * ElectricityManager.registerConsumer(this);
     * @param newUnit - The consumer to be registered.
     */
    public static void registerElectricUnit(IElectricUnit newUnit)
    {
        if (!electricUnits.contains(newUnit))
        {
            electricUnits.add(newUnit);
        }
    }

    /**
     * Registers a UE conductor
     * @param conductor - The conductor tile entity
     * @return - The ID of the connection line that is assigned to this conductor
     */
    public static void registerConductor(TileEntityConductor newConductor)
    {
        cleanUpConnections();
        wireConnections.add(new ElectricityConnection(getMaxConnectionID(), newConductor));

        if (!electricConductors.contains(newConductor))
        {
            electricConductors.add(newConductor);
        }
    }

    /**
     * Merges two connection lines together into one.
     * @param ID1 - ID of connection line
     * @param ID2 - ID of connection line
     */
    public static void mergeConnection(int ID1, int ID2)
    {
        if (ID1 != ID2)
        {
            ElectricityConnection connection1 = getConnectionByID(ID1);
            ElectricityConnection connection2 = getConnectionByID(ID2);
            connection1.conductors.addAll(connection2.conductors);
            connection1.setID(ID1);
            wireConnections.remove(connection2);
        }
    }

    /**
     * Separate one connection line into two different ones between two conductors.
     * This function does this by resetting all wires in the connection line and
     * making them each reconnect.
     * @param conductorA - existing conductor
     * @param conductorB - broken/invalid conductor
     */
    public static void splitConnection(TileEntityConductor conductorA, TileEntityConductor conductorB)
    {
        ElectricityConnection connection = getConnectionByID(conductorA.connectionID);
        connection.cleanUpArray();

        for (TileEntityConductor conductor : connection.conductors)
        {
            conductor.reset();
        }

        for (TileEntityConductor conductor : connection.conductors)
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
    public static ElectricityConnection getConnectionByID(int ID)
    {
        cleanUpConnections();

        for (int i = 0; i < wireConnections.size(); i++)
        {
            if (wireConnections.get(i).getID() == ID)
            {
                return wireConnections.get(i);
            }
        }

        return null;
    }

    /**
     * Clean up and remove useless connections
     */
    public static void cleanUpConnections()
    {
        for (int i = 0; i < wireConnections.size(); i++)
        {
            wireConnections.get(i).cleanUpArray();

            if (wireConnections.get(i).conductors.size() == 0)
            {
                wireConnections.remove(i);
            }
        }
    }

    /**
     * Get the highest connection ID. Use this to assign new wire connection lines
     * @return
     */
    public static int getMaxConnectionID()
    {
        maxConnectionID ++;
        return maxConnectionID;
    }

    /**
     * Produces electricity into a specific conductor and distribute it evenly into different machines
     * @param targetConductor - The tile entity in which the electricity is being produced into
     * @param side - The side in which the electricity is coming in from. 0-5 byte.
     */
    public static void produceElectricity(TileEntityConductor targetConductor, float watts, float voltage)
    {
        if(targetConductor != null && watts > 0 && voltage > 0)
        {
            //Find a path between this conductor and all connected units and try to send the electricity to them directly
            ElectricityConnection connection = getConnectionByID(targetConductor.connectionID);

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

                                if (electricUnit.electricityRequest() > 0 && electricUnit.canReceiveFromSide(ForgeDirection.getOrientation(i).getOpposite()))
                                {
                                    float transferWatts = Math.max(0, Math.min(leftOverWatts, Math.min(watts / allElectricUnitsInLine.size(), electricUnit.electricityRequest())));
                                    leftOverWatts -= transferWatts;
                                    electricityTransferQueue.add(new ElectricityTransferData(electricUnit, ForgeDirection.getOrientation(i).getOpposite(), transferWatts, voltage));
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
    public static float electricityRequired(int ID)
    {
        ElectricityConnection connection = getConnectionByID(ID);
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
                                need += electricUnit.electricityRequest();
                            }
                        }
                    }
                }
            }
        }

        return need;
    }

	@Override
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

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if(this.refreshConductors)
		{
			for(int j = 0; j < electricConductors.size(); j ++)
	        {
	        	TileEntityConductor conductor  = electricConductors.get(j);
	            conductor.refreshConnectedBlocks();
	        }
			
			this.refreshConductors = false;
		}
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.SERVER, TickType.CLIENT);
	}

	@Override
	public String getLabel()
	{
		return "Electricity Manager";
	}
	
	@ForgeSubscribe
	public void loadWorld(Load event)
	{
		this.refreshConductors = true;
	}
}