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
import universalelectricity.api.net.NetworkEvent.EnergyProduceEvent;

/** Basic energy network that moves energy from point A to point B. If you implement this in your own
 * conductor code you will need to save the energy yourself. To do this have each conductor save its
 * buffer from conductorBuffer map. When loading from the map make sure to also load up the buffer
 * into the network as well.
 * 
 * @author Calclavia, Darkguardsman */
public class EnergyNetwork extends NodeNetwork<IEnergyNetwork, IConductor, Object> implements IEnergyNetwork
{
    /** The energy to be distributed on the next update. */
    protected long energyBuffer;

    /** The individual buffer from each conductor. Used to limit the amount of buffer that can be
     * stored. */
    protected final HashMap<IConductor, Long> conductorBuffers = new HashMap<IConductor, Long>();

    /** Resistance of the network to flow of energy, is based on total resistance of each conductor */
    protected float resistance;

    /** The total energy buffer in the last tick. */
    protected long lastEnergyBuffer;

    /** Last cached value for network demand energy */
    protected long lastNetworkRequest = -1;

    /** Temporary variable */
    private long energyPerWire;

    /** The direction in which a conductor is placed relative to a specific conductor. */
    protected final HashMap<Object, EnumSet<ForgeDirection>> handlerDirectionMap = new LinkedHashMap<Object, EnumSet<ForgeDirection>>();

    public EnergyNetwork()
    {
        super(IConductor.class);
    }

    @Override
    public void addConnector(IConductor connector)
    {
        connector.setNetwork(this);
        super.addConnector(connector);
    }

