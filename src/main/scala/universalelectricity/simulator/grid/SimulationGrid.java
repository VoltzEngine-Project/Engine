package universalelectricity.simulator.grid;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.api.core.grid.IUpdate;
import universalelectricity.compatibility.Compatibility;
import universalelectricity.core.grid.Grid;
import universalelectricity.core.grid.UpdateTicker;
import universalelectricity.core.transform.vector.VectorWorld;
import universalelectricity.simulator.parts.NetworkNode;
import universalelectricity.simulator.parts.NetworkPart;
import universalelectricity.simulator.parts.NetworkWire;

import java.util.*;

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 * @author Darkguardsman
 */
public class SimulationGrid extends Grid<NetworkNode> implements IUpdate
{
    protected boolean hasChanged;
    protected Set<VectorWorld> changeLocations;
    protected long ticks = 0;
    /**
     * @param nodes - any node to init the network with
     */
    public SimulationGrid(NetworkNode... nodes)
    {
        super(NetworkWire.class);
        hasChanged = false;
        changeLocations = new HashSet<VectorWorld>();
        for(NetworkNode node : nodes)
        {
            add(node);
        }
        UpdateTicker.addUpdater(this);
    }

    @Override
    public void add(NetworkNode node)
    {
        hasChanged = true;
        super.add(node);
    }

    @Override
    public void remove(NetworkNode node)
    {
        hasChanged = true;
        //changeLocations.add(new VectorWorld(node));
        super.remove(node);
    }

    @Override
    public void update(double deltaTime)
    {
        ticks++;
        if(ticks == 1)
        {
            buildEntireNetwork();
        }else if(ticks + 1 >= Long.MAX_VALUE)
        {
            ticks = 2;
        }
        if(hasChanged)
        {
            //updateConnections();
            hasChanged = false;
            this.buildEntireNetwork();

        }
        updateSimulation();
    }

    /** Updates changes to connections preventing a network wide remap */
    public void updateConnections()
    {
        //For each connection change get part nearby and decide how it effects the network
        //If the change creates a new junction, split wire part into wire sections & a new junction
        //If the change just adds to a wire part add the node to the wire part connected
        //If the change connects in a new machine add a new input/output then resim the network
        for(VectorWorld vec : changeLocations)
        {
            int connections = 0;
            for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                TileEntity tile = vec.clone().add(direction).getTileEntity();
                if(tile instanceof INodeProvider)
                {
                    INode node = ((INodeProvider) tile).getNode(NetworkNode.class, direction.getOpposite());
                    if(node instanceof NetworkNode && ((NetworkNode) node).getGrid() == this)
                    {
                        connections++;
                        continue;
                    }
                }
                if(Compatibility.isHandler(tile))
                {
                    connections++;
                }
            }

            //Was a junction
            if(connections > 2)
            {

            } else
            //Was a wire connection
            if(connections > 1)
            {
                //TODO check if the network split and rebuild connections
            }
        }
    }

    /** Maps the entire network out from start to finish */
    public void buildEntireNetwork()
    {
        // Ask all nodes to rebuild there connections
        for(NetworkNode node : getNodes())
        {
            node.reconstruct();
        }
        //TODO Collect connection data and formulate all inputs/outputs to machines

        //Trigger pathfinder to build our simulator parts that wrapper the nodes
        GridPathfinder networkPathFinder = new GridPathfinder(this);
        List<NetworkPart> parts = networkPathFinder.generateParts();
        // TODO cache parts
        
        //Get delta points to correctly simulate changes in the network
        calculateDeltaPoints();

        //Que first simulation of network data
        updateSimulation();
    }

    /** Called each update to simulate changes in data */
    public void updateSimulation()
    {
        //TODO grab delta changes of the network
        //TODO maybe cache delta changes and check if they change every few ticks
    }

    /** Called to calculate the points of change in the network */
    public void calculateDeltaPoints()
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

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public boolean continueUpdate()
    {
        return getNodes().size() > 0;
    }
}
