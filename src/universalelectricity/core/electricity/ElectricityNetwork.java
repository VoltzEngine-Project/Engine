package universalelectricity.core.electricity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnectionProvider;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.path.Pathfinder;
import universalelectricity.core.path.PathfinderChecker;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import cpw.mods.fml.common.FMLLog;

/**
 * An Electrical Network specifies a wire connection. Each wire connection line will have its own
 * electrical network.
 * 
 * !! Do not include this class if you do not intend to have custom wires in your mod. This will
 * increase future compatibility. !!
 * 
 * @author Calclavia
 * 
 */
public class ElectricityNetwork implements IElectricityNetwork
{
	public Set<TileEntity> electricalTiles = new HashSet<TileEntity>();
	public Map<TileEntity, ForgeDirection> acceptorDirections = new HashMap<TileEntity, ForgeDirection>();

	private final Set<IConductor> conductors = new HashSet<IConductor>();

	public ElectricityNetwork()
	{

	}

	public ElectricityNetwork(IConductor... conductors)
	{
		this.conductors.addAll(Arrays.asList(conductors));
	}

	@Override
	public float produce(float energy, float voltage, TileEntity... ignoreTiles)
	{
		double prevSending = energy;

		Set<TileEntity> avaliableEnergyTiles = this.getElectrical();

		if (!avaliableEnergyTiles.isEmpty())
		{
			int divider = avaliableEnergyTiles.size();
			double remaining = energy % divider;
			double sending = (energy - remaining) / divider;

			for (TileEntity acceptor : avaliableEnergyTiles)
			{
				double currentSending = sending + remaining;

				remaining = 0;

				if (acceptor instanceof IElectrical)
				{
					energy -= (currentSending - ((IStrictEnergyAcceptor) acceptor).transferEnergyToAcceptor(currentSending));
				}
			}

			try
			{
				ElectricityPack tileRequest = this.consumers.get(tileEntity);

				if (this.consumers.containsKey(tileEntity) && tileRequest != null)
				{
					// Calculate the electricity this TileEntity is receiving in percentage.
					totalElectricity = this.getProduced();

					if (totalElectricity.getWatts() > 0)
					{
						ElectricityPack totalRequest = this.getRequestWithoutReduction();
						totalElectricity.amperes *= (tileRequest.amperes / totalRequest.amperes);

						int distance = this.getConductors().size();
						double ampsReceived = totalElectricity.amperes - (totalElectricity.amperes * totalElectricity.amperes * this.getTotalResistance()) / totalElectricity.voltage;
						double voltsReceived = totalElectricity.voltage - (totalElectricity.amperes * this.getTotalResistance());

						totalElectricity.amperes = ampsReceived;
						totalElectricity.voltage = voltsReceived;

						return totalElectricity;
					}
				}
			}
			catch (Exception e)
			{
				FMLLog.severe("Universal Electricity: Failed to produce electricity!");
				e.printStackTrace();
			}

		}

		return energy;
	}

	/**
	 * @return How much electricity this network needs.
	 */
	@Override
	public float getRequest(TileEntity... ignoreTiles)
	{
		float requiredElectricity = 0;

		Iterator<TileEntity> it = this.getElectrical().iterator();

		loop:
		while (it.hasNext())
		{
			TileEntity tileEntity = it.next();

			if (ignoreTiles != null)
			{
				for (TileEntity ignoreTile : ignoreTiles)
				{
					if (tileEntity == ignoreTile)
					{
						continue loop;
					}
				}
			}

			if (tileEntity instanceof IElectrical)
			{
				if (!tileEntity.isInvalid())
				{
					if (tileEntity.worldObj.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) == tileEntity)
					{
						requiredElectricity += ((IElectrical) tileEntity).getRequest();
						continue;
					}
				}
			}

			it.remove();

		}

