package universalelectricity.electricity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.implement.IConductor;
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
	public static ElectricityManager instance = new ElectricityManager();
	
    private List<IElectricityReceiver> electricUnits = new ArrayList<IElectricityReceiver>();
    private List<IConductor> electricConductors = new ArrayList<IConductor>();

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
    public void splitConnection(IConductor conductorA, IConductor conductorB)
    {
        ElectricityNetwork connection = getConnectionByID(conductorA.getConnectionID());
        connection.cleanUpArray();

        for(IConductor conductor : connection.conductors)
        {
            conductor.reset();
        }

        for(IConductor conductor : connection.conductors)
        {
            for (byte i = 0; i < 6; i++)
            {
                conductor.updateConnectionWithoutSplit(Vector3.getConnectorFromSide(conductor.getWorld(), new Vector3(((TileEntity)conductor).xCoord, ((TileEntity)conductor).yCoord, ((TileEntity)conductor).zCoord), ForgeDirection.getOrientation(i)), ForgeDirection.getOrientation(i));
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
     * Produces electricity into a specific wire which will be distributed across the electricity network.
     * @param sender - The machine sending the electricity.
     * @param targetConductor - The conductor receiving the electricity (or connected to the machine).
     * @param amps - The amount of amps this machine is sending.
     * @param voltage 0 The amount of volts this machine is sending.
     */
    public void produceElectricity(TileEntity sender, IConductor targetConductor, double amps, double voltage)
    {
        if(targetConductor != null && amps > 0 && voltage > 0)
        {
            //Find a path between this conductor and all connected units and try to send the electricity to them directly
            ElectricityNetwork electricityNetwork = this.getConnectionByID(targetConductor.getConnectionID());

            if(electricityNetwork != null)
            {
                List<IElectricityReceiver> allElectricUnitsInLine = electricityNetwork.getConnectedReceivers();
                double leftOverAmps = amps;

                for (IConductor conductor : electricityNetwork.conductors)
                {
                    for (byte i = 0; i < conductor.getConnectedBlocks().length; i++)
                    {
                        TileEntity tileEntity = conductor.getConnectedBlocks()[i];

                        if (tileEntity != null)
                        {
                            if (tileEntity instanceof IElectricityReceiver)
                            {
                                IElectricityReceiver receiver = (IElectricityReceiver)tileEntity;

                                if (Math.ceil(receiver.wattRequest()) > 0 && receiver.canReceiveFromSide(ForgeDirection.getOrientation(i).getOpposite()))
                                {
                                    double transferAmps = Math.max(0, Math.min(leftOverAmps, Math.min(amps / allElectricUnitsInLine.size(), ElectricInfo.getAmps(Math.ceil(receiver.wattRequest()), receiver.getVoltage()) )));
                                    leftOverAmps -= transferAmps;
                                    
                                    //Calculate electricity loss
                                    double distance = Vector3.distance(Vector3.get(sender), Vector3.get((TileEntity)receiver));
                                    double ampsReceived = transferAmps - (transferAmps * transferAmps * targetConductor.getResistance() * distance)/voltage;
                                    double voltsReceived = voltage - (transferAmps * targetConductor.getResistance() * distance);

                                    this.electricityTransferQueue.add(new ElectricityTransferData(sender, receiver, electricityNetwork, ForgeDirection.getOrientation(i).getOpposite(), ampsReceived, voltsReceived));
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
            for (IConductor conductor : connection.conductors)
            {
                for (byte i = 0; i < conductor.getConnectedBlocks().length; i++)
                {
                    TileEntity tileEntity = conductor.getConnectedBlocks()[i];

                    if (tileEntity != null)
                    {
                        if (tileEntity instanceof IElectricityReceiver)
                        {
                            IElectricityReceiver electricUnit = (IElectricityReceiver)tileEntity;

                            if (electricUnit.canReceiveFromSide(ForgeDirection.getOrientation(i).getOpposite()))
                            {
                                need += Math.ceil(electricUnit.wattRequest());
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
        	IConductor conductor  = this.electricConductors.get(j);
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
	            
	            double totalAmpsReceived = 0;
	            ElectricityNetwork electricityNetwork = null;
	            
                for (int ii = 0; ii < electricityTransferQueue.size(); ii ++)
                {
                    if (electricityTransferQueue.get(ii).receiver == electricUnit)
                    {
                    	totalAmpsReceived += electricityTransferQueue.get(ii).amps;
                    	
                    	if(electricityNetwork == null)
                    	{
                    		electricityNetwork = electricityTransferQueue.get(ii).network;
                    	}
                    	
                    	electricUnit.onReceive(electricityTransferQueue.get(ii).sender, electricityTransferQueue.get(ii).amps, electricityTransferQueue.get(ii).voltage, electricityTransferQueue.get(ii).side);
    	                electricityTransferQueue.remove(ii);
                    }              
                }
                
                if(totalAmpsReceived > 0 || electricityNetwork != null)
                {
                	if(totalAmpsReceived > electricityNetwork.getLowestAmpConductor())
                	{
                		electricityNetwork.meltDown();
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