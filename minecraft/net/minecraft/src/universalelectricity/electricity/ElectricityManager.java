package net.minecraft.src.universalelectricity.electricity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.TileEntity;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.Vector3;
import net.minecraft.src.universalelectricity.extend.TileEntityConductor;

/**
 * This class is used to manage electricity transfering and flow
 * @author Calclavia
 *
 */
public class ElectricityManager
{
    private static List<IElectricUnit> electricUnits = new ArrayList<IElectricUnit>();
    private static List<TileEntityConductor> electricConductors = new ArrayList<TileEntityConductor>();

    private static List<ElectricityTransferData> electricityTransferQueue = new ArrayList<ElectricityTransferData>();
    private static List<ElectricityConnection> wireConnections = new ArrayList<ElectricityConnection>();
    private static int maxConnectionID = 0;

    public static int inGameTicks = 0;

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
                conductor.updateConnectionWithoutSplit(UniversalElectricity.getUEUnitFromSide(conductor.worldObj, new Vector3(conductor.xCoord, conductor.yCoord, conductor.zCoord), i), i);
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
        if (targetConductor != null && watts > 0 && voltage > 0)
        {
            //Find a path between this conductor and all connected units and try to send the electricity to them directly
            ElectricityConnection connection = getConnectionByID(targetConductor.connectionID);

            if (connection != null)
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

                                if (electricUnit.electricityRequest() > 0 && electricUnit.canReceiveFromSide(Vector3.getOrientationFromSide(i, (byte)2)))
                                {
                                    float transferWatts = Math.max(0, Math.min(leftOverWatts, Math.min(watts / allElectricUnitsInLine.size(), electricUnit.electricityRequest())));
                                    leftOverWatts -= transferWatts;
                                    electricityTransferQueue.add(new ElectricityTransferData(electricUnit, Vector3.getOrientationFromSide(i, (byte)2), transferWatts, voltage));
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

                            if (electricUnit.canReceiveFromSide(Vector3.getOrientationFromSide(i, (byte)2)))
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

    public static void onUpdate()
    {
        for(int j = 0; j < electricConductors.size(); j ++)
        {
        	TileEntityConductor conductor  = electricConductors.get(j);
            conductor.refreshConnectedBlocks();
        }

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

            if (inGameTicks % electricUnit.getTickInterval() == 0 && electricUnit.getTickInterval() > 0)
            {
                float watts = 0;
                float voltage = 0;
                byte side = -1;

                //Try to stack all electricity from one side into one update
                for (int ii = 0; ii < electricityTransferQueue.size(); ii ++)
                {
                    if (electricityTransferQueue.get(ii).eletricUnit == electricUnit)
                    {
                        //If the side is not set for this tick
                        if (side == -1)
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

        inGameTicks ++;
    }
}