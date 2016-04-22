package com.builtbroken.mc.lib.world.radio.network;

import com.builtbroken.mc.api.map.radio.IRadioWaveReceiver;
import com.builtbroken.mc.api.map.radio.IRadioWaveSender;
import com.builtbroken.mc.api.map.radio.wireless.*;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.world.radio.RadioMap;
import com.builtbroken.mc.lib.world.radio.RadioRegistry;

import java.util.ArrayList;
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
    public void onConnectionAdded(IWirelessConnector connector, IWirelessNetworkObject object)
    {
        //TODO notify sub parts that network has changed
        if (!attachedDevices.contains(object))
        {
            attachedDevices.add(object);
            if (object instanceof IWirelessDataPoint)
            {
                dataPoints.add((IWirelessDataPoint) object);
            }
        }
    }

    @Override
    public void onConnectionRemoved(IWirelessConnector connector, IWirelessNetworkObject object)
    {
        //TODO notify sub parts that network has changed
        if (attachedDevices.contains(object))
        {
            attachedDevices.remove(object);
            if (object instanceof IWirelessDataPoint)
            {
                dataPoints.remove(object);
            }
        }
    }

    @Override
    public void onConnectionRemoved(IWirelessConnector connector)
    {
        //TODO notify sub parts that network has changed
        if(wirelessConnectors.contains(connector))
        {
            wirelessConnectors.remove(connector);
            for(IWirelessNetworkObject obj : connector.getWirelessNetworkObjects())
            {
                attachedDevices.remove(obj);
                if(obj instanceof IWirelessDataPoint)
                {
                    dataPoints.remove(obj);
                }
            }
        }
    }


    /**
     * Called to update all connections
     */
    public void updateConnections()
    {
        //TODO find better way to handle this that doesn't result in a clean slate each time
        //TODO we need to keep track of additions and removals so sub parts can be updated
        //Clear all as we have no way to ensure these are still connected
        wirelessConnectors.clear();
        attachedDevices.clear();
        dataPoints.clear();

        //Update list if we still have a sender
        if (hub != null && hub.getRadioReceiverRange() != null)
        {
            //Get all receivers in range
            Cube range = hub.getWirelessCoverageArea();
            RadioMap map = RadioRegistry.getRadioMapForWorld(hub.world());
            List<IRadioWaveReceiver> receivers = map.getReceiversInRange(range, hub);
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
        }
        //Add sender to receiver list
        if (hub instanceof IWirelessConnector)
        {
            addConnector((IWirelessConnector) hub);
        }
        //TODO notify sub parts that network has changed
    }

    protected void addConnector(IWirelessConnector receiver)
    {
        if (!wirelessConnectors.contains(receiver))
        {
            wirelessConnectors.add(receiver);
            List<IWirelessNetworkObject> objects = receiver.getWirelessNetworkObjects();
            for (IWirelessNetworkObject object : objects)
            {
                if (!attachedDevices.contains(object))
                {
                    attachedDevices.add(object);
                    if (object instanceof IWirelessDataPoint)
                    {
                        dataPoints.add((IWirelessDataPoint) object);
                    }
                }
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
}
