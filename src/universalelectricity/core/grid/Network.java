package universalelectricity.core.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.IConnector;
import universalelectricity.api.energy.IEnergyConductor;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorHelper;
import universalelectricity.core.electricity.ElectricalEvent.EnergyUpdateEvent;
import universalelectricity.core.electricity.ElectricalEvent.ElectricityRequestEvent;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.path.Pathfinder;
import universalelectricity.core.path.PathfinderChecker;
import cpw.mods.fml.common.FMLLog;

/**
 * An grid-like, world cable-based network.
 * 
 * @author Calclavia
 * 
 */
public abstract class Network<N, C, A> implements INetwork<N, C, A>
{
	/**
	 * A set of nodes (e.g receivers).
	 */
	private final Set<A> nodeSet = new LinkedHashSet<A>();

	/**
	 * A set of connectors (e.g conductors).
	 */
	private final Set<C> connectorSet = new LinkedHashSet<C>();

	@Override
	public void addConnector(C connector)
	{
		this.connectorSet.add(connector);
	}

	@Override
	public void removeConnector(C connector)
	{
		this.connectorSet.add(connector);
	}

	@Override
	public Set<C> getConnectors()
	{
		return this.connectorSet;
	}

	
	/**
	 * @return How much electricity this network needs.
	 */
	@Override
	public ElectricityPack getRequest(TileEntity... ignoreTiles)
	{
		List<ElectricityPack> requests = new ArrayList<ElectricityPack>();

		Iterator<TileEntity> it = this.getNodes().iterator();

		while (it.hasNext())
		{
			TileEntity tileEntity = it.next();

			if (Arrays.asList(ignoreTiles).contains(tileEntity))
			{
				continue;
			}

			if (tileEntity instanceof IEnergyInterface)
			{
				if (!tileEntity.isInvalid())
				{
					if (tileEntity.worldObj.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) == tileEntity)
					{
						for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
						{
							if (((IEnergyInterface) tileEntity).canConnect(direction) && this.getConnectors().contains(VectorHelper.getConnectorFromSide(tileEntity.worldObj, new Vector3(tileEntity), direction)))
							{
								requests.add(ElectricityPack.getFromWatts(((IEnergyInterface) tileEntity).getRequest(direction), ((IEnergyInterface) tileEntity).getVoltage(direction)));
								continue;
							}
						}
					}
				}
			}
		}

		ElectricityPack mergedPack = ElectricityPack.merge(requests);
		ElectricityRequestEvent evt = new ElectricityRequestEvent(this, mergedPack, ignoreTiles);
		MinecraftForge.EVENT_BUS.post(evt);
		return mergedPack;
	}

	/**
	 * @return Returns all producers in this electricity network.
	 */
	@Override
	public Set<TileEntity> getNodes()
	{
		return this.nodes.keySet();
	}

	/**
	 * @param tile The tile to get connections for
	 * @return The list of directions that can be connected to for the provided tile
	 */
	@Override
	public ArrayList<ForgeDirection> getPossibleDirections(TileEntity tile)
	{
		return this.nodes.containsKey(tile) ? this.nodes.get(tile) : null;
	}

	/**
	 * This function is called to refresh all conductors in this network
	 */
	@Override
	public void refresh()
	{
		this.nodes.clear();

		try
		{
			Iterator<IEnergyConductor> it = this.connectorSet.iterator();

			while (it.hasNext())
			{
				IEnergyConductor conductor = it.next();

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

				for (int i = 0; i < conductor.getAdjacentConnections().length; i++)
				{
					TileEntity acceptor = conductor.getAdjacentConnections()[i];

					if (!(acceptor instanceof IEnergyConductor) && acceptor instanceof IConnector)
					{
						ArrayList<ForgeDirection> possibleDirections = null;

						if (this.nodes.containsKey(acceptor))
						{
							possibleDirections = this.nodes.get(acceptor);
						}
						else
						{
							possibleDirections = new ArrayList<ForgeDirection>();
						}

						possibleDirections.add(ForgeDirection.getOrientation(i));

						this.nodes.put(acceptor, possibleDirections);
					}
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
	public float getLowestCurrentCapacity()
	{
		float lowestAmperage = 0;

		for (IEnergyConductor conductor : this.connectorSet)
		{
			if (lowestAmperage == 0 || conductor.getEnergyCapacitance() < lowestAmperage)
			{
				lowestAmperage = conductor.getEnergyCapacitance();
			}
		}

		return lowestAmperage;
	}

	@Override
	public void merge(IEnergyNetwork network)
	{
		if (network != null && network != this)
		{
			Network newNetwork = new Network();
			newNetwork.getConnectors().addAll(this.getConnectors());
			newNetwork.getConnectors().addAll(network.getConnectors());
			newNetwork.refresh();
		}
	}

	@Override
	public void split(IEnergyConductor splitPoint)
	{
		if (splitPoint instanceof TileEntity)
		{
			this.getConnectors().remove(splitPoint);

			/**
			 * Loop through the connected blocks and attempt to see if there are connections between
			 * the two points elsewhere.
			 */
			TileEntity[] connectedBlocks = splitPoint.getAdjacentConnections();

			for (int i = 0; i < connectedBlocks.length; i++)
			{
				TileEntity connectedBlockA = connectedBlocks[i];

				if (connectedBlockA instanceof INetworkConnection)
				{
					for (int ii = 0; ii < connectedBlocks.length; ii++)
					{
						final TileEntity connectedBlockB = connectedBlocks[ii];

						if (connectedBlockA != connectedBlockB && connectedBlockB instanceof INetworkConnection)
						{
							Pathfinder finder = new PathfinderChecker(((TileEntity) splitPoint).worldObj, (INetworkConnection) connectedBlockB, splitPoint);
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

									if (nodeTile instanceof IConnector)
									{
										if (nodeTile != splitPoint)
										{
											((IConnector) nodeTile).setNetwork(this);
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
								IEnergyNetwork newNetwork = new Network();

								for (Vector3 node : finder.closedSet)
								{
									TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).worldObj);

									if (nodeTile instanceof IConnector)
									{
										if (nodeTile != splitPoint)
										{
											newNetwork.getConnectors().add((IEnergyConductor) nodeTile);
										}
									}
								}

								newNetwork.refresh();
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
		return this.getClass().getSimpleName() + "[" + this.hashCode() + "|Connectors:" + this.connectorSet.size() + "|Acceptors:" + this.nodes.size() + "]";
	}
}