		return requiredElectricity;
	}

	/**
	 * @return Returns all producers in this electricity network.
	 */
	@Override
	public Set<TileEntity> getElectrical()
	{
		return this.electricalTiles;
	}

	@Override
	public void cleanUpConductors()
	{
		Iterator it = this.conductors.iterator();

		while (it.hasNext())
		{
			IConductor conductor = (IConductor) it.next();

			if (conductor == null)
			{
				it.remove();
			}
			else if (((TileEntity) conductor).isInvalid())
			{
				it.remove();
			}
			else
			{
				conductor.setNetwork(this);
			}
		}
	}

	/**
	 * This function is called to refresh all conductors in this network
	 */
	@Override
	public void refresh()
	{
		this.cleanUpConductors();

		this.electricalTiles.clear();

		try
		{
			Iterator<IConductor> it = this.conductors.iterator();

			while (it.hasNext())
			{
				IConductor conductor = it.next();
				conductor.updateAdjacentConnections();

				for (TileEntity acceptor : conductor.getAdjacentConnections())
				{
					this.electricalTiles.add(acceptor);
				}
			}
		}
		catch (Exception e)
		{
			FMLLog.severe("Universal Electricity: Failed to refresh conductor.");
			e.printStackTrace();
		}
	}

	@Override
	public float getTotalResistance()
	{
		float resistance = 0;

		for (IConductor conductor : this.conductors)
		{
			resistance += conductor.getResistance();
		}

		return resistance;
	}

	@Override
	public float getLowestCurrentCapacity()
	{
		float lowestAmperage = 0;

		for (IConductor conductor : this.conductors)
		{
			if (lowestAmperage == 0 || conductor.getCurrentCapcity() < lowestAmperage)
			{
				lowestAmperage = conductor.getCurrentCapcity();
			}
		}

		return lowestAmperage;
	}

	@Override
	public Set<IConductor> getConductors()
	{
		return this.conductors;
	}

	@Override
	public void merge(IElectricityNetwork network)
	{
		if (network != null && network != this)
		{
			ElectricityNetwork newNetwork = new ElectricityNetwork();
			newNetwork.getConductors().addAll(this.getConductors());
			newNetwork.getConductors().addAll(network.getConductors());
			newNetwork.cleanUpConductors();
		}
	}

	@Override
	public void split(IConnectionProvider splitPoint)
	{
		if (splitPoint instanceof TileEntity)
		{
			this.getConductors().remove(splitPoint);

			/*
			for (ForgeDirection dir : ForgeDirection.values())
			{
				if (dir != ForgeDirection.UNKNOWN)
				{
					Vector3 splitVec = new Vector3((TileEntity) splitPoint);
					TileEntity tileAroundSplit = VectorHelper.getTileEntityFromSide(((TileEntity) splitPoint).worldObj, splitVec, dir);
				}
			}*/

			/**
			 * Loop through the connected blocks and attempt to see if there are connections between
			 * the two points elsewhere.
			 */
			TileEntity[] connectedBlocks = splitPoint.getAdjacentConnections();

			for (int i = 0; i < connectedBlocks.length; i++)
			{
				TileEntity connectedBlockA = connectedBlocks[i];

				if (connectedBlockA instanceof IConnectionProvider)
				{
					for (int ii = 0; ii < connectedBlocks.length; ii++)
					{
						final TileEntity connectedBlockB = connectedBlocks[ii];

						if (connectedBlockA != connectedBlockB && connectedBlockB instanceof IConnectionProvider)
						{
							Pathfinder finder = new PathfinderChecker(((TileEntity) splitPoint).worldObj, (IConnectionProvider) connectedBlockB, splitPoint);
							finder.init(new Vector3(connectedBlockA));

							if (finder.results.size() > 0)
							{
								/**
								 * The connections A and B are still intact elsewhere. Set all
								 * references of wire connection into one network.
								 */

								for (Vector3 node : finder.closedSet)
								{
									TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).worldObj);

									if (nodeTile instanceof INetworkProvider)
									{
										if (nodeTile != splitPoint)
										{
											((INetworkProvider) nodeTile).setNetwork(this);
										}
									}
								}
							}
							else
							{
								/**
								 * The connections A and B are not connected anymore. Give both of
								 * them a new network.
								 */
								IElectricityNetwork newNetwork = new ElectricityNetwork();

								for (Vector3 node : finder.closedSet)
								{
									TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).worldObj);

									if (nodeTile instanceof INetworkProvider)
									{
										if (nodeTile != splitPoint)
										{
											newNetwork.getConductors().add((IConductor) nodeTile);
										}
									}
								}

								newNetwork.cleanUpConductors();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String toString()
	{
		return "ElectricityNetwork[" + this.hashCode() + "|Wires:" + this.conductors.size() + "]";
	}

}
