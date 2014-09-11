package universalelectricity.simulator.grid;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.IUpdate;
import universalelectricity.api.core.grid.sim.ISimNode;
import universalelectricity.core.grid.Grid;
import universalelectricity.core.grid.UpdateTicker;
import universalelectricity.core.transform.vector.VectorWorld;
import universalelectricity.simulator.grid.component.SimNode;
import universalelectricity.simulator.grid.component.NetworkPart;

import java.util.*;

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 * @author Darkguardsman
 */
public class SimulatedGrid extends Grid<ISimNode> implements IUpdate
{
    /** Marks the grid to be rebuilt */
    protected boolean hasChanged;

    /** Current update cycle count, resets to 1 every time it maxes out */
    protected long ticks = 0;
    /** @param nodes - any node to init the network with */
    public SimulatedGrid(ISimNode... nodes)
    {
        super(ISimNode.class);
        hasChanged = false;
        for(ISimNode node : nodes)
        {
            add(node);
        }
        UpdateTicker.addUpdater(this);
    }

    @Override
    public void add(ISimNode node)
    {
        hasChanged = true;
        super.add(node);
    }

    @Override
    public void remove(ISimNode node)
    {
        hasChanged = true;
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

    /** Maps the entire network out from start to finish */
    public void buildEntireNetwork()
    {
        // Ask all nodes to rebuild there connections
        for(SimNode node : getNodes())
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
