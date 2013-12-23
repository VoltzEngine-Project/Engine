package universalelectricity.core.net;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.energy.EnergyNetworkLoader;
import universalelectricity.api.energy.IConductor;
import universalelectricity.api.energy.IEnergyNetwork;
import universalelectricity.api.net.IConnector;
import universalelectricity.api.net.NetworkEvent.EnergyProduceEvent;
import universalelectricity.api.net.NetworkEvent.EnergyUpdateEvent;

/**
 * The energy network, neglecting voltage.
 *
 * @author Calclavia
 */
public class EnergyNetwork extends Network<IEnergyNetwork, IConductor, Object> implements IEnergyNetwork
{
	/** The energy to be distributed on the next update. */
	private long energyBuffer;

	/**
	 * The maximum buffer that the network can take. It is the average of all energy capacitance of
	 * the conductors.
	 */
	private long energyBufferCapacity;

	/** The total energy loss of this network. The loss is based on the loss in each conductor. */
	private float resistance;

	/** The total energy buffer in the last tick. */
	private long lastEnergyBuffer;

	/** The cached amperage that was last sent in the network */
	private long amperageBuffer;

	/** Last cached value for network demand energy */
	private long lastNetworkRequest = -1;

	/** The direction in which a conductor is placed relative to a specific conductor. */
	private HashMap<Object, EnumSet<ForgeDirection>> handlerDirectionMap = new LinkedHashMap<Object, EnumSet<ForgeDirection>>();

	/**
	 * The energy sources cached for the next network update event.
	 */
	private Set<Object> sources = new HashSet<Object>();

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
			this.lastEnergyBuffer = this.energyBuffer;

			this.amperageBuffer = this.energyBuffer / UniversalElectricity.DEFAULT_VOLTAGE;
			long totalUsableEnergy = (long) (this.energyBuffer - this.getEnergyLoss(this.energyBuffer));
			long remainingUsableEnergy = totalUsableEnergy;

			int receiverCount = Math.max(this.getNodes().size() - this.sources.size(), 1);

			distribution:
			for (Entry<Object, EnumSet<ForgeDirection>> entry : handlerDirectionMap.entrySet())
			{
				if (entry.getValue() != null)
				{
					for (ForgeDirection direction : entry.getValue())
					{
						if (remainingUsableEnergy >= 0)
						{
							long energyPerReceiver = (remainingUsableEnergy / receiverCount) + totalUsableEnergy % receiverCount;
							remainingUsableEnergy -= CompatibilityModule.receiveEnergy(entry.getKey(), direction, energyPerReceiver, true);
						}
						else
						{
							break distribution;
						}
					}

					if (receiverCount > 1)
					{
						receiverCount--;
					}
				}
			}

