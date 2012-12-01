package universalelectricity.core.electricity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.FMLLog;

import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/**
 * THIS IS THE NEW ELECTRICITY MANAGER. THIS IS ONLY A DRAFT!
 * 
 * The Electricity Network Manager.
 * 
 * @author Calclavia
 * 
 */
public class Electricity
{
	public static Electricity instance = new Electricity();

	private List<TileEntity> producers = new ArrayList<TileEntity>();
	private List<TileEntity> receivers = new ArrayList<TileEntity>();

	private List<ElectricityNetwork> electricityNetworks = new ArrayList<ElectricityNetwork>();

	public void registerProducer(TileEntity tileEntity)
	{
		if (!producers.contains(tileEntity))
			producers.add(tileEntity);
	}
	

	public void registerReceiver(TileEntity tileEntity)
	{
		if (!receivers.contains(tileEntity))
			receivers.add(tileEntity);
	}

	/**
	 * Registers a conductor into the UE electricity net.
	 */
	public void registerConductor(IConductor newConductor)
	{
		cleanUpConnections();
		this.electricityNetworks.add(new ElectricityNetwork(newConductor));
	}
	
	public void unregister(TileEntity tileEntity)
	{
		if (producers.contains(tileEntity))
		{
			producers.remove(tileEntity);
		}
		
		if (receivers.contains(tileEntity))
		{
			receivers.remove(tileEntity);
		}
		
		for(ElectricityNetwork network : this.electricityNetworks)
		{
			network.stopProducing(tileEntity);
			network.stopRequesting(tileEntity);
		}
		
		System.out.println("UNREGISTERED");
	}

	/**
	 * Merges two connection lines together into one.
	 * 
	 * @param networkA
	 *            - The network to be merged into. This network will be kept.
	 * @param networkB
	 *            - The network to be merged. This network will be deleted.
	 */
	public void mergeConnection(ElectricityNetwork networkA, ElectricityNetwork networkB)
	{
		if (networkA != networkB)
		{
			if (networkA != null && networkB != null)
			{
				networkA.conductors.addAll(networkB.conductors);
				networkA.setNetwork();
				this.electricityNetworks.remove(networkB);
				networkB = null;
			}
			else
			{
				System.err.println("Failed to merge Universal Electricity wire connections!");
			}
		}
	}

	/**
	 * Separate one connection line into two different ones between two conductors. This function
	 * does this by resetting all wires in the connection line and making them each reconnect.
	 * 
	 * @param conductorA
	 *            - existing conductor
	 * @param conductorB
	 *            - broken/invalid conductor
	 */
	public void splitConnection(IConductor conductorA, IConductor conductorB)
	{
		ElectricityNetwork connection = conductorA.getNetwork();

		if (connection != null)
		{
			connection.cleanConductors();

			for (IConductor conductor : connection.conductors)
			{
				conductor.reset();
			}

			for (IConductor conductor : connection.conductors)
			{
				for (byte i = 0; i < 6; i++)
				{
					conductor.updateConnectionWithoutSplit(Vector3.getConnectorFromSide(conductor.getWorld(), new Vector3(((TileEntity) conductor).xCoord, ((TileEntity) conductor).yCoord, ((TileEntity) conductor).zCoord), ForgeDirection.getOrientation(i)), ForgeDirection.getOrientation(i));
				}
			}
		}
		else
		{
			FMLLog.severe("Conductor invalid network while splitting connection!");
		}
	}

	/**
	 * Clean up and remove all useless and invalid connections.
	 */
	public void cleanUpConnections()
	{
		try
		{
			for (int i = 0; i < this.electricityNetworks.size(); i++)
			{
				this.electricityNetworks.get(i).cleanConductors();

				if (this.electricityNetworks.get(i).conductors.size() == 0)
				{
					this.electricityNetworks.remove(i);
				}
			}
		}
		catch (Exception e)
		{
			FMLLog.severe("Failed to clean up wire connections!");
		}
	}
}
