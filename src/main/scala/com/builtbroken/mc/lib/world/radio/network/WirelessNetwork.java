package com.builtbroken.mc.lib.world.radio.network;

import com.builtbroken.mc.api.map.radio.IRadioWaveReceiver;
import com.builtbroken.mc.api.map.radio.IRadioWaveSender;
import com.builtbroken.mc.api.map.radio.wireless.*;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.world.radio.RadioMap;
import com.builtbroken.mc.lib.world.radio.RadioRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Object used to keep track of linked senders & receivers
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2016.
 */
public class WirelessNetwork implements IWirelessNetwork
{
    /** Frequency of the network */
    public final float hz;

    /** Primary point for the network */
    protected IWirelessNetworkHub hub;

    /** List of connectors to pull information from */
    protected final List<IWirelessConnector> wirelessConnectors = new ArrayList();

    /** Map of connectors to connections, used mainly for cleanup */
    protected final HashMap<IWirelessConnector, List<IWirelessNetworkObject>> connectorToConnections = new HashMap();
    /** Devices that are attached to the wireless network */
    protected final List<IWirelessNetworkObject> attachedDevices = new ArrayList();
    /** Quick access point for {@link #attachedDevices} that are data points */
    protected final List<IWirelessDataPoint> dataPoints = new ArrayList();

    public WirelessNetwork(float hz, IWirelessNetworkHub hub)
    {
        this.hz = hz;
        this.hub = hub;
    }

    @Override
    public IWirelessNetworkHub getPrimarySender()
    {
        return hub;
    }

    @Override
    public List<IWirelessConnector> getWirelessConnectors()
    {
        return wirelessConnectors;
    }

    @Override
    public List<IRadioWaveSender> getRelayStations()
    {
        //TODO implement
        return new ArrayList();
    }

    @Override
    public List<IWirelessNetworkObject> getAttachedObjects()
    {
        return attachedDevices;
    }

    @Override
    public boolean addConnection(IWirelessConnector connector, IWirelessNetworkObject object)
    {
        if (!attachedDevices.contains(object))
        {
            addConnection(object);
            List<IWirelessNetworkObject> objects = connectorToConnections.get(connector);
            if (objects == null)
            {
                objects = new ArrayList();
            }
            objects.add(object);
            connectorToConnections.put(connector, objects);
            return true; //TODO actually check it was added
        }
        return false;
    }

    @Override
    public boolean removeConnection(IWirelessConnector connector, IWirelessNetworkObject object)
    {
        //TODO notify sub parts that network has changed
        if (attachedDevices.contains(object))
        {
            attachedDevices.remove(object);
            List<IWirelessNetworkObject> objects = connectorToConnections.get(connector);
            if (objects == null)
            {
                objects = new ArrayList();
            }
            objects.remove(object);
            connectorToConnections.put(connector, objects);
            if (object instanceof IWirelessDataPoint)
            {
                dataPoints.remove(object);
            }
            object.removeWirelessNetwork(this, ConnectionRemovedReason.CONNECTION_LOST);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeConnector(IWirelessConnector connector)
    {
        //TODO notify sub parts that network has changed
        if (wirelessConnectors.contains(connector))
        {
            clearConnections(connector);
            return wirelessConnectors.remove(connector);
        }
        return false;
    }

    protected void clearConnections(IWirelessConnector connector)
    {
        //Clear cached connections
        List<IWirelessNetworkObject> objects = connectorToConnections.get(connector);
        if (objects != null)
        {
            for (IWirelessNetworkObject object : objects)
            {
                attachedDevices.remove(object);
                if (object instanceof IWirelessDataPoint)
                {
                    dataPoints.remove(object);
                }
            }
        }
        connectorToConnections.remove(connector);
        //Double check by clearing objects that may not be cached but still added
        for (IWirelessNetworkObject obj : connector.getWirelessNetworkObjects())
        {
            attachedDevices.remove(obj);
            if (obj instanceof IWirelessDataPoint)
            {
                dataPoints.remove(obj);
            }
        }
    }


    /**
     * Called to update all connections
     */
    public void updateConnections()
    {
        //Update list if we still have a sender
        if (hub != null && hub.getWirelessCoverageArea() != null)
        {
            //Get all receivers in range
            Cube range = hub.getWirelessCoverageArea();
            RadioMap map = RadioRegistry.getRadioMapForWorld(hub.world());
            List<IRadioWaveReceiver> receivers = map.getReceiversInRange(range, hub instanceof IRadioWaveReceiver ? (IRadioWaveReceiver) hub : null);
            //Loop threw receivers
            if (!receivers.isEmpty())
            {
                for (IRadioWaveReceiver receiver : receivers)
                {
                    if (receiver instanceof IWirelessConnector)
                    {
                        addConnector((IWirelessConnector) receiver); //Add each receiver to the list
                    }
                }
            }
            //Add sender to receiver list
            if (hub instanceof IWirelessConnector)
            {
                addConnector((IWirelessConnector) hub);
            }

            //Clear invalid connectors
            Iterator<IWirelessConnector> it2 = wirelessConnectors.iterator();
            while (it2.hasNext())
            {
                IWirelessConnector con = it2.next();
                if (con instanceof TileEntity && ((TileEntity) con).isInvalid() || con instanceof Entity && !((Entity) con).isEntityAlive())
                {
                    it2.remove();
                    clearConnections(con);
                    //TODO notify listeners
                }
            }

            //Clear invalid attached devices
            Iterator<IWirelessNetworkObject> it = attachedDevices.iterator();
            while (it.hasNext())
            {
                IWirelessNetworkObject obj = it.next();
                if (obj instanceof TileEntity && ((TileEntity) obj).isInvalid() || obj instanceof Entity && !((Entity) obj).isEntityAlive())
                {
                    it.remove();
                    obj.removeWirelessNetwork(this, ConnectionRemovedReason.TILE_INVALIDATE);
                    if (obj instanceof IWirelessDataPoint)
                    {
                        dataPoints.remove(obj);
                        //TODO notify listeners
                    }
                }
            }
        }
        else
        {
            kill();
        }
    }

    @Override
    public boolean addConnector(IWirelessConnector receiver)
    {
        if (!wirelessConnectors.contains(receiver))
        {
            boolean b = wirelessConnectors.add(receiver); //TODO if not true, stop
            List<IWirelessNetworkObject> objects = receiver.getWirelessNetworkObjects();
            for (IWirelessNetworkObject object : objects)
            {
                addConnection(object);
            }
            return b;
        }
        return false;
    }

    protected void addConnection(IWirelessNetworkObject object)
    {
        //TODO notify sub parts that network has changed
        if (!attachedDevices.contains(object))
        {
            attachedDevices.add(object);
            object.addWirelessNetwork(this);
            if (object instanceof IWirelessDataPoint)
            {
                dataPoints.add((IWirelessDataPoint) object);
            }
        }
    }

    public void kill()
    {
        //TODO notify sub parts that the connection died
        wirelessConnectors.clear();
        attachedDevices.clear();
        dataPoints.clear();
    }

    @Override
    public String toString()
    {
        return "WirelessNetwork[" + hz + ", " + dataPoints.size() + "/" + attachedDevices.size() + " dataPoints, " + hub;
    }
}
