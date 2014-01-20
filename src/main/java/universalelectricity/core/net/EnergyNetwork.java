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
public class EnergyNetwork extends NodeNetwork<IEnergyNetwork, IConductor, Object> implements IEnergyNetwork
{
	/** The energy to be distributed on the next update. */
	protected long energyBuffer;

	/**
	 * The individual buffer from each conductor. Used to limit the amount of buffer that can be
	 * stored.
	 */
	protected final HashMap<IConductor, Long> conductorBuffer = new HashMap<IConductor, Long>();

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
	protected final HashMap<Object, EnumSet<ForgeDirection>> handlerDirectionMap = new LinkedHashMap<Object, EnumSet<ForgeDirection>>();

	@Override
	public void addConnector(IConductor connector)
	{
		connector.setNetwork(this);
		this.conductorBuffer.put(connector, (long) 0);
		super.addConnector(connector);
	}

	@Override
	public void update()
	{
		/** Energy we have to move after loss has been removed */
		long usableEnergy = Math.max(this.energyBuffer - this.getEnergyLoss(), 0);
		/** Energy currently left after we have moved some */
		long currentEnergy = usableEnergy;
		/** Energy per handler */
		long perHandler = 0;
		/** Energy per side of a handler */
		long perSide = 0;
		/** Number of handlers */
		int handlers = handlerDirectionMap.size();
		if (handlers > 0)
		{
			/** For each conductor, output the energy into the handlers. */
			for (Entry<Object, EnumSet<ForgeDirection>> entry : handlerDirectionMap.entrySet())
			{
				/** Energy to give to each handler */
				perHandler = (usableEnergy / handlers) + (usableEnergy % handlers);

				for (ForgeDirection direction : entry.getValue())
				{
					/** Energy per to give per side */
					perSide = (perHandler / entry.getValue().size()) + (perHandler % entry.getValue().size());
					currentEnergy -= this.addEnergyToHandler(entry.getKey(), direction, perSide, true);
				}
				if (handlers > 1)
				{
					handlers--;
				}
			}

			/** Don't set energy if none was sent to prevent energy loss */
			if (usableEnergy != currentEnergy)
				this.energyBuffer = Math.max(currentEnergy, 0);
		}
		long remainingBufferPerConductor = this.energyBuffer / this.getConnectors().size();
		Iterator<IConductor> it = this.getConnectors().iterator();

		while (it.hasNext())
		{
			this.conductorBuffer.put(it.next(), remainingBufferPerConductor);
		}

		// Clear the network request cache.
		this.lastNetworkRequest = -1;
	}

	/** Applies power to the machine */
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
					this.reconstructConnector(conductor);
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

	@Override
	protected void reconstructConnector(IConductor conductor)
	{
		conductor.setNetwork(this);

		if (conductor.getConnections() != null)
		{
			for (int i = 0; i < conductor.getConnections().length; i++)
			{
				reconstructHandler(conductor.getConnections()[i], ForgeDirection.getOrientation(i).getOpposite());
			}
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
		long newBuffer = getBuffer() + ((EnergyNetwork) network).getBuffer();
		IEnergyNetwork newNetwork = super.merge(network);

		if (newNetwork != null)
		{
			newNetwork.setBuffer(newBuffer);
			return newNetwork;
		}

		return null;
	}

	/**
	 * Temporary variable used through these two methods.
	 */
	private long energyPerWire;

	@Override
	public void split(IConductor splitPoint)
	{
		energyPerWire = this.energyBuffer / Math.max(this.getConnectors().size() - 1, 1);
		super.split(splitPoint);
	}

	@Override
	public void onSplit(IEnergyNetwork newNetwork)
	{
		newNetwork.setBuffer(newNetwork.getBuffer() + energyPerWire);
		energyBuffer -= energyPerWire;
	}

	@Override
	public IEnergyNetwork newInstance()
	{
		return EnergyNetworkLoader.getNewNetwork();
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
	public long getBufferOf(IConductor conductor)
	{
		if (this.conductorBuffer != null && this.conductorBuffer.containsKey(conductor))
		{
			if (this.conductorBuffer.get(conductor) != null)
			{
				return this.conductorBuffer.get(conductor);
			}
		}
		return 0;
	}

	@Override
	public void setBufferFor(IConductor conductor, long buffer)
	{
		this.conductorBuffer.put(conductor, buffer);
	}

}
