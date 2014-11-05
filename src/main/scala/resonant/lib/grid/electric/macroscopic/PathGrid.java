package resonant.lib.grid.electric.macroscopic;

import resonant.api.grid.IUpdate;
import resonant.api.grid.sim.IPathNode;
import resonant.lib.grid.Grid;
import resonant.lib.grid.UpdateTicker;
import resonant.lib.grid.electric.macroscopic.part.GridPart;
import resonant.lib.grid.electric.macroscopic.node.IComponent;

import java.util.List;

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 *
 * @author Darkguardsman
 */
public class PathGrid extends Grid<IPathNode> implements IUpdate
{
	/**
	 * Marks the grid to be rebuilt
	 */
	protected boolean changed;

	/**
	 * Current update cycle count, resets to 1 every time it maxes out
	 */
	protected long ticks = 0;

	List<GridPart> parts;

	/**
	 * @param nodes - any node to init the network with
	 */
	public PathGrid(IPathNode... nodes)
	{
		super(IPathNode.class);
		changed = false;

		for (IPathNode node : nodes)
		{
			add(node);
		}

		UpdateTicker.addUpdater(this);
	}

	@Override
	public void add(IPathNode node)
	{
		changed = true;
		super.add(node);
	}

	@Override
	public void remove(IPathNode node)
	{
		changed = true;
		super.remove(node);
	}

	@Override
	public void update(double deltaTime)
	{
		ticks++;

		if (ticks == 1)
		{
			rebuild();
		}
		else if (ticks + 1 >= Long.MAX_VALUE)
		{
			ticks = 2;
		}

		if (changed)
		{
			//updateConnections();
			changed = false;
			rebuild();
		}

		//TODO: We only have to simulate when emf changes
		simulation(deltaTime);
	}

	/**
	 * Maps the entire network out from start to finish
	 */
	public void rebuild()
	{
		//Clear cached network
		if (parts != null)
		{
			for (IComponent comp : parts)
			{
				comp.destroy();
			}

			parts = null;
		}

		// Ask all nodes to rebuild there connections
		// TODO maybe do a first build check so we don't double reconstruct on world load
		for (IPathNode node : getNodes())
		{
			node.reconstruct();
		}

		//TODO Collect connection data and formulate all inputs/outputs to machines
		//Trigger pathfinder to build our simulator parts that wrapper the nodes
		GridPathfinder networkPathFinder = new GridPathfinder(this);
		parts = networkPathFinder.generateParts();

		// Init the parts
		for (IComponent comp : parts)
		{
			comp.build();
		}

		//Get delta points to correctly simulate changes in the network
		calculateDeltaPoints();
	}

	/**
	 * Called each update to simulate changes in the grid
	 *
	 * @param deltaTime Time between successive updates
	 */
	public void simulation(double deltaTime)
	{
		//TODO grab delta changes of the network
		//TODO maybe cache delta changes and check if they change every few ticks
	}

	/**
	 * Called to calculate the points of change in the network
	 */
	public void calculateDeltaPoints()
	{

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
