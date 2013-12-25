package universalelectricity.core.net;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.energy.EnergyNetworkLoader;
import universalelectricity.api.energy.IConductor;
import universalelectricity.api.energy.IEnergyNetwork;
import universalelectricity.api.net.IConnector;
import universalelectricity.api.net.NetworkEvent.EnergyProduceEvent;

/**
 * The energy network, neglecting voltage.
 * 
 * @author Calclavia
 */
public class EnergyNetwork extends Network<IEnergyNetwork, IConductor, Object> implements IEnergyNetwork
{
	/** The energy to be distributed on the next update. */
	protected long energyBuffer;

	/**
	 * The individual buffer from each conductor. Used to limit the amount of buffer that can be
	 * stored.
	 */
	protected HashMap<IConductor, Long> conductorBuffer = new HashMap<IConductor, Long>();

	/**
	 * The total resistance of this entire network. The loss is based on the resistance in each
	 * conductor.
	 */
	protected float resistance;

	/** The total energy buffer in the last tick. */
	protected long lastEnergyBuffer;

	/** Last cached value for network demand energy */
	protected long lastNetworkRequest = -1;

	/** The direction in which a conductor is placed relative to a specific conductor. */
	protected HashMap<Object, EnumSet<ForgeDirection>> handlerDirectionMap = new LinkedHashMap<Object, EnumSet<ForgeDirection>>();

	/** A reference to which handlers a conductor is connected to. */
	protected final HashMap<IConductor, Object[]> conductorHandlerMap = new HashMap<IConductor, Object[]>();

	@Override
	public void addConnector(IConductor connector)
	{
		connector.setNetwork(this);
		super.addConnector(connector);
	}

	@Override
	public void update()
	{
		/**
		 * Extract all buffer and calculate the buffer that needs to be distributed this tick.
		 */
		long buffer = this.energyBuffer - this.getEnergyLoss();

		/**
		 * Calculate how much energy should be distributed to each active conductor.
		 */
		long perConductor = buffer / this.conductorBuffer.size();
		long perConductorFirst = perConductor + buffer % this.conductorBuffer.size();
		boolean isFirst = true;
		long energyUsed = 0;

		/**
		 * For each conductor, output the energy into the handlers.
		 */
		for (Entry<IConductor, Object[]> entry : conductorHandlerMap.entrySet())
		{
			long perHandler = (isFirst ? perConductorFirst : perConductor) / entry.getValue().length;

			for (int i = 0; i < entry.getValue().length; i++)
			{
				energyUsed += this.addEnergyToHandler(entry.getValue(), ForgeDirection.getOrientation(i).getOpposite(), perHandler, true);
			}

			isFirst = false;
		}

		/**
		 * Equalize the buffer in each relevant conductor's internal buffer to prevent
		 * over-injection of energy for some machines.
		 */
		this.energyBuffer = Math.max(buffer - energyUsed, 0);
		long remainingBufferPerConductor = this.energyBuffer / this.conductorBuffer.size();
		Iterator<Entry<IConductor, Long>> it = this.conductorBuffer.entrySet().iterator();

		while (it.hasNext())
		{
			Entry<IConductor, Long> entry = it.next();
			entry.setValue(remainingBufferPerConductor);
		}

		// Clear the network request cache.
		this.lastNetworkRequest = -1;
	}

	/**
	 * Applies energy to a handler
	 * 
	 * @param handler - machine getting the power
	 * @param side - side to apply the power
	 * @param energy - energy sum
	 * @param doApply - true it will give the power, false will check how much power will be
	 * received
	 * @return energy accepted
	 */
	public long addEnergyToHandler(Object handler, ForgeDirection side, long energy, boolean doApply)
	{
		return CompatibilityModule.receiveEnergy(handler, side, energy, doApply);
	}

