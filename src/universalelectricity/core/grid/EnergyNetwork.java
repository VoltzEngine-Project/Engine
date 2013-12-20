package universalelectricity.core.grid;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.IConnector;
import universalelectricity.api.energy.IConductor;
import universalelectricity.api.vector.Vector3;
import universalelectricity.core.electricity.ElectricalEvent.EnergyUpdateEvent;
import universalelectricity.core.path.ConnectionPathfinder;
import universalelectricity.core.path.Pathfinder;

/**
 * @author Calclavia
 * 
 */
public class EnergyNetwork extends Network<IEnergyNetwork, IConductor, TileEntity> implements IEnergyNetwork
{
	private int cachedEnergyLoss = -1;

	/**
	 * The amount of energy set to be distributed per conductor.
	 */
	private int energyDistribution;
	private int energyDistributionPerSide;

	@Override
	public void addConnector(IConductor connector)
	{
		connector.setNetwork(this);
		super.addConnector(connector);
	}

	@Override
	public void update()
	{
		EnergyUpdateEvent evt = new EnergyUpdateEvent(this);
		MinecraftForge.EVENT_BUS.post(evt);

		if (!evt.isCanceled())
		{
			int totalEnergy = 0;

			for (IConductor conductor : this.connectorSet)
			{
				totalEnergy += conductor.onExtractEnergy(null, conductor.getEnergyCapacitance(), true);
			}

			int energyLoss = getTotalEnergyLoss();
			int totalUsableEnergy = totalEnergy - energyLoss;
			int remainingUsableEnergy = totalUsableEnergy;

			this.energyDistribution = (totalUsableEnergy / this.connectorSet.size());
			this.energyDistributionPerSide = (this.energyDistribution / 6);

			for (IConductor conductor : this.connectorSet)
			{
				conductor.distribute();
			}
		}
	}

	@Override
	public void merge(IEnergyNetwork network)
	{
		if (network != null && network != this)
		{
			IEnergyNetwork newNetwork = new EnergyNetwork();
			newNetwork.getConnectors().addAll(this.getConnectors());
			newNetwork.getConnectors().addAll(network.getConnectors());
			network.getConnectors().clear();
			this.connectorSet.clear();
		}

	}

	@Override
	public void split(IConductor splitPoint)
	{
		if (splitPoint instanceof TileEntity)
		{
			World world = ((TileEntity) splitPoint).worldObj;

			this.removeConnector(splitPoint);

			/**
			 * Loop through the connected blocks and attempt to see if there are connections between
			 * the two points elsewhere.
			 */
			TileEntity[] connectedBlocks = splitPoint.getAdjacentConnections();

			for (int i = 0; i < connectedBlocks.length; i++)
			{
				TileEntity connectedBlockA = connectedBlocks[i];

				if (connectedBlockA instanceof IConnector)
				{
					for (int ii = 0; ii < connectedBlocks.length; ii++)
					{
						final TileEntity connectedBlockB = connectedBlocks[ii];

						if (connectedBlockA != connectedBlockB && connectedBlockB instanceof IConnector)
						{
							Pathfinder finder = new ConnectionPathfinder(world, (IConnector) connectedBlockB, splitPoint);
							finder.init(new Vector3(connectedBlockA));

							if (finder.results.size() > 0)
							{
								/**
								 * The connections A and B are still intact elsewhere. Set all
								 * references of wire connection into one network.
								 */

								for (Vector3 node : finder.closedSet)
								{
									TileEntity nodeTile = node.getTileEntity(world);

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
								try
								{
									/**
									 * The connections A and B are not connected anymore.
									 * Give them both a new common network.
									 */
									INetwork newNetwork = this.getClass().newInstance();

									for (Vector3 node : finder.closedSet)
									{
										TileEntity nodeTile = node.getTileEntity(world);

										if (nodeTile instanceof IConnector)
										{
											if (nodeTile != splitPoint)
											{
												newNetwork.addConnector((IConductor) nodeTile);
											}
										}
									}
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int getDistribution()
	{
		return this.energyDistribution;
	}

	@Override
	public int getDistributionSide()
	{
		return this.energyDistributionPerSide;
	}

	@Override
	public int getTotalEnergyLoss()
	{
		if (this.cachedEnergyLoss == -1)
		{
			this.cachedEnergyLoss = 0;

			for (IConductor conductor : this.getConnectors())
			{
				this.cachedEnergyLoss += conductor.getEnergyLoss();
			}
		}

		return this.cachedEnergyLoss;
	}

}
