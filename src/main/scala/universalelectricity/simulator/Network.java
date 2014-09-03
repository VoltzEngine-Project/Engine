package universalelectricity.simulator;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.api.core.grid.IUpdate;
import universalelectricity.compatibility.Compatibility;
import universalelectricity.core.grid.Grid;
import universalelectricity.core.grid.UpdateTicker;
import universalelectricity.core.transform.vector.VectorWorld;
import universalelectricity.simulator.peaces.NetworkNode;
import universalelectricity.simulator.peaces.NetworkWire;
import universalelectricity.simulator.peaces.WireJunction;
import universalelectricity.simulator.peaces.WirePath;

import java.util.*;

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 * @author Darkguardsman
 */
public class Network extends Grid<NetworkNode> implements IUpdate
{
    protected NetworkSimulator simulator;
    protected boolean hasChanged;
    protected Set<VectorWorld> changeLocations;
    protected long ticks = 0;
    /**
     * @param nodes - any node to init the network with
     */
    public Network(NetworkNode... nodes)
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
        if(getSimulator() != null)
        {
            getSimulator().updateSimulation();
        }
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
        //Make sure all nodes have constructed wire connections
        //Collect connection date and formulate all inputs/outputs to machines
        //From here map out all junctions and wire paths creating the simulation network
        //Have simulator calculate out delta changes across sections
        //Que first simulation of network data

        //Mix of a path finder and logic builder
       
    }

    @Override
    public boolean canUpdate()
    {
        return getSimulator() != null;
    }

    @Override
    public boolean continueUpdate()
    {
        return getNodes().size() > 0;
    }

    public NetworkSimulator getSimulator()
    {
        return simulator;
    }
}