	@Override
	public boolean canUpdate()
	{
		return this.getConnectors().size() > 0 && this.getNodes().size() > 0 && this.energyBuffer > 0;
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
							this.lastNetworkRequest += Math.max(CompatibilityModule.receiveEnergy(entry.getKey(), direction, Long.MAX_VALUE, false), 0);
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
		if (network instanceof EnergyNetwork && network != this)
		{
			long newBuffer = this.getBuffer();
			newBuffer += ((EnergyNetwork) network).getBuffer();
			EnergyNetwork newNetwork = new EnergyNetwork();
			newNetwork.getConnectors().addAll(this.getConnectors());
			newNetwork.getConnectors().addAll(network.getConnectors());

			network.getConnectors().clear();
			network.getNodes().clear();
			this.getConnectors().clear();
			this.getNodes().clear();

			newNetwork.reconstruct();

			newNetwork.setBuffer(newBuffer);
			return newNetwork;
		}

		return null;
	}

	@Override
	public void split(IConductor splitPoint)
	{
		this.removeConnector(splitPoint);
		this.reconstruct();
		this.saveBuffer();

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
									if (node != splitPoint && node instanceof IConductor)
									{
										newNetwork.addConnector((IConductor) node);
										this.removeConnector((IConductor) node);
									}
								}
								newNetwork.loadBuffer();
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
		this.saveBuffer();

		/** Check if connectorA connects with connectorB. */
		ConnectionPathfinder finder = new ConnectionPathfinder(connectorB);
		finder.findNodes(connectorA);

		if (finder.results.size() <= 0)
		{
			try
			{
				/**
				 * The connections A and B are not connected anymore. Give them both a new common
				 * network.
				 */
				IEnergyNetwork newNetwork = EnergyNetworkLoader.getNewNetwork();

				for (IConnector node : finder.closedSet)
				{
					newNetwork.addConnector((IConductor) node);
				}

				newNetwork.reconstruct();
				newNetwork.loadBuffer();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public long produce(IConductor conductor, ForgeDirection from, long amount, boolean doReceive)
	{
		EnergyProduceEvent evt = new EnergyProduceEvent(this, conductor, amount, doReceive);
		MinecraftForge.EVENT_BUS.post(evt);

		if (!evt.isCanceled() && amount > 0)
		{
			long conductorBuffer = 0;

			if (this.conductorBuffer.containsKey(conductor))
			{
				conductorBuffer = this.conductorBuffer.get(conductor);
			}

			long energyReceived = Math.min((conductor.getCurrentCapacity() * UniversalElectricity.DEFAULT_VOLTAGE) - conductorBuffer, amount);

			if (doReceive && energyReceived > 0)
			{
				this.energyBuffer += energyReceived;
				conductorBuffer += energyReceived;
				this.conductorBuffer.put(conductor, conductorBuffer);
				NetworkTickHandler.addNetwork(this);
			}

			return Math.max(energyReceived, 0);
		}

		return 0;
	}

	/**
	 * Assume voltage to be the default voltage for the energy network to calculate energy loss.
	 * Energy Loss Forumla: Delta V = I x R; P = I x V; Therefore: P = I^2 x R
	 */
	protected long getEnergyLoss()
	{
		long amperage = this.getBuffer() / this.getVoltage();
		return (long) ((amperage * amperage) * this.resistance);
	}

	public long getVoltage()
	{
		return UniversalElectricity.DEFAULT_VOLTAGE;
	}

	public long getBuffer()
	{
		return this.energyBuffer;
	}

	public void setBuffer(long newBuffer)
	{
		this.energyBuffer = newBuffer;
	}

	@Override
	public long getLastBuffer()
	{
		return this.lastEnergyBuffer;
	}

	@Override
	public void saveBuffer()
	{
		if (this.getConnectors().size() > 0)
		{
			long energyPerPart = 0;
			for (IConductor conductor : this.getConnectors())
			{
				energyPerPart = (this.energyBuffer / this.getConnectors().size()) + this.energyBuffer % this.getConnectors().size();
				conductor.setSaveBuffer(energyPerPart);
			}
		}
	}

	@Override
	public void loadBuffer()
	{
		this.energyBuffer = 0;
		for (IConductor conductor : this.getConnectors())
		{
			this.energyBuffer += conductor.getSavedBuffer();
		}
	}

}