			this.energyBuffer = Math.max(remainingUsableEnergy, 0);
			this.sources.clear();
		}

		// Clear the network request cache.
		this.lastNetworkRequest = -1;
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

	@Override
	public long getRequest()
	{
		if (this.lastNetworkRequest == -1)
		{
			this.lastNetworkRequest = 0;

			if (this.getNodes().size() > 0)
			{
				for (Entry<Object, EnumSet<ForgeDirection>> entry : handlerDirectionMap.entrySet())
				{
					if (entry.getValue() != null && !(entry.getValue() instanceof IConductor))
					{
						for (ForgeDirection direction : entry.getValue())
						{
							this.lastNetworkRequest += CompatibilityModule.receiveEnergy(entry.getKey(), direction, Long.MAX_VALUE, false);
						}
					}
				}
			}
		}

		return this.lastNetworkRequest;
	}

	@Override
	public float getResistance()
	{
		return this.resistance;
	}

	/** Clears all cache and reconstruct the network. */
	@Override
	public void reconstruct()
	{
		if (this.getConnectors().size() > 0)
		{
			// Reset all values related to wires
			this.getNodes().clear();
			this.handlerDirectionMap.clear();
			this.energyBufferCapacity = 0;
			this.resistance = 0;

			// Iterate threw list of wires
			Iterator<IConductor> it = this.getConnectors().iterator();

			while (it.hasNext())
			{
				IConductor conductor = it.next();

				if (conductor != null)
				{
					this.reconstructConductor(conductor);
				}
				else
				{
					it.remove();
				}
			}

			this.energyBufferCapacity /= this.getConnectors().size();

			if (this.getNodes().size() > 0)
			{
				NetworkTickHandler.addNetwork(this);
			}
		}
	}

	/** Segmented out call so overriding can be done when conductors are reconstructed. */
	protected void reconstructConductor(IConductor conductor)
	{
		conductor.setNetwork(this);

		for (int i = 0; i < conductor.getConnections().length; i++)
		{
			reconstructHandler(conductor.getConnections()[i], ForgeDirection.getOrientation(i).getOpposite());
		}

		this.energyBufferCapacity += conductor.getTransferCapacity();
		this.resistance += conductor.getResistance();
	}

	/** Segmented out call so overriding can be done when machines are reconstructed. */
	protected void reconstructHandler(Object obj, ForgeDirection side)
	{
		if (obj != null && !(obj instanceof IConductor))
		{
			if (CompatibilityModule.canConnect(obj, side))
			{
				EnumSet<ForgeDirection> set = this.handlerDirectionMap.get(obj);
				if (set == null)
				{
					set = EnumSet.noneOf(ForgeDirection.class);
				}
				this.getNodes().add(obj);
				set.add(side);
				this.handlerDirectionMap.put(obj, set);
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
			this.getConnectors().clear();

			newNetwork.reconstruct();

			return newNetwork;
		}

		return null;
	}

	@Override
	public void split(IConductor splitPoint)
	{
		this.removeConnector(splitPoint);
		this.reconstruct();

		/**
		 * Loop through the connected blocks and attempt to see if there are connections between the
		 * two points elsewhere.
		 */
		Object[] connectedBlocks = splitPoint.getConnections();

		for (int i = 0; i < connectedBlocks.length; i++)
		{
			Object connectedBlockA = connectedBlocks[i];

			if (connectedBlockA instanceof IConnector)
			{
				for (int ii = 0; ii < connectedBlocks.length; ii++)
				{
					final Object connectedBlockB = connectedBlocks[ii];

					if (connectedBlockA != connectedBlockB && connectedBlockB instanceof IConnector)
					{
						ConnectionPathfinder finder = new ConnectionPathfinder((IConnector) connectedBlockB, splitPoint);
						finder.findNodes((IConnector) connectedBlockA);

						if (finder.results.size() <= 0)
						{
							try
							{
								/**
								 * The connections A and B are not connected anymore. Give them both
								 * a new common network.
								 */
								IEnergyNetwork newNetwork = EnergyNetworkLoader.getNewNetwork();

								for (IConnector node : finder.closedSet)
								{
									if (node != splitPoint)
									{
										newNetwork.addConnector((IConductor) node);
									}
								}

								newNetwork.reconstruct();
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

	@Override
	public void split(IConductor connectorA, IConductor connectorB)
	{
		this.reconstruct();

		/**
		 * Check if connectorA connects with connectorB.
		 */
		ConnectionPathfinder finder = new ConnectionPathfinder((IConnector) connectorB);
		finder.findNodes((IConnector) connectorA);

		if (finder.results.size() <= 0)
		{
			try
			{
				/**
				 * The connections A and B are not connected anymore. Give them both
				 * a new common network.
				 */
				IEnergyNetwork newNetwork = EnergyNetworkLoader.getNewNetwork();

				for (IConnector node : finder.closedSet)
				{
					newNetwork.addConnector((IConductor) node);
				}

				newNetwork.reconstruct();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public long produce(Object source, ForgeDirection side, long amount, boolean doReceive)
	{
		EnergyProduceEvent evt = new EnergyProduceEvent(this, source, amount, doReceive);
		MinecraftForge.EVENT_BUS.post(evt);

		if (!evt.isCanceled() && amount > 0)
		{
			// Take account in the resistance of the system to prevent overflow.
			long energyReceived = Math.min(this.energyBufferCapacity - this.energyBuffer, amount);

			if (doReceive)
			{
				this.energyBuffer += energyReceived;
				NetworkTickHandler.addNetwork(this);
			}

			this.sources.add(source);

			return energyReceived;
		}

		return 0;
	}

	/**
	 * Assume voltage to be the default voltage for the energy network to calculate energy
	 * loss.
	 * Energy Loss Forumla:
	 * Delta V = I x R
	 * P = I x V
	 * Therefore: P = I^2 x R
	 */
	private long getEnergyLoss(long energy)
	{
		long amperage = energy / UniversalElectricity.DEFAULT_VOLTAGE;
		return (long) ((amperage * amperage) * this.resistance);
	}

	@Override
	public long getLastBuffer()
	{
		return this.lastEnergyBuffer;
	}

	@Override
	public long getBufferCapacity()
	{
		return this.energyBufferCapacity;
	}

	@Override
	public long getLastAmperageBuffer()
	{
		return this.amperageBuffer;
	}
}
