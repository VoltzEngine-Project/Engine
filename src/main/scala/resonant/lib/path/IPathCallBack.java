package resonant.lib.path;

import universalelectricity.core.transform.vector.Vector3;

import java.util.Set;

public interface IPathCallBack
{
	/**
	 * @param finder      - The Pathfinder object.
	 * @param currentNode - The node being iterated through.
	 * @return A set of nodes connected to the currentNode. Essentially one should return a set of
	 * neighboring nodes.
	 */
	public Set<Vector3> getConnectedNodes(Pathfinder finder, Vector3 currentNode);

	/**
	 * Called when looping through nodes.
	 *
	 * @param finder      - The Pathfinder.
	 * @param start       - The starting node.
	 * @param currentNode - The node being searched.
	 * @return True to stop the path finding operation.
	 */
	public boolean onSearch(Pathfinder finder, Vector3 start, Vector3 currentNode);
}