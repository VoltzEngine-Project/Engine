package com.builtbroken.mc.lib.helper.path;

import com.builtbroken.mc.lib.transform.vector.Pos;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A class that allows flexible pathfinding for different positions. Compared to AStar pathfinding,
 * this version is faster but does not calculated the most optimal path.
 *
 * @author Calclavia
 */
public class Pathfinder
{
	/**
	 * A pathfinding call back interface used to call back on paths.
	 */
	public IPathCallBack callBackCheck;

	/**
	 * A list of permissions that the pathfinder already went through.
	 */
	public Set<Pos> closedSet;

	/**
	 * The resulted path found by the pathfinder. Could be null if no path was found.
	 */
	public List<Pos> results;

	private Pos start;

	public Pathfinder(IPathCallBack callBack)
	{
		this.callBackCheck = callBack;
		this.reset();
	}

	/**
	 * @return True on success finding, false on failure.
	 */
	public boolean findNodes(Pos currentNode)
	{
		if (this.start == null)
		{
			this.start = currentNode;
		}

		this.closedSet.add(currentNode);

		if (this.callBackCheck.onSearch(this, this.start, currentNode))
		{
			return false;
		}

		for (Pos node : this.callBackCheck.getConnectedNodes(this, currentNode))
		{
			if (!this.closedSet.contains(node))
			{
				if (this.findNodes(node))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Called to execute the pathfinding operation.
	 */
	public Pathfinder init(Pos startNode)
	{
		this.findNodes(startNode);
		return this;
	}

	public Pathfinder reset()
	{
		this.closedSet = new LinkedHashSet<>();
		this.results = new LinkedList<>();
		return this;
	}
}
