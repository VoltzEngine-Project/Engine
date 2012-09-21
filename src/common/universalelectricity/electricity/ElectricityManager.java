package universalelectricity.electricity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.implement.IElectricityReceiver;
import universalelectricity.prefab.TileEntityConductor;
import universalelectricity.prefab.Vector3;
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
	public static ElectricityManager instance;
	
    private List<IElectricityReceiver> electricUnits = new ArrayList<IElectricityReceiver>();
    private List<TileEntityConductor> electricConductors = new ArrayList<TileEntityConductor>();

    private List<ElectricityTransferData> electricityTransferQueue = new ArrayList<ElectricityTransferData>();
    private List<ElectricityNetwork> wireConnections = new ArrayList<ElectricityNetwork>();
    private int maxConnectionID = 0;

    public int refreshConductors;
    
    public ElectricityManager()
    {
    	this.timedConductorRefresh();
    }
    
    public void timedConductorRefresh()
    {
    	this.refreshConductors = 20*10;
    }

    /**
     * Registers an electricity consumer for it to receive electricity.
     * Call it in your consumer's tile entity constructor like this:
     * ElectricityManager.registerConsumer(this);
     * @param newUnit - The consumer to be registered.
     */
    public void registerElectricUnit(IElectricityReceiver newUnit)
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
        this.wireConnections.add(new ElectricityNetwork(getMaxConnectionID(), newConductor));

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
            ElectricityNetwork connection1 = getConnectionByID(ID1);
            ElectricityNetwork connection2 = getConnectionByID(ID2);
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
        ElectricityNetwork connection = getConnectionByID(conductorA.connectionID);
        connection.cleanUpArray();

        for(TileEntityConductor conductor : connection.conductors)
        {
            conductor.reset();
        }

        for(TileEntityConductor conductor : connection.conductors)
        {
            for (byte i = 0; i < 6; i++)
            {
                conductor.updateConnectionWithoutSplit(Vector3.getConnectorFromSide(conductor.getWorld(), new Vector3(conductor.xCoord, conductor.yCoord, conductor.zCoord), ForgeDirection.getOrientation(i)), ForgeDirection.getOrientation(i));
            }
        }
    }

    /**
     * Gets a electricity wire connection line by it's connection ID
     * @param ID
     * @return
     */
    public ElectricityNetwork getConnectionByID(int ID)
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
    public void produceElectricity(TileEntity sender, TileEntityConductor targetConductor, double amps, double voltage)
    {
        if(targetConductor != null && amps > 0 && voltage > 0)
        {
            //Find a path between this conductor and all connected units and try to send the electricity to them directly
            ElectricityNetwork connection = this.getConnectionByID(targetConductor.connectionID);

            if(connection != null)
            {
                List<IElectricityReceiver> allElectricUnitsInLine = connection.getConnectedElectricUnits();
                double leftOverAmps = amps;

                for (TileEntityConductor conductor : connection.conductors)
                {
                    for (byte i = 0; i < conductor.connectedBlocks.length; i++)
                    {
                        TileEntity tileEntity = conductor.connectedBlocks[i];

                        if (tileEntity != null)
                        {
                            if (tileEntity instanceof IElectricityReceiver)
                            {
                                IElectricityReceiver electricUnit = (IElectricityReceiver)tileEntity;

                                if (electricUnit.wattRequest() > 0 && electricUnit.canReceiveFromSide(ForgeDirection.getOrientation(i).getOpposite()))
                                {
                                    double transferAmps = Math.max(0, Math.min(leftOverAmps, Math.min(amps / allElectricUnitsInLine.size(), ElectricInfo.getAmps(electricUnit.wattRequest(), electricUnit.getVoltage()) )));
                                    leftOverAmps -= transferAmps;
                                    this.electricityTransferQueue.add(new ElectricityTransferData(sender, electricUnit, ForgeDirection.getOrientation(i).getOpposite(), transferAmps, voltage));
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
     * @return - The amount of watts this connection line needs
     */
    public double getElectricityRequired(int ID)
    {
        ElectricityNetwork connection = this.getConnectionByID(ID);
        double need = 0;

        if (connection != null)
        {
            for (TileEntityConductor conductor : connection.conductors)
            {
                for (byte i = 0; i < conductor.connectedBlocks.length; i++)
                {
                    TileEntity tileEntity = conductor.connectedBlocks[i];

                    if (tileEntity != null)
                    {
                        if (tileEntity instanceof IElectricityReceiver)
                        {
                            IElectricityReceiver electricUnit = (IElectricityReceiver)tileEntity;

                            if (electricUnit.canReceiveFromSide(ForgeDirection.getOrientation(i).getOpposite()))
                            {
                                need += electricUnit.wattRequest();
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
	        	IElectricityReceiver electricUnit = electricUnits.get(i);
	        	
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
	            
                for (int ii = 0; ii < electricityTransferQueue.size(); ii ++)
                {
                    if (electricityTransferQueue.get(ii).receiver == electricUnit)
                    {
                    	electricUnit.onReceive(electricityTransferQueue.get(ii).sender, electricityTransferQueue.get(ii).amps, electricityTransferQueue.get(ii).voltage, electricityTransferQueue.get(ii).side);
    	                electricityTransferQueue.remove(ii);
                    }              
                }
	        }
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