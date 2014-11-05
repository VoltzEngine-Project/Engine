package resonant.lib.grid.electric.macroscopic.part;

import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.grid.sim.IPathNode;
import resonant.lib.grid.electric.macroscopic.PathGrid;

import java.util.HashMap;

/**
 * @author DarkCow
 */
public class Junction extends GridPart
{
	HashMap<Object, ForgeDirection> connectionMap;
	HashMap<ForgeDirection, Boolean> inputMap;
	IPathNode node = null;

	public Junction(PathGrid sim, IPathNode node)
	{
		super(sim);
		this.node = node;
	}

	public void add(Object object, ForgeDirection side)
	{
		connectionMap.put(object, side);
	}

	public void remove(Object object)
	{
		if (connectionMap.containsKey(object))
		{
			ForgeDirection direction = connectionMap.get(object);
			connectionMap.remove(object);
			inputMap.remove(direction);
		}
	}

}
