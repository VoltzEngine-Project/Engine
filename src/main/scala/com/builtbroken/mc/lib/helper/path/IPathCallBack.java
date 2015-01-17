package com.builtbroken.mc.lib.helper.path;

import com.builtbroken.mc.lib.transform.vector.Pos;

import java.util.Set;

public interface IPathCallBack
{
	/**
	 * @param finder      - The Pathfinder object.
	 * @param currentNode - The node being iterated through.
	 * @return A set of permissions connected to the currentNode. Essentially one should return a set of
	 * neighboring permissions.
	 */
	public Set<Pos> getConnectedNodes(Pathfinder finder, Pos currentNode);

	/**
	 * Called when looping through permissions.
	 *
	 * @param finder      - The Pathfinder.
	 * @param start       - The starting node.
	 * @param currentNode - The node being searched.
	 * @return True to stop the path finding operation.
	 */
	public boolean onSearch(Pathfinder finder, Pos start, Pos currentNode);
}