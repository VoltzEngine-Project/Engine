package com.builtbroken.api.tile;

import net.minecraftforge.common.util.ForgeDirection;
import com.builtbroken.api.tile.node.INode;

/**
 * @author Calclavia
 */
public interface INodeProvider
{
	/**
	 * @param nodeType - The type of node we are looking for.
	 * @param from     - The direction.
	 * @return Returns the node object.
	 */
	public <N extends INode> N getNode(Class<? extends N> nodeType, ForgeDirection from);
}