    @Override
    public void update()
    {
        /** Energy we have to move after loss has been removed */
        long usableEnergy = Math.max(energyBuffer - getEnergyLoss(), 0);
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
                    currentEnergy -= addEnergyToHandler(entry.getKey(), direction, perSide, true);
                }
                if (handlers > 1)
                {
                    handlers--;
                }
            }
            this.lastEnergyBuffer = this.energyBuffer;
            /** Don't set energy if none was sent to prevent energy loss */
            if (usableEnergy != currentEnergy)
                energyBuffer = Math.max(currentEnergy, 0);
        }
        long remainingBufferPerConductor = energyBuffer / getConnectors().size();
        Iterator<IConductor> it = getConnectors().iterator();

        while (it.hasNext())
        {
            conductorBuffers.put(it.next(), remainingBufferPerConductor);
        }

        // Clear the network request cache.
        lastNetworkRequest = -1;
    }

    /** Applies power to the machine */
    public long addEnergyToHandler(Object handler, ForgeDirection side, long energy, boolean doApply)
    {
        return CompatibilityModule.receiveEnergy(handler, side, energy, doApply);
    }

    @Override
    public boolean canUpdate()
    {
        return getConnectors().size() > 0 && getNodes().size() > 0 && energyBuffer > 0;
    }

    @Override
    public boolean continueUpdate()
    {
        return canUpdate();
    }

    @Override
    public long getRequest()
    {
        if (lastNetworkRequest == -1)
        {
            lastNetworkRequest = 0;

            if (getNodes().size() > 0)
            {
                for (Entry<Object, EnumSet<ForgeDirection>> entry : handlerDirectionMap.entrySet())
                {
                    if (entry.getValue() != null && !(entry.getValue() instanceof IConductor))
                    {
                        for (ForgeDirection direction : entry.getValue())
                        {
                            lastNetworkRequest += Math.max(CompatibilityModule.receiveEnergy(entry.getKey(), direction, Long.MAX_VALUE, false), 0);
                        }
                    }
                }
            }
        }
        return lastNetworkRequest;
    }

    @Override
    public boolean isValidConnector(Object node)
    {
        return node instanceof IConductor;
    }

    @Override
    public float getResistance()
    {
        return resistance;
    }

    /** Clears all cache and reconstruct the network. */
    @Override
    public void reconstruct()
    {
        if (getConnectors().size() > 0)
        {
            // Reset all values related to wires
            getNodes().clear();
            handlerDirectionMap.clear();
            resistance = 0;

            // Iterate threw list of wires
            Iterator<IConductor> it = getConnectors().iterator();

            while (it.hasNext())
            {
                IConductor conductor = it.next();

                if (conductor != null)
                {
                    reconstructConnector(conductor);
                }
                else
                {
                    it.remove();
                }
            }

            if (getNodes().size() > 0)
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
                reconstructHandler(conductor, conductor.getConnections()[i], ForgeDirection.getOrientation(i).getOpposite());
            }
        }

        resistance += conductor.getResistance();
    }

    /** Segmented out call so overriding can be done when machines are reconstructed. */
    protected void reconstructHandler(IConductor conductor, Object obj, ForgeDirection side)
    {
        if (obj != null && !(obj instanceof IConductor))
        {
            if (CompatibilityModule.canConnect(obj, side, conductor))
            {
                EnumSet<ForgeDirection> set = handlerDirectionMap.get(obj);
                if (set == null)
                {
                    set = EnumSet.noneOf(ForgeDirection.class);
                }
                getNodes().add(obj);
                set.add(side);
                handlerDirectionMap.put(obj, set);
            }
        }
    }

    @Override
    public IEnergyNetwork merge(IEnergyNetwork network)
    {
        IEnergyNetwork newNetwork = super.merge(network);

        if (newNetwork != null)
        {
            long newBuffer = getBuffer() + ((EnergyNetwork) network).getBuffer();
            newNetwork.setBuffer(newBuffer);
            return newNetwork;
        }

        return null;
    }

    @Override
    public void split(IConductor splitPoint)
    {
        energyPerWire = energyBuffer / Math.max(getConnectors().size() - 1, 1);
        super.split(splitPoint);
    }

    @Override
    public void onSplit(IEnergyNetwork newNetwork)
    {
        newNetwork.setBuffer(newNetwork.getBuffer() + energyPerWire);
        energyBuffer -= energyPerWire;
    }

    @Override
    public Class getConnectorClass()
    {
        return IConductor.class;
    }

    @Override
    public IEnergyNetwork newInstance()
    {
        return EnergyNetworkLoader.getNewNetwork();
    }

    @Override
    public long produce(IConductor conductor, ForgeDirection from, long amount, boolean doReceive)
    {
        long energyReceived = 0;
        long conductorBuffer = 0;
        //No energy nothing should happen
        if (amount > 0)
        {
            //Fire event giving other mods a change to interact with this network
            EnergyProduceEvent evt = new EnergyProduceEvent(this, conductor, amount, doReceive);
            MinecraftForge.EVENT_BUS.post(evt);

            if (!evt.isCanceled())
            {
                if (conductorBuffers.containsKey(conductor))
                {
                    conductorBuffer = conductorBuffers.get(conductor);
                }

                energyReceived = Math.min(Math.max(((conductor.getCurrentCapacity() * getVoltage()) - conductorBuffer), 0), amount);

                if (doReceive && energyReceived > 0)
                {
                    energyBuffer += energyReceived;
                    conductorBuffer += energyReceived;
                    conductorBuffers.put(conductor, conductorBuffer);
                }
            }
        }

        if (this.energyBuffer > 0)
            NetworkTickHandler.addNetwork(this);
        return energyReceived;
    }

    /** Assume voltage to be the default voltage for the energy network to calculate energy loss.
     * Energy Loss Forumla: Delta V = I x R; P = I x V; Therefore: P = I^2 x R */
    protected long getEnergyLoss()
    {
        long amperage = getBuffer() / getVoltage();
        return (long) ((amperage * amperage) * resistance);
    }

    public long getVoltage()
    {
        return UniversalElectricity.DEFAULT_VOLTAGE;
    }

    @Override
    public long getBuffer()
    {
        return energyBuffer;
    }

    @Override
    public void setBuffer(long newBuffer)
    {
        energyBuffer = newBuffer;
    }

    @Override
    public long getLastBuffer()
    {
        return lastEnergyBuffer;
    }

    @Override
    public long getBufferOf(IConductor conductor)
    {
        if (conductorBuffers != null && conductorBuffers.containsKey(conductor))
        {
            if (conductorBuffers.get(conductor) != null)
            {
                return conductorBuffers.get(conductor);
            }
        }

        return 0;
    }

    @Override
    public void setBufferFor(IConductor conductor, long buffer)
    {
        conductorBuffers.put(conductor, buffer);
        energyBuffer += buffer;
    }

}
