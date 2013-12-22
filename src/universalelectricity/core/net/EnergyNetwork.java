package universalelectricity.core.net;

import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.electricity.ElectricalEvent.EnergyUpdateEvent;
import universalelectricity.api.energy.IConductor;
import universalelectricity.api.energy.IEnergyNetwork;
import universalelectricity.api.net.IConnector;
import universalelectricity.api.net.INetwork;
import universalelectricity.api.vector.Vector3;
import universalelectricity.core.path.ConnectionPathfinder;
import universalelectricity.core.path.Pathfinder;

/**
 * @author Calclavia
 * 
 */
public class EnergyNetwork extends Network<IEnergyNetwork, IConductor, TileEntity> implements IEnergyNetwork
{
	/**
	 * The energy to be distributed on the next update.
	 */
	private long energyBuffer;

	/**
	 * The maximum buffer that the network can take. It is the average of all energy capacitance of
	 * the conductors.
	 */
	private long energyBufferCapacity;

	/**
	 * The total energy loss of this network. The loss is based on the loss in each conductor.
	 */
	private long networkEnergyLoss;

	/**
	 * The total energy buffer in the last tick.
	 */
	private long lastEnergyBuffer;

	/**
	 * The direction in which a conductor is placed relative to a specific conductor.
	 */
	private HashMap<Object, ForgeDirection> handlerDirectionMap = new HashMap<Object, ForgeDirection>();

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
			int handlerSize = this.handlerSet.size();

			this.lastEnergyBuffer = this.energyBuffer;
			long totalUsableEnergy = this.energyBuffer - this.networkEnergyLoss;
			long energyPerHandler = totalUsableEnergy / handlerSize;
			long energyRemainderHandler = (energyPerHandler + totalUsableEnergy % handlerSize);
			long remainingUsableEnergy = totalUsableEnergy;

			boolean isFirst = true;

			for (Object handler : this.handlerSet)
			{
				if (remainingUsableEnergy >= 0)
				{
					remainingUsableEnergy -= CompatibilityModule.receiveEnergy(handler, this.handlerDirectionMap.get(handler), isFirst ? energyRemainderHandler : energyPerHandler, true);
				}

				isFirst = false;
			}

			this.energyBuffer = Math.max(remainingUsableEnergy, 0);
		}
	}

	@Override
	public boolean canUpdate()
	{
		return this.getConnectors().size() > 0 && this.getNodes().size() > 0 && this.energyBuffer > 0 && this.energyBufferCapacity > 0;
	}

	@Override
	public boolean continueUpdate()
	{
		return this.canUpdate();
	}

	/**
	 * Clears all cache and reconstruct the network.
	 */
	@Override
	public void reconstruct()
	{
		if (this.connectorSet.size() > 0)
		{
			this.handlerSet.clear();
			this.handlerDirectionMap.clear();
			Iterator<IConductor> it = this.connectorSet.iterator();

			while (it.hasNext())
			{
				IConductor conductor = it.next();
				conductor.setNetwork(this);

				for (int i = 0; i < conductor.getConnections().length; i++)
				{
					TileEntity obj = conductor.getConnections()[i];

					if (obj != null)
					{
						if (CompatibilityModule.isHandler(obj))
						{
							this.handlerSet.add(obj);
							this.handlerDirectionMap.put(obj, ForgeDirection.getOrientation(i).getOpposite());
						}
					}
				}

				this.energyBufferCapacity += conductor.getEnergyCapacitance();
				this.networkEnergyLoss += conductor.getEnergyLoss();
			}

			this.energyBufferCapacity /= this.connectorSet.size();

			if (this.handlerSet.size() > 0)
			{
				NetworkTickHandler.addNetwork(this);
			}
		}
	}

	@Override
	public IEnergyNetwork merge(IEnergyNetwork network)
	{
		if (network != null && network != this)
		{
			IEnergyNetwork newNetwork = new EnergyNetwork();
			newNetwork.getConnectors().addAll(this.getConnectors());
			newNetwork.getConnectors().addAll(network.getConnectors());

			network.getConnectors().clear();
			this.connectorSet.clear();

			newNetwork.reconstruct();

			return newNetwork;
		}

		return null;
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
			TileEntity[] connectedBlocks = splitPoint.getConnections();

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
	public long produce(long amount)
	{
		long receive = Math.min(this.energyBufferCapacity - amount, amount);
		this.energyBuffer += receive;
		NetworkTickHandler.addNetwork(this);
		return receive;
	}

	@Override
	public long getLastBuffer()
	{
		return this.lastEnergyBuffer;
	}
}
