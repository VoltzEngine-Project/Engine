package com.builtbroken.mc.lib.helper.path;

import net.minecraftforge.common.util.ForgeDirection;
import com.builtbroken.mc.lib.transform.vector.Pos;

import java.util.*;

/**
 * An advanced version of pathfinding to find the shortest path between two points. Uses the A*
 * Pathfinding algorithm.
 *
 * @author Calclavia
 */
public class PathfinderAStar extends Pathfinder
{
	/**
	 * The set of tentative permissions to be evaluated, initially containing the start node
	 */
	public Set<Pos> openSet;

	/**
	 * The map of navigated permissions storing the data of which position came from which in the format
	 * of: X came from Y.
	 */
	public HashMap<Pos, Pos> navigationMap;

	/**
	 * Score values, used to determine the score for a path to evaluate how optimal the path is.
	 * G-Score is the cost along the best known path while F-Score is the total cost.
	 */
	public HashMap<Pos, Double> gScore, fScore;

	/**
	 * The node in which the pathfinder is trying to reach.
	 */
	public Pos goal;

	public PathfinderAStar(IPathCallBack callBack, Pos goal)
	{
		super(callBack);
		this.goal = goal;
	}

	@Override
	public boolean findNodes(Pos start)
	{
		this.reset();
		this.openSet.add(start);
		this.gScore.put(start, 0d);
		this.fScore.put(start, this.gScore.get(start) + getHeuristicEstimatedCost(start, this.goal));

		while (!this.openSet.isEmpty())
		{
			// Current is the node in openset having the lowest f_score[] value
			Pos currentNode = null;

			double lowestFScore = 0;

			for (Pos node : this.openSet)
			{
				if (currentNode == null || this.fScore.get(node) < lowestFScore)
				{
					currentNode = node;
					lowestFScore = this.fScore.get(node);
				}
			}

			if (currentNode == null)
			{
				break;
			}

			if (this.callBackCheck.onSearch(this, start, currentNode))
			{
				return false;
			}

			if (currentNode.equals(this.goal))
			{
				this.results = reconstructPath(this.navigationMap, goal);
				return true;
			}

			this.openSet.remove(currentNode);
			this.closedSet.add(currentNode);

			for (Pos neighbor : getNeighborNodes(currentNode))
			{
				double tentativeGScore = this.gScore.get(currentNode) + currentNode.distance(neighbor);

				if (this.closedSet.contains(neighbor))
				{
					if (tentativeGScore >= this.gScore.get(neighbor))
					{
						continue;
					}
				}

				if (!this.openSet.contains(neighbor) || tentativeGScore < this.gScore.get(neighbor))
				{
					this.navigationMap.put(neighbor, currentNode);
					this.gScore.put(neighbor, tentativeGScore);
					this.fScore.put(neighbor, gScore.get(neighbor) + getHeuristicEstimatedCost(neighbor, goal));
					this.openSet.add(neighbor);
				}
			}
		}

		return false;
	}

	@Override
	public Pathfinder reset()
	{
		this.openSet = new HashSet<>();
		this.navigationMap = new HashMap<>();
		this.gScore = new HashMap<>();
		this.fScore = new HashMap<>();
		return super.reset();
	}

	/**
	 * A recursive function to back track and find the path in which we have analyzed.
	 */
	public List<Pos> reconstructPath(HashMap<Pos, Pos> nagivationMap, Pos current_node)
	{
		List<Pos> path = new LinkedList<>();
		path.add(current_node);

		if (nagivationMap.containsKey(current_node))
		{
			path.addAll(reconstructPath(nagivationMap, nagivationMap.get(current_node)));
			return path;
		}
		else
		{
			return path;
		}
	}

	/**
	 * @return An estimated cost between two points.
	 */
	public double getHeuristicEstimatedCost(Pos start, Pos goal)
	{
		return start.distance(goal);
	}

	/**
	 * @return A Set of neighboring Vector3 positions.
	 */
	public Set<Pos> getNeighborNodes(Pos vector)
	{
		if (this.callBackCheck != null)
		{
			return this.callBackCheck.getConnectedNodes(this, vector);
		}
		else
		{
			Set<Pos> neighbors = new HashSet<>();

			for (int i = 0; i < 6; i++)
			{
				neighbors.add(vector.clone().add(ForgeDirection.getOrientation(i)));
			}

			return neighbors;
		}
	}
}
