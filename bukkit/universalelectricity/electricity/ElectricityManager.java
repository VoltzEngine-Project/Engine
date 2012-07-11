package universalelectricity.electricity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.TileEntity;
import universalelectricity.UniversalElectricity;
import universalelectricity.Vector3;
import universalelectricity.extend.TileEntityConductor;


/**
 * The Class ElectricityManager.
 */
public class ElectricityManager
{
	
	/** The electric units. */
	private static List<IElectricUnit> electricUnits = new ArrayList<IElectricUnit>();
	
	/** The electric conductors. */
	private static List<TileEntityConductor> electricConductors = new ArrayList<TileEntityConductor>();

	/** The electricity transfer queue. */
	private static List<ElectricityTransferData> electricityTransferQueue = new ArrayList<ElectricityTransferData>();
	
	/** The wire connections. */
	private static List<ElectricityConnection> wireConnections = new ArrayList<ElectricityConnection>();
	
	/** The max connection id. */
	private static int maxConnectionID = 0;

	/** The in game ticks. */
	public static int inGameTicks = 0;

	/**
	 * Register electric unit.
	 *
	 * @param newUnit the new unit
	 */
	public static void registerElectricUnit(IElectricUnit newUnit)
	{
		if(!electricUnits.contains(newUnit))
		{
			electricUnits.add(newUnit);
		}
	}

	/**
	 * Register conductor.
	 *
	 * @param newConductor the new conductor
	 */
	public static void registerConductor(TileEntityConductor newConductor)
	{
		cleanUpConnections();
		wireConnections.add(new ElectricityConnection(getMaxConnectionID(), newConductor));

		if(!electricConductors.contains(newConductor))
		{
			electricConductors.add(newConductor);
		}
	}

	/**
	 * Merge connection.
	 *
	 * @param ID1 the i d1
	 * @param ID2 the i d2
	 */
	public static void mergeConnection(int ID1, int ID2)
	{
		if(ID1 != ID2)
		{
			ElectricityConnection connection1 = getConnectionByID(ID1);
			ElectricityConnection connection2 = getConnectionByID(ID2);

			connection1.conductors.addAll(connection2.conductors);
			connection1.setID(ID1);

			wireConnections.remove(connection2);
		}
	}

	/**
	 * Split connection.
	 *
	 * @param conductorA the conductor a
	 * @param conductorB the conductor b
	 */
	public static void splitConnection(TileEntityConductor conductorA, TileEntityConductor conductorB)
	{
		ElectricityConnection connection = getConnectionByID(conductorA.connectionID);
		connection.cleanUpArray();

		for(TileEntityConductor conductor : connection.conductors)
		{
			conductor.reset();
		}

		for(TileEntityConductor conductor : connection.conductors)
		{
			for(byte i = 0; i < 6; i++)
	        {
				conductor.updateConnectionWithoutSplit(UniversalElectricity.getUEUnitFromSide(conductor.world, new Vector3(conductor.x, conductor.y, conductor.z), i), i);
	        }
		}
	}

	/**
	 * Gets the connection by id.
	 *
	 * @param ID the id
	 * @return the connection by id
	 */
	public static ElectricityConnection getConnectionByID(int ID)
	{
		cleanUpConnections();

		for(int i = 0; i < wireConnections.size(); i++)
		{
			if(wireConnections.get(i).getID() == ID)
			{
				return wireConnections.get(i);
			}
		}

		return null;
	}

	/**
	 * Clean up connections.
	 */
	public static void cleanUpConnections()
	{
		for(int i = 0; i < wireConnections.size(); i++)
		{
			wireConnections.get(i).cleanUpArray();

			if(wireConnections.get(i).conductors.size() == 0)
			{
				wireConnections.remove(i);
			}
		}
	}

	/**
	 * Gets the max connection id.
	 *
	 * @return the max connection id
	 */
	public static int getMaxConnectionID()
	{
		maxConnectionID ++;
		return maxConnectionID;
	}

