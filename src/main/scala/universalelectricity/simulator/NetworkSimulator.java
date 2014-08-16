package universalelectricity.simulator;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by robert on 8/16/2014.
 */
public class NetworkSimulator
{
    /** called to calculate how data changes across the network */
    public void calculateChangeMap()
    {
        //Majority of the calculations will be threw junctions
        //The rest of major points will be modified like transformers
    }

    /** Called each update to simulate changes in data */
    public void updateSimulation()
    {

    }

    /** Is this object(most likely a tile) an input of data(most likely energy) from the network */
    public boolean isInputDevice(Object object, ForgeDirection connectingFrom)
    {
        return false;
    }

    /** Is this object(most likely a tile) an output of data(most likely energy) for the network */
    public boolean isOutputDevice(Object object, ForgeDirection connectingFrom)
    {
        return false;
    }
}