	/**
	 * Produce electricity.
	 *
	 * @param targetConductor the target conductor
	 * @param watts the watts
	 * @param voltage the voltage
	 */
	public static void produceElectricity(TileEntityConductor targetConductor, float watts, float voltage)
	{		
		if(targetConductor != null && watts > 0 && voltage > 0)
		{
			ElectricityConnection connection = getConnectionByID(targetConductor.connectionID);

			if(connection != null)
			{							
				List<IElectricUnit> allElectricUnitsInLine = connection.getConnectedElectricUnits();

				float leftOverWatts = watts;

				for(TileEntityConductor conductor : connection.conductors)
				{
					for(byte i = 0; i < conductor.connectedBlocks.length; i++)
					{
						TileEntity tileEntity = conductor.connectedBlocks[i];

						if(tileEntity != null)
						{
							if(tileEntity instanceof IElectricUnit)
							{
								IElectricUnit electricUnit = (IElectricUnit)tileEntity;

								if(electricUnit.electricityRequest() > 0 && electricUnit.canReceiveFromSide(UniversalElectricity.getOrientationFromSide(i, (byte)2)))
								{
						    		float transferWatts = Math.max(0, Math.min(leftOverWatts, Math.min(watts/allElectricUnitsInLine.size(), electricUnit.electricityRequest())));
						    		leftOverWatts -= transferWatts;
									electricityTransferQueue.add(new ElectricityTransferData(electricUnit, UniversalElectricity.getOrientationFromSide(i, (byte)2), transferWatts, voltage));
								}
							}
						}
					}
				}


			}			
		}
	}

	/**
	 * Electricity required.
	 *
	 * @param ID the id
	 * @return the float
	 */
	public static float electricityRequired(int ID)
	{
		ElectricityConnection connection = getConnectionByID(ID);

		float need = 0;

		if(connection != null)
		{
			for(TileEntityConductor conductor : connection.conductors)
			{
				for(byte i = 0; i < conductor.connectedBlocks.length; i++)
				{
					TileEntity tileEntity = conductor.connectedBlocks[i];

					if(tileEntity != null)
					{
						if(tileEntity instanceof IElectricUnit)
						{
							IElectricUnit electricUnit = (IElectricUnit)tileEntity;

							if(electricUnit.canReceiveFromSide(UniversalElectricity.getOrientationFromSide(i, (byte)2)))
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

	/**
	 * On update.
	 */
	public static void onUpdate()
	{
		for(TileEntityConductor conductor : electricConductors)
		{
			conductor.refreshConnectedBlocks();
		}

		for(IElectricUnit electricUnit : electricUnits)
		{
			if(electricUnit == null)
			{
				electricUnits.remove(electricUnit);
				break;
			}
			else if(((TileEntity)electricUnit).l())
			{
				electricUnits.remove(electricUnit);
				break;
			}

			if(inGameTicks % electricUnit.getTickInterval() == 0 && electricUnit.getTickInterval() > 0)
			{
				float watts = 0;
				float voltage = 0;
				byte side = -1;


				for(int i = 0; i < electricityTransferQueue.size(); i ++)
				{
					if(electricityTransferQueue.get(i).eletricUnit == electricUnit)
					{
						if(side == -1)
						{
							watts = electricityTransferQueue.get(i).watts;
							voltage = electricityTransferQueue.get(i).voltage;
							side = electricityTransferQueue.get(i).side;
							electricityTransferQueue.remove(i);
						}
						else if(electricityTransferQueue.get(i).side == side)
						{
							watts += electricityTransferQueue.get(i).watts;
							voltage = Math.min(voltage, electricityTransferQueue.get(i).voltage);
							electricityTransferQueue.remove(i);
						}
					}
				}

				electricUnit.onUpdate(watts, voltage, side);
			}
		}

		inGameTicks ++;
	}
}